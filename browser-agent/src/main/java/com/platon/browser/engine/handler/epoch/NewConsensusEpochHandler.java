package com.platon.browser.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static java.lang.String.format;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 结算周期变更事件处理类
 */
@Component
public class NewConsensusEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatonClient client;
    private StakingStage stakingStage;

    @Override
    public void handle(EventContext context) throws Exception {
        stakingStage = context.getStakingStage();

        updateValidator(); // 更新缓存中的辅助共识周期验证人信息
        updateStaking(); // 更新质押相关信息
    }

    /**
     * 更新与质押相关的信息
     */
    private void updateStaking(){
        List<CustomStaking> stakingList = NODE_CACHE.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        //if(stakingList.size()!=4) throw new RuntimeException("质押信息少于4条！");
        Long blockNumber = bc.getCurBlock().getNumber();
        // <节点ID, 前一共识轮出块数(PRE_QTY),当前共识轮出块数(CUR_QTY),验证轮数(VER_ROUND)>
        Map<String,String> consensusInfo = new HashMap<>();
        String tpl = "前一共识轮出块数(PRE_QTY),当前共识轮出块数(CUR_QTY),验证轮数(VER_ROUND)";
        for (CustomStaking staking:stakingList){
            Node node = bc.getCurValidator().get(staking.getNodeId());
            if(node!=null){
                staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
                staking.setStatVerifierTime(staking.getStatVerifierTime()+1);
            }else {
                staking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
            }

            String info = tpl.replace("PRE_QTY",staking.getPreConsBlockQty().toString())
                    .replace("CUR_QTY",staking.getCurConsBlockQty().toString())
                    .replace("VER_ROUND",staking.getStatVerifierTime().toString());
            consensusInfo.put(staking.getNodeId(),info);
            staking.setPreConsBlockQty(staking.getCurConsBlockQty());
            staking.setCurConsBlockQty(BigInteger.ZERO.longValue());
            stakingStage.updateStaking(staking);
        }

        // 更新node表中的共识验证轮数: stat_verifier_time
        bc.getCurValidator().forEach((nodeId,validator)->{
            try {
                CustomNode node = NODE_CACHE.getNode(nodeId);
                node.setStatVerifierTime(node.getStatVerifierTime()+1);
            } catch (NoSuchBeanException e) {
                logger.error("更新共识验证人(nodeId={})验证轮数出错:{}",nodeId,e.getMessage());
            }
        });

        logger.debug("质押节点共识信息：{}", JSON.toJSONString(consensusInfo,true));
    }

    /**
     * 更新共识周期验证人和结算周期验证人映射缓存
     * // 假设当前链上最高区块号为750
     * 1         250        500        750
     * |----------|----------|----------|
     * A B C      A C D       B C D
     * 共识周期的临界块号分别是：1,250,500,750
     * 使用临界块号查到的验证人：1=>"A,B,C",250=>"A,B,C",500=>"A,C,D",750=>"B,C,D"
     * 如果当前区块号为753，由于未达到
     */
    private void updateValidator() throws Exception {
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        BaseResponse<List <Node>> result;
        // ==================================更新前一周期验证人列表=======================================
        bc.getPreValidator().clear();
        bc.getPreValidator().putAll(bc.getCurValidator());
        if(bc.getPreValidator().size()>0){
            logger.debug("前一轮共识周期(最后块号{})验证人(倒换):{}",blockNumber,JSON.toJSONString(bc.getPreValidator(),true));
        }
        if(bc.getPreValidator().size()==0){
            // 取入参区块号的前一共识周期结束块号，因此可以通过它查询前一共识周期验证人历史列表
            BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
            try {
                result = client.getHistoryValidatorList(prevEpochLastBlockNumber);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CandidateException(format("【查询前轮共识验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",prevEpochLastBlockNumber,e.getMessage()));
            }
            if (!result.isStatusOk()) {
                throw new CandidateException(format("【查询前轮共识验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",prevEpochLastBlockNumber,result.errMsg));
            }else{
                bc.getPreValidator().clear();
                result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getPreValidator().put(HexTool.prefix(node.getNodeId()), node));
                logger.debug("前一轮共识周期(最后块号{})验证人(查{}):{}",blockNumber,blockNumber,JSON.toJSONString(bc.getPreValidator(),true));
            }
        }


        // ==================================更新当前周期验证人列表=======================================
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+chainConfig.getConsensusPeriodBlockCount().longValue());
        result = client.getHistoryValidatorList(nextEpochFirstBlockNumber);
        if(result.isStatusOk()){
            bc.getCurValidator().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));
            logger.debug("当前轮共识周期验证人(查{}):{}",nextEpochFirstBlockNumber,JSON.toJSONString(bc.getCurValidator(),true));
        }
        if (!result.isStatusOk()) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                result = client.getNodeContract().getValidatorList().send();
                bc.getCurValidator().clear();
                result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));
                logger.debug("当前轮共识周期验证人(实时):{}",JSON.toJSONString(bc.getCurValidator(),true));
            } catch (Exception e) {
                e.printStackTrace();
                throw new CandidateException(format("【查询当前共识验证人-底层出错】查询实时共识周期验证人出错:%s",e.getMessage()));
            }
            if(!result.isStatusOk()){
                throw new CandidateException(format("【查询当前共识验证人-底层出错】查询实时共识周期验证人出错:%s",result.errMsg));
            }
        }


        if(bc.getCurValidator().size()==0){
            throw new CandidateException("查询不到共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+bc.getCurConsensusEpoch()+")");
        }
    }
}
