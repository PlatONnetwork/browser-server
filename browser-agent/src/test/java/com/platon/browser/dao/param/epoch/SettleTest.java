package com.platon.browser.dao.param.epoch;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 结算周期切换参数入库
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SettleTest extends AgentTestBase {

    @Test
    public void test(){
        Settle target = Settle.builder()
                .curVerifierSet(Collections.emptySet())
                .preVerifierSet(Collections.emptySet())
                .settingEpoch(3)
                .stakingLockEpoch(3)
                .stakingReward(BigDecimal.TEN)
                .stakingList(Collections.emptyList())
                .build();
        target.setStakingList(Collections.emptyList())
                .setCurVerifierSet(Collections.emptySet())
                .setPreVerifierSet(Collections.emptySet())
                .setSettingEpoch(3)
                .setStakingLockEpoch(3)
                .setStakingReward(null);
        target.getSettingEpoch();
        target.getStakingLockEpoch();
        target.getStakingReward();
        target.getStakingList();
        target.getCurVerifierSet();
        target.getPreVerifierSet();
        target.getBusinessType();
        assertTrue(true);
    }

}