package com.platon.browser.engine.cache;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.UnDelegation;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.platon.browser.engine.BlockChain.ADDRESS_CACHE;
import static com.platon.browser.engine.BlockChain.NODE_CACHE;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 18:03
 * @Description: 地址缓存更新器
 */
@Component
public class AddressCacheUpdater {
    @Autowired
    private BlockChain bc;
    /**
     * 2.补充统计地址相关数据，在批量采集后批量入库前执行
     *      a.staking_value  质押的金额
     *      b.delegate_value  委托的金额
     *      c.redeemed_value   赎回中的金额，包含委托和质押
     *      d.candidate_count   已委托的验证人
     *      e.delegate_hes   未锁定委托
     *      f.delegate_locked   已锁定委托
     *      g.delegate_unlock  已经解锁的
     *      h.delegate_reduction  赎回中的
     *
     */
    public void updateAddressStatistics () throws BusinessException {
        for (CustomAddress customAddress : ADDRESS_CACHE.getAllAddress()) {
            BigInteger stakingValue = BigInteger.ZERO;
            BigInteger delegateValue = BigInteger.ZERO;
            BigInteger statkingRedeemed = BigInteger.ZERO;
            BigInteger delegateReddemed = BigInteger.ZERO;
            BigInteger redeemedValue = BigInteger.ZERO;
            BigInteger candidateCount = BigInteger.ZERO;
            BigInteger delegateHes = BigInteger.ZERO;
            BigInteger delegateLocked = BigInteger.ZERO;
            BigInteger delegateUnlock = BigInteger.ZERO;
            BigInteger delegateReduction = BigInteger.ZERO;
            for (CustomStaking stakings : NODE_CACHE.getAllStaking()) {
                if (stakings.getStakingAddr().equals(customAddress.getAddress())) {
                    stakingValue = stakingValue.add(new BigInteger(stakings.getStakingHas()).add(new BigInteger(stakings.getStakingLocked())));
                    statkingRedeemed = statkingRedeemed.add(new BigInteger(stakings.getStakingReduction()));
                }

            }
            for (CustomDelegation delegation : NODE_CACHE.getDelegationByIsHistory(CustomDelegation.YesNoEnum.NO)) {
                if (delegation.getDelegateAddr().equals(customAddress.getAddress())) {
                    delegateValue = delegateValue.add(new BigInteger(delegation.getDelegateHas()).add(new BigInteger(delegation.getDelegateLocked())));
                    delegateReddemed = delegateReddemed.add(new BigInteger(delegation.getDelegateReduction()));
                    delegateHes = delegateHes.add(new BigInteger(delegation.getDelegateHas()));
                    delegateLocked = delegateLocked.add(new BigInteger(delegation.getDelegateLocked()));
                    delegateReduction = delegateReduction.add(new BigInteger(delegation.getDelegateReduction()));
                    candidateCount = candidateCount.add(BigInteger.ONE);
                    Integer status = 0;
                    try {
                        CustomStaking staking = NODE_CACHE.getStaking(delegation.getNodeId(),delegation.getStakingBlockNum());
                        status = staking.getStatus();
                    } catch (NoSuchBeanException e) {
                        throw new BusinessException(e.getMessage());
                    }
                    if (status.equals(CustomStaking.StatusEnum.EXITING.code) || status.equals(CustomStaking.StatusEnum.EXITED.code)) {
                        delegateUnlock = delegateUnlock.add(new BigInteger(delegation.getDelegateHas()));
                    }
                }
            }
            redeemedValue = statkingRedeemed.add(delegateReddemed);

            //address引用对象更新
            customAddress.setStakingValue(stakingValue.toString());
            customAddress.setDelegateValue(delegateValue.toString());
            customAddress.setRedeemedValue(redeemedValue.toString());
            customAddress.setCandidateCount(candidateCount.intValue());
            customAddress.setDelegateValue(delegateHes.toString());
            customAddress.setDelegateLocked(delegateLocked.toString());
            customAddress.setDelegateUnlock(delegateUnlock.toString());
            customAddress.setDelegateReduction(delegateReduction.toString());
        }
    }
}
