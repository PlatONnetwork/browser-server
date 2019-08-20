package com.platon.browser.engine.handler;

import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.result.StakingExecuteResult;
import com.platon.browser.exception.ConsensusEpochChangeException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.exception.SettleEpochChangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
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
    private StakingExecuteResult executeResult;
    private BlockChain bc;

    @Override
    public void handle(EventContext context) throws ConsensusEpochChangeException {
        nodeCache = context.getNodeCache();
        executeResult = context.getExecuteResult();
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
            executeResult.stageUpdateStaking(staking);
        }

    }

}
