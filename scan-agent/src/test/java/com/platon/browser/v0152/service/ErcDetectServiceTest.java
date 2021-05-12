package com.platon.browser.v0152.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.v0152.bean.ErcContractId;
import com.platon.browser.v0152.contract.ErcContract;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.Response;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.tx.exceptions.PlatonCallTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class ErcDetectServiceTest extends AgentTestBase {

    @Mock
    private PlatOnClient platOnClient;

    @Mock
    private Web3jWrapper web3jWrapper;

    @Mock
    private Web3j web3j;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @Mock
    private ErcContract ercContract;

    @Mock
    private RemoteCall remoteCall;

    @InjectMocks
    @Spy
    private ErcDetectService ercDetectService;

    private String contractAddress = "atp1pcqqfnl2xwyts8xlvx7n5dlxy3z634u6sldhep";

    @Test
    public void detectInputData() throws IOException {
        when(platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
        when(platOnClient.getWeb3jWrapper().getWeb3j().platonCall(any(Transaction.class), any(DefaultBlockParameterName.class))).thenReturn(request);
        // 测试超时异常
        when(request.send()).thenThrow(new PlatonCallTimeoutException(1, "超时", response));
        // 测试业务异常
        //when(request.send()).thenThrow(new IOException());
         ercDetectService.isSupportErc721Enumerable(contractAddress);
    }

    @Test
    public void getContractId() throws Exception {
        when(platOnClient.getWeb3jWrapper()).thenReturn(web3jWrapper);
        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
        when(platOnClient.getWeb3jWrapper().getWeb3j().platonCall(any(Transaction.class), any(DefaultBlockParameterName.class))).thenReturn(request);
        // 测试超时异常
        //when(request.send()).thenThrow(new PlatonCallTimeoutException(1, "超时", response));
        // 测试业务异常
        //when(request.send()).thenThrow(new IOException());
        when(ercContract.name()).thenReturn(remoteCall);
        when(remoteCall.send()).thenThrow(new PlatonCallTimeoutException(1, "超时", response));
        ercDetectService.getContractId(contractAddress);
    }

}
