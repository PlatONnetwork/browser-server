package com.platon.browser.engine.handler;

import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.ConsensusEpochChangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * 结算周期变更事件处理类
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description:
 */
@Component
public class NewConsensusEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandler.class);
    private NodeCache nodeCache;
    private StakingStage stakingStage;
    private BlockChain bc;

    @Override
    public void handle(EventContext context) throws ConsensusEpochChangeException {
        nodeCache = context.getNodeCache();
        stakingStage = context.getStakingStage();
        bc = context.getBlockChain();

        List<CustomStaking> stakings = nodeCache.getStakingByStatus(Collections.singletonList(CustomStaking.StatusEnum.CANDIDATE));
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

    }

}
