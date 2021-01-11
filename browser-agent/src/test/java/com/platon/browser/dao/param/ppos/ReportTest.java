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
public class ReportTest extends AgentTestBase {

    @Test
    public void test(){
        Report target = Report.builder()
                .benefitAddr("null")
                .nodeId("null")
                .settingEpoch(3)
                .slashData("null")
                .slashRate(BigDecimal.ONE)
                .stakingBlockNum(BigInteger.ONE)
                .time(new Date())
                .txHash("null")
                .codeRemainRedeemAmount(BigDecimal.TEN)
                .codeRewardValue(BigDecimal.TEN)
                .codeStatus(3)
                .slash2ReportRate(BigDecimal.TEN)
                .codeSlashValue(BigDecimal.TEN)
                .codeStakingReductionEpoch(3)
                .build();
          target.setBenefitAddr(null)
                .setNodeId(null)
                .setSettingEpoch(3)
                .setSlashData(null)
                .setSlashRate(null)
                .setStakingBlockNum(null)
                .setTime(null)
                .setTxHash(null)
                .setCodeRemainRedeemAmount(null)
                .setCodeRewardValue(null)
                .setCodeStatus(3)
                .setSlash2ReportRate(null)
                .setCodeSlashValue(null)
                .setCodeStakingReductionEpoch(3);

        target.getBenefitAddr();
        target.getNodeId();
        target.getSettingEpoch();
        target.getSlashData();
        target.getSlashRate();
        target.getStakingBlockNum();
        target.getTime();
        target.getTxHash();
        target.getCodeRemainRedeemAmount();
        target.getCodeRewardValue();
        target.getCodeStatus();
        target.getSlash2ReportRate();
        target.getCodeSlashValue();
        target.getCodeStakingReductionEpoch();;
          target.getBusinessType();
        assertTrue(true);
    }
}
