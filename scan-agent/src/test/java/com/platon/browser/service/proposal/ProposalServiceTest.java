package com.platon.browser.service.proposal;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.client.Web3jWrapper;
import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.TallyResult;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalServiceTest extends AgentTestBase {

    @Mock
    private PlatOnClient client;
    @Mock
    private SpecialApi sca;
    @InjectMocks
    @Spy
    private ProposalService target;

    @Mock
    private CallResponse baseResponse;

    @Before
    public void setup() throws Exception {
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
    @Test
    public void test() throws Exception {
        target.getProposalParticipantStat("0x3522","0xfsfsf");
        target.getTallyResult("0xerere");

        when(baseResponse.isStatusOk()).thenReturn(false);

        target.getTallyResult("0x3354");
    }

}
