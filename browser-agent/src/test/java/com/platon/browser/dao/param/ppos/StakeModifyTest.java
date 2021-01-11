package com.platon.browser.dao.param.ppos;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeModifyTest extends AgentTestBase {

    @Test
    public void test(){
        StakeModify target = StakeModify.builder()
                .nodeId("null")
                .stakingBlockNum(BigInteger.ONE)
                .benefitAddr("null")
                .details("null")
                .externalId("null")
                .isInit(3)
                .nodeName("null")
                .webSite("null")
                .build();

        target.setNodeId(null)
            .setStakingBlockNum(null)
            .setBenefitAddr(null)
            .setDetails(null)
            .setExternalId(null)
            .setIsInit(3)
            .setNodeName(null)
            .setWebSite(null);

        target.getNodeId();
        target.getStakingBlockNum();
        target.getBenefitAddr();
        target.getDetails();
        target.getExternalId();
        target.getIsInit();
        target.getNodeName();
        target.getWebSite();

        target.getBusinessType();
        assertTrue(true);
    }
}
