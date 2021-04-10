package com.platon.browser.utils;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class RoundCalculationTest {

    private static String bcString = "{\n" +
            "\"expectBlockCount\": \"10\",\n" +
            "\"consensusValidatorCount\": \"4\",\n" +
            "\"additionalCycleMinutes\": \"28\",\n" +
            "\"settlePeriodCountPerIssue\": \"10\",\n" +
            "\"blockInterval\": \"1\",\n" +
            "\"consensusPeriodBlockCount\": \"40\",\n" +
            "\"settlePeriodBlockCount\": \"160\",\n" +
            "\"addIssuePeriodBlockCount\": \"1600\",\n" +
            "\"platOnFundAccount\":\"0x493301712671ada506ba6ca7891f436d29185821\",\n" +
            "\"platOnFundInitAmount\": \"0\",\n" +
            "\"communityFundAccount\":\"0xc27a3d7e7e729c9aaefd17500168dcc9b43ddf02\",\n" +
            "\"communityFundInitAmount\": \"331811981000000000000000000\",\n" +
            "\"stakeThreshold\": \"1000000\",\n" +
            "\"delegateThreshold\": \"10\",\n" +
            "\"unStakeRefundSettlePeriodCount\": \"5\",\n" +
            "\"duplicateSignSlashRate\": \"0.0100000000000000\",\n" +
            "\"duplicateSignRewardRate\": \"0.50\",\n" +
            "\"evidenceValidEpoch\": \"4\",\n" +
            "\"minProposalTextParticipationRate\": \"0.5\",\n" +
            "\"minProposalTextSupportRate\": \"0.667\",\n" +
            "\"minProposalCancelParticipationRate\": \"0.5\",\n" +
            "\"minProposalCancelSupportRate\": \"0.667\",\n" +
            "\"minProposalUpgradePassRate\": \"0.667\",\n" +
            "\"proposalTextConsensusRounds\": \"4\",\n" +
            "\"versionProposalActiveConsensusRounds\": \"4\",\n" +
            "\"blockRewardRate\": \"0.50\",\n" +
            "\"stakeRewardRate\": \"0.50\",\n" +
            "\"platOnFoundationYear\": \"10\",\n" +
            "\"maxSettlePeriodCount4AnnualizedRateStat\":\"4\",\n" +
            "\"initIssueAmount\": \"10000000000\",\n" +
            "\"addIssueRate\": \"0.025\",\n" +
            "\"incentiveRateFromIssue\": \"0.8\",\n" +
            "\"electionBackwardBlockCount\": \"20\",\n" +
            "\"foundationSubsidies\":\n" +
            "  {\n" +
            "  \"0\":62215742,\n" +
            "  \"1\":55965742,\n" +
            "  \"2\":49559492,\n" +
            "  \"3\":42993086,\n" +
            "  \"4\":36262520,\n" +
            "  \"5\":29363689,\n" +
            "  \"6\":22292388,\n" +
            "  \"7\":15044304,\n" +
            "  \"8\":7615018\n" +
            "  },\n" +
            "\"proposalUrlTemplate\":\"https://github.com/danielgogo/PIPs/blob/master/PIP-%s.md\",\n" +
            "\"proposalPipNumTemplate\":\"PIP-%s\",\n" +
            "\"keyBase\":\"https://keybase.io/\",\n" +
            "\"keyBaseApi\":\"_/api/1.0/user/autocomplete.json?q=\",\n" +
            "\"defaultStakingLockedAmount\": \"1500000\",\n" +
            "\"defaultStakingList\":[]\n" +
            "}";

    @Test
    public void test(){
        BlockChainConfig bcc = JSON.parseObject(bcString, BlockChainConfig.class);
        RoundCalculation.activeBlockNumCal(BigDecimal.TEN,bcc);

        RoundCalculation.endBlockNumCal("888",BigDecimal.TEN,bcc);

        RoundCalculation.getParameterProposalVoteEndBlockNum(980L,bcc);

        assertTrue(true);
    }
}
