package com.platon.browser.config;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.ConfigLoadingException;
import com.platon.protocol.core.methods.response.bean.EconomicConfig;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockChainConfigTest {

    @Mock
    private PlatOnClient client;
    @Spy
    private BlockChainConfig target;

    private String ecString = "{\n" +
            "\t\"common\":{\n" +
            "\t\t\"additionalCycleTime\":28,\n" +
            "\t\t\"maxConsensusVals\":4,\n" +
            "\t\t\"maxEpochMinutes\":3,\n" +
            "\t\t\"nodeBlockTimeWindow\":10,\n" +
            "\t\t\"perRoundBlocks\":10\n" +
            "\t},\n" +
            "\t\"gov\":{\n" +
            "\t\t\"cancelProposalSupportRate\":0.667,\n" +
            "\t\t\"cancelProposalVoteRate\":0.5,\n" +
            "\t\t\"paramProposalSupportRate\":0.667,\n" +
            "\t\t\"paramProposalVoteDurationSeconds\":160,\n" +
            "\t\t\"paramProposalVoteRate\":0.5,\n" +
            "\t\t\"textProposalSupportRate\":0.667,\n" +
            "\t\t\"textProposalVoteDurationSeconds\":160,\n" +
            "\t\t\"textProposalVoteRate\":0.5,\n" +
            "\t\t\"versionProposalSupportRate\":0.1,\n" +
            "\t\t\"versionProposalVoteDurationSeconds\":1600\n" +
            "\t},\n" +
            "\t\"innerAcc\":{\n" +
            "\t\t\"cdfAccount\":\"0xc27a3d7e7e729c9aaefd17500168dcc9b43ddf02\",\n" +
            "\t\t\"cdfBalance\":331811981000000000000000000,\n" +
            "\t\t\"platonFundAccount\":\"0x493301712671ada506ba6ca7891f436d29185821\",\n" +
            "\t\t\"platonFundBalance\":0\n" +
            "\t},\n" +
            "\t\"reward\":{\n" +
            "\t\t\"newBlockRate\":50,\n" +
            "\t\t\"platonFoundationYear\":10\n" +
            "\t},\n" +
            "\t\"slashing\":{\n" +
            "\t\t\"duplicateSignReportReward\":50,\n" +
            "\t\t\"maxEvidenceAge\":1,\n" +
            "\t\t\"slashBlocksReward\":20,\n" +
            "\t\t\"slashFractionDuplicateSign\":100,\n" +
            "\t\t\"zeroProduceNumberThreshold\":20,\n" +
            "\t\t\"zeroProduceCumulativeTime\":100,\n" +
            "\t\t\"zeroProduceFreezeDuration\":100\n" +
            "\t},\n" +
            "\t\"staking\":{\n" +
            "\t\t\"hesitateRatio\":1,\n" +
            "\t\t\"maxValidators\":30,\n" +
            "\t\t\"operatingThreshold\":10000000000000000000,\n" +
            "\t\t\"stakeThreshold\":1000000000000000000000000,\n" +
            "\t\t\"unStakeFreezeDuration\":5,\n" +
            "\t\t\"rewardPerMaxChangeRange\":10,\n" +
            "\t\t\"rewardPerChangeInterval\":4\n" +
            "\t}\n" +
            "}\n";

    //@Test
    public void test() throws InvocationTargetException, IllegalAccessException, ConfigLoadingException {
        //ReflectionTestUtils.setField(target,"client",client);

        Set<String> set = new HashSet<>(InnerContractAddrEnum.getAddresses());
        target.getInnerContractAddr();
        for(Method method:BlockChainConfig.class.getDeclaredMethods()){
            if(method.getName().contains("get")){
                method.invoke(target);
            }
        }

        target.setDefaultStakingLockedAmount(BigDecimal.TEN);

        EconomicConfig ec = JSON.parseObject(ecString,EconomicConfig.class);
        when(client.getEconomicConfig()).thenReturn(ec);
        target.init();

        target.getInnerContractAddr();

        assertTrue(true);
    }
}
