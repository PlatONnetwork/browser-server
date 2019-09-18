package com.platon.browser.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.service.CandidateService;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private PlatonClient client;
    @Autowired
    private SpecialContractApi sca;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle(EventContext context) throws Exception {
        updateValidator(); // 更新缓存中的辅助共识周期验证人信息
        updateStaking(); // 更新质押相关信息
    }

    /**
     * 更新与质押相关的信息
     */
    private void updateStaking() throws NoSuchBeanException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();

        List<CustomStaking> stakingList = nodeCache.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        // <节点ID, 前一共识轮出块数(PRE_QTY),当前共识轮出块数(CUR_QTY),验证轮数(VER_ROUND)>
        Map<String,String> consensusInfo = new HashMap<>();
        String tpl = "前一共识轮出块数(PRE_QTY),当前共识轮出块数(CUR_QTY),验证轮数(VER_ROUND)";
        for (CustomStaking staking:stakingList){
            Node nextNode = bc.getCurValidator().get(staking.getNodeId());
            // 看当前验证人是否在下一轮共识
            if(nextNode!=null){
                staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
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

        // 下一轮验证人提前设置验证轮数：验证周期轮数+1
        for (Node node:bc.getCurValidator().values()){
            CustomNode customNode = nodeCache.getNode(HexTool.prefix(node.getNodeId()));
            // 节点经过的共识周期轮数+1
            customNode.setStatVerifierTime(customNode.getStatVerifierTime()+1);
            // 累加共识周期期望区块数（提前设置下一轮期望的出块数）
            BigDecimal statExpectQty = new BigDecimal(customNode.getStatExpectBlockQty());
            statExpectQty=statExpectQty.add(bc.getCurConsensusExpectBlockCount());
            customNode.setStatExpectBlockQty(statExpectQty.toString());
            try {
                CustomStaking latestStaking = customNode.getLatestStaking();
                // 节点最新质押记录经过的共识轮数+1
                latestStaking.setStatVerifierTime(latestStaking.getStatVerifierTime()+1);
            } catch (NoSuchBeanException e) {
                logger.debug("无质押，不处理");
            }
        }
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
        List <Node> preValidator;
        // ==================================更新前一周期验证人列表=======================================

        BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
        while (true){
            try {
                // 使用当前区块号查询前一共识周期验证人
                preValidator = sca.getHistoryValidatorList(client.getWeb3j(),prevEpochLastBlockNumber);
                bc.getPreValidator().clear();
                preValidator.stream().filter(Objects::nonNull).forEach(node -> bc.getPreValidator().put(HexTool.prefix(node.getNodeId()), node));
                logger.debug("前一轮共识周期(未块:{})验证人:{}",blockNumber,JSON.toJSONString(preValidator,true));
                break;
            } catch (Exception e) {
                logger.error("【查询前轮共识验证人-底层出错】使用块号【{}】查询共识周期验证人出错,将重试:{}",prevEpochLastBlockNumber,e.getMessage());
            }
        }

        // ==================================更新下一共识周期验证人列表=======================================
        List <Node> curValidator;
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
        try {
            // 先使用区块号查询
            curValidator = sca.getHistoryValidatorList(client.getWeb3j(),nextEpochFirstBlockNumber);
            logger.debug("下一轮共识周期验证人(始块:{}):{}",nextEpochFirstBlockNumber,JSON.toJSONString(curValidator,true));
        }catch (Exception e){
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            curValidator = candidateService.getCurValidators();
            logger.debug("下一轮共识周期验证人(实时):{}",JSON.toJSONString(curValidator,true));
        }

        bc.getCurValidator().clear();
        curValidator.stream().filter(Objects::nonNull).forEach(node -> bc.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));

        if(bc.getCurValidator().size()==0){
            throw new CandidateException("查询不到下一轮共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+bc.getCurConsensusEpoch()+")");
        }

        // 更新当前共识周期期望出块数
        bc.updateCurConsensusExpectBlockCount(bc.getCurValidator().size());

        logger.debug("下一轮共识周期验证人:{}",JSON.toJSONString(bc.getCurValidator(),true));
    }
}
