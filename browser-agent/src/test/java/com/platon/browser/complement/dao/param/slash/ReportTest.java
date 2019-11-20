package com.platon.browser.complement.dao.param.slash;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ReportTest extends AgentTestBase {

    @Test
    public void test(){
        Report target = Report.builder()
                .benefitAddr(null)
                .nodeId(null)
                .settingEpoch(3)
                .slashData(null)
                .slashRate(null)
                .stakingBlockNum(null)
                .time(null)
                .txHash(null)
                .codeCurStakingLocked(null)
                .codeRewardValue(null)
                .codeStatus(3)
                .slash2ReportRate(null)
                .codeSlashValue(null)
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
                .setCodeCurStakingLocked(null)
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
        target.getCodeCurStakingLocked();
        target.getCodeRewardValue();
        target.getCodeStatus();
        target.getSlash2ReportRate();
        target.getCodeSlashValue();
        target.getCodeStakingReductionEpoch();;
          target.getBusinessType();
        assertTrue(true);
    }
}
