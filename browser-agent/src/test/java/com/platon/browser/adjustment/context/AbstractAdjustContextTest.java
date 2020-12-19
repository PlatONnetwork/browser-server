package com.platon.browser.adjustment.context;

import com.platon.browser.AgentTestData;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AbstractAdjustContextTest extends AgentTestData {
    @Test
    public void testContext(){
        StakingAdjustContext sac = new StakingAdjustContext();
        sac.setAdjustParam(stakingAdjustParamList.get(0));
        sac.setNode(null);
        sac.setStaking(null);
        AbstractAdjustContext ac = sac;
        List<String> errors = ac.validateContext();
        Assert.assertEquals(2,errors.size());

        DelegateAdjustContext dac = new DelegateAdjustContext();
        dac.setAdjustParam(delegateAdjustParamList.get(0));
        dac.setNode(null);
        dac.setStaking(null);
        dac.setDelegation(null);
        ac = dac;
        errors = ac.validateContext();
        Assert.assertEquals(3,errors.size());
    }
}
