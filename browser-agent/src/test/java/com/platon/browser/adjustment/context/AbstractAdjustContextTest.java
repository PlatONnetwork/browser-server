package com.platon.browser.adjustment.context;

import com.platon.browser.AgentTestData;
import com.platon.browser.adjustment.bean.AdjustParam;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AbstractAdjustContextTest extends AgentTestData {
    @Test
    public void testStakingAdjustContext(){
        AdjustParam adjustParam = null;
        for (AdjustParam e : adjustParamList) {
            if ("staking".equals(e.getOptType())){
                adjustParam = e;
                break;
            }
        }
        if(adjustParam==null) return;
        StakingAdjustContext sac = new StakingAdjustContext();
        sac.setAdjustParam(adjustParam);
        sac.setNode(null);
        sac.setStaking(null);
        AbstractAdjustContext ac = sac;
        List<String> errors = ac.validate();
        Assert.assertEquals(2,errors.size());
        String errorInfo = ac.errorInfo();
        Assert.assertTrue(errorInfo.contains("质押记录"));
        Assert.assertTrue(errorInfo.contains("节点记录"));
    }

    @Test
    public void testDelegateAdjustContext(){
        AdjustParam adjustParam = null;
        for (AdjustParam e : adjustParamList) {
            if ("delegate".equals(e.getOptType())){
                adjustParam = e;
                break;
            }
        }
        if(adjustParam==null) return;
        DelegateAdjustContext dac = new DelegateAdjustContext();
        dac.setAdjustParam(adjustParam);
        dac.setNode(null);
        dac.setStaking(null);
        dac.setDelegation(null);
        AbstractAdjustContext ac = dac;
        List<String> errors = ac.validate();
        Assert.assertEquals(3,errors.size());
        String errorInfo = ac.errorInfo();
        Assert.assertTrue(errorInfo.contains("质押记录"));
        Assert.assertTrue(errorInfo.contains("节点记录"));
        Assert.assertTrue(errorInfo.contains("委托记录"));
    }
}
