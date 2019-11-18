package com.platon.browser.common.service.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.common.exception.CandidateException;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.config.BlockChainConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochRetryServiceTest extends AgentTestBase {

    @Mock private BlockChainConfig chainConfig;
    @Mock private AccountService accountService;
    @Mock PlatOnClient platOnClient;
    @Mock
    SpecialApi specialApi;
    @Mock List<Node> curVerifiers;
    @Spy private EpochRetryService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "accountService", accountService);
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(target, "specialApi", specialApi);
        ReflectionTestUtils.setField(target, "curVerifiers", curVerifiers);
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.valueOf(250));
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(10));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(50));
        when(chainConfig.getBlockRewardRate()).thenReturn(BigDecimal.valueOf(0.6));
        when(chainConfig.getStakeRewardRate()).thenReturn(BigDecimal.valueOf(0.4));
        when(chainConfig.getSettlePeriodCountPerIssue()).thenReturn(BigInteger.valueOf(5));
        when(platOnClient.getLatestBlockNumber()).thenReturn(BigInteger.valueOf(501));
        when(accountService.getInciteBalance(any())).thenReturn(BigDecimal.valueOf(10000));

        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);

        when(specialApi.getHistoryValidatorList(any(),any())).thenReturn(validatorList);
        when(specialApi.getHistoryVerifierList(any(),any())).thenReturn(verifierList);

        when(curVerifiers.size()).thenReturn(4);
        when(platOnClient.getLatestValidators()).thenReturn(validatorList);
        when(platOnClient.getLatestVerifiers()).thenReturn(verifierList);
    }

    /**
     * 测试增发周期变更
     */
    @Test
    public void issueEpochChange() throws Exception {
        target.issueChange(BigInteger.valueOf(501));
        assertEquals(6000,target.getInciteAmount4Block().intValue());
        assertEquals(24,target.getBlockReward().intValue());
        assertEquals(4000,target.getInciteAmount4Stake().intValue());
        assertEquals(800,target.getSettleStakeReward().intValue());
        assertEquals(200,target.getStakeReward().intValue());
        verify(target, times(1)).issueChange(any(BigInteger.class));
    }

    /**
     * 测试共识周期变更
     */
    @Test
    public void consensusEpochChange() throws Exception {
        target.consensusChange(BigInteger.valueOf(41));
        verify(target, times(1)).consensusChange(any(BigInteger.class));
    }

    /**
     * 测试结算周期变更
     */
    @Test
    public void settlementEpochChange() throws Exception {
        target.settlementChange(BigInteger.valueOf(321));
        verify(target, times(1)).settlementChange(any(BigInteger.class));
    }

    /**
     * 测试取候选人列表
     */
    @Test
    public void getCandidatesNormal() throws Exception {
        NodeContract nodeContract = mock(NodeContract.class);
        when(platOnClient.getNodeContract()).thenReturn(nodeContract);
        RemoteCall call = mock(RemoteCall.class);
        when(nodeContract.getCandidateList()).thenReturn(call);
        BaseResponse response = mock(BaseResponse.class);
        response.data=candidateList;
        when(call.send()).thenReturn(response);

        when(response.isStatusOk()).thenReturn(true);
        target.getCandidates();

        verify(target, times(1)).getCandidates();
    }

    /**
     * 测试取候选人列表
     */
    @Test(expected = CandidateException.class)
    public void getCandidatesException() throws Exception {
        NodeContract nodeContract = mock(NodeContract.class);
        when(platOnClient.getNodeContract()).thenReturn(nodeContract);
        RemoteCall call = mock(RemoteCall.class);
        when(nodeContract.getCandidateList()).thenReturn(call);
        BaseResponse response = mock(BaseResponse.class);
        response.data=candidateList;
        when(call.send()).thenReturn(response);

        when(response.isStatusOk()).thenReturn(true);
        response.data=null;
        target.getCandidates();
        verify(target, times(1)).getCandidates();
    }
}
