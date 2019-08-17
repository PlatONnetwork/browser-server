package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.dto.CustomUnDelegation;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.result.StakingExecuteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
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
public class NewSettleEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewSettleEpochHandler.class);
    private CustomTransaction tx;
    private NodeCache nodeCache;
    private StakingExecuteResult executeResult;
    private BlockChain bc;

    @Override
    public void handle(EventContext context) {
        tx = context.getTransaction();
        nodeCache = context.getNodeCache();
        executeResult = context.getExecuteResult();
        bc = context.getBlockChain();
        stakingSettle();
        modifyDelegationInfoOnNewSettingEpoch();
        modifyUnDelegationInfoOnNewSettingEpoch();
    }


    //结算周期变更导致的委托数据的变更
    private void modifyDelegationInfoOnNewSettingEpoch () {
        //由于结算周期的变更，对所有的节点下的质押的委托更新
        //只需变更不为历史节点的委托数据(isHistory=NO(2))
        List<CustomDelegation> delegations = nodeCache.getDelegationByIsHistory(Collections.singletonList(CustomDelegation.YesNoEnum.NO));
        delegations.forEach(delegation->{
            //经过结算周期的变更，上个周期的犹豫期金额累加到锁定期的金额
            delegation.setDelegateLocked(new BigInteger(delegation.getDelegateLocked()).add(new BigInteger(delegation.getDelegateHas())).toString());
            //累加后的犹豫期金额至为0
            delegation.setDelegateHas("0");
            delegation.setDelegateReduction("0");
            //并判断经过一个结算周期后该委托的对应赎回是否全部完成，若完成则将委托设置为历史节点
            //判断条件委托的犹豫期金额 + 委托的锁定期金额 + 委托的赎回金额是否等于0
            if (new BigInteger(delegation.getDelegateHas()).add(new BigInteger(delegation.getDelegateLocked())).add(new BigInteger(delegation.getDelegateReduction())) == BigInteger.ZERO) {
                delegation.setIsHistory(CustomDelegation.YesNoEnum.YES.code);
            }
            //添加需要更新的委托的信息到委托更新列表
            executeResult.stageUpdateDelegation(delegation);
        });
    }

    //结算周期变更导致的委托赎回的变更
    private void modifyUnDelegationInfoOnNewSettingEpoch () {
        //由于结算周期的变更，对所有的节点下的质押的委托的委托赎回更新
        //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
        List<CustomUnDelegation> unDelegations = nodeCache.getUnDelegationByStatus(Collections.singletonList(CustomUnDelegation.StatusEnum.EXITING));
        unDelegations.forEach(unDelegation -> {
            //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
            unDelegation.setRedeemLocked("0");
            //当锁定期金额为零时，认为此笔赎回委托交易已经完成
            unDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.code);
            //添加需要更新的赎回委托信息到赎回委托更新列表
            executeResult.stageUpdateUnDelegation(unDelegation);
        });
    }

    /**
     * 质押结算
     * 对所有候选中和退出中的节点进行结算
     */
    private void stakingSettle() {
        // 结算周期切换时对所有候选中和退出中状态的节点进行结算
        List<CustomStaking> stakings = nodeCache.getStakingByStatus(Arrays.asList(CustomStaking.StatusEnum.CANDIDATE,CustomStaking.StatusEnum.EXITING));
        stakings.forEach(staking -> {
            // 调整金额状态
            BigInteger stakingLocked = new BigInteger(staking.getStakingLocked()).add(new BigInteger(staking.getStakingHas()));
            staking.setStakingLocked(stakingLocked.toString());
            staking.setStakingHas("0");
            if(bc.getCurSettingEpoch() > staking.getStakingReductionEpoch()+1){
                //
                staking.setStakingReduction("0");
            }
            BigInteger stakingReduction = new BigInteger(staking.getStakingReduction());
            if(stakingLocked.add(stakingReduction).compareTo(BigInteger.ZERO)==0){
                staking.setStatus(CustomStaking.StatusEnum.EXITED.code);
            }
            // 计算质押激励和年化率
        });
    }
}
