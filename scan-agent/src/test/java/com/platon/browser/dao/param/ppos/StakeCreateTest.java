package com.platon.browser.dao.param.ppos;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeCreateTest extends AgentTestBase {

    @Test
    public void test(){
        StakeCreate target = StakeCreate.builder()
                .benefitAddr("null")
                .bigVersion("null")
                .details("null")
                .externalId("null")
                .isInit(3)
                .joinTime(new Date())
                .nodeId("null")
                .nodeName("null")
                .programVersion("null")
                .stakingAddr("null")
                .stakingBlockNum(BigInteger.ONE)
                .stakingHes(BigDecimal.TEN)
                .stakingTxIndex(3)
                .txHash("null")
                .webSite("null")
                .build();

        target.setBenefitAddr(null)
            .setBigVersion(null)
            .setDetails(null)
            .setExternalId(null)
            .setJoinTime(null)
            .setNodeId(null)
            .setNodeName(null)
            .setProgramVersion(null)
            .setStakingAddr(null)
            .setStakingBlockNum(null)
            .setStakingHes(null)
            .setStakingTxIndex(3)
            .setTxHash(null)
            .setWebSite(null)
            .setIsInit(2);

        target.getBenefitAddr();
        target.getBigVersion();
        target.getDetails();
        target.getExternalId();
        target.getJoinTime();
        target.getNodeId();
        target.getNodeName();
        target.getProgramVersion();
        target.getStakingAddr();
        target.getStakingBlockNum();
        target.getStakingHes();
        target.getStakingTxIndex();
        target.getTxHash();
        target.getWebSite();
        target.getIsInit();
        target.getBusinessType();
        assertTrue(true);
    }
}
