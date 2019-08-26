package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.ConsensusEpochChangeException;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.List;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 结算周期变更事件处理类
 */
@Component
public class NewConsensusEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandler.class);

    @Override
    public void handle(EventContext context) throws ConsensusEpochChangeException {
        NodeCache nodeCache = context.getNodeCache();
        StakingStage stakingStage = context.getStakingStage();
        BlockChain bc = context.getBlockChain();

        List<CustomStaking> stakings = nodeCache.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE);
        for (CustomStaking staking:stakings){
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
}
