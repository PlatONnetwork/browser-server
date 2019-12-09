package com.platon.browser.common.service.proposal;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.sdk.contracts.ppos.ProposalContract;
import com.platon.sdk.contracts.ppos.dto.CallResponse;
import com.platon.sdk.contracts.ppos.dto.resp.TallyResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalServiceTest extends AgentTestBase {

    @Mock
    private PlatOnClient client;
    @Mock
    private SpecialApi sca;

    @Spy
    private ProposalService target;

    @Mock
    private CallResponse baseResponse;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "client", client);
        ReflectionTestUtils.setField(target, "sca", sca);
        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
        when(client.getWeb3jWrapper()).thenReturn(web3jWrapper);
        Web3j web3j = mock(Web3j.class);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
        when(sca.getProposalParticipants(any(),any(),any())).thenReturn(null);
        ProposalContract contract = mock(ProposalContract.class);
        when(client.getProposalContract()).thenReturn(contract);
        RemoteCall<CallResponse<TallyResult>> rcall = mock(RemoteCall.class);
        when(contract.getTallyResult(any())).thenReturn(rcall);
        when(rcall.send()).thenReturn(baseResponse);
        when(baseResponse.isStatusOk()).thenReturn(true);
    }

    /**
     * 测试更新
     */
    @Test(expected = ContractInvokeException.class)
    public void test() throws Exception {
        target.getProposalParticipantStat("0x3522","0xfsfsf");
        target.getTallyResult("0xerere");

        when(baseResponse.isStatusOk()).thenReturn(false);

        target.getTallyResult("0x3354");
    }

}
