package com.platon.browser.engine.handler.epoch;

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
import com.platon.browser.exception.ConsensusEpochChangeException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

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
    public void handle(EventContext context) throws ConsensusEpochChangeException, CandidateException {
        stakingStage = context.getStakingStage();

        updateValidator(); // 更新缓存中的辅助共识周期验证人信息
        updateStaking(); // 更新质押相关信息
    }

    /**
     * 更新与质押相关的信息
     */
    private void updateStaking(){
        List<CustomStaking> stakingList = NODE_CACHE.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        for (CustomStaking staking:stakingList){
            Node node = bc.getCurValidator().get(staking.getNodeId());
            if(node!=null){
                staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
                staking.setStatVerifierTime(staking.getStatVerifierTime()+1);
            }else {
                staking.setIsConsensus(CustomStaking.YesNoEnum.NO.code);
            }
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
    private void updateValidator() throws CandidateException {
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        try {
            BaseResponse<List <Node>> result;
            // ==================================更新前一周期验证人列表=======================================
            bc.getPreValidator().clear();
            bc.getPreValidator().putAll(bc.getCurValidator());
            if(bc.getCurValidator().size()>0){
                // 入参区块号属于前一共识周期，因此可以通过它查询前一共识周期验证人历史列表
                BigInteger prevEpochFirstBlockNumber = BigInteger.valueOf(blockNumber).subtract(chainConfig.getConsensusPeriodBlockCount()).add(BigInteger.ONE);
                result = client.getHistoryValidatorList(prevEpochFirstBlockNumber);
                if (!result.isStatusOk()) {
                    throw new CandidateException(String.format("【底层出错】查询块号在【%s】的共识周期验证人历史出错:[可能原因:(1.Agent共识周期块数设置与链上不一致;2.底层链在共识周期切换块号【%s】未记录共识周期验证人历史.),当前块号({}),错误详情:%s]",prevEpochFirstBlockNumber,prevEpochFirstBlockNumber,blockNumber,result.errMsg));
                }else{
                    bc.getPreValidator().clear();
                    result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getPreValidator().put(HexTool.prefix(node.getNodeId()), node));
                }
            }

            // ==================================更新当前周期验证人列表=======================================
            BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
            result = client.getHistoryValidatorList(nextEpochFirstBlockNumber);
            if (!result.isStatusOk()) {
                // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
                result = client.getNodeContract().getValidatorList().send();
                if(!result.isStatusOk()){
                    throw new CandidateException(String.format("【底层出错】查询实时共识周期验证人出错:%s",result.errMsg));
                }
            }
            bc.getCurValidator().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));
        } catch (Exception e) {
            throw new CandidateException(e.getMessage());
        }
        if(bc.getCurValidator().size()==0){
            throw new CandidateException("查询不到共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+bc.getCurConsensusEpoch()+")");
        }
    }
}
