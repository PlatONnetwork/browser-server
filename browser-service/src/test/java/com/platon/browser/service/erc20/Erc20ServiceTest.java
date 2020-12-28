package com.platon.browser.service.erc20;

import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.platon.browser.client.PlatOnClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-29 16:42
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class Erc20ServiceTest {
    @Mock
    public Erc20RetryService erc20RetryService;
    @Mock
    public PlatOnClient platOnClient;
    @InjectMocks
    @Spy
    public Erc20ServiceImpl target;

    @Before
    public void setup() throws Exception {
        Erc20Contract erc20Contract = mock(Erc20Contract.class);
        doReturn(erc20Contract).when(target).init(any());

        RemoteCall remoteCall0 = mock(RemoteCall.class);
        when(erc20Contract.name()).thenReturn(remoteCall0);
        when(remoteCall0.send()).thenReturn("contract-name");

        RemoteCall remoteCall1 = mock(RemoteCall.class);
        when(erc20Contract.symbol()).thenReturn(remoteCall1);
        when(remoteCall1.send()).thenReturn("symbol-name");

        RemoteCall remoteCall2 = mock(RemoteCall.class);
        when(erc20Contract.decimals()).thenReturn(remoteCall2);
        when(remoteCall2.send()).thenReturn(BigInteger.TEN);

        RemoteCall remoteCall3 = mock(RemoteCall.class);
        when(erc20Contract.totalSupply()).thenReturn(remoteCall3);
        when(remoteCall3.send()).thenReturn(BigInteger.TEN);

        List<Erc20Contract.TransferEventResponse> transferEventResponses = new ArrayList<>();
        Erc20Contract.TransferEventResponse transferEventResponse = new Erc20Contract.TransferEventResponse();
        transferEventResponse.from = "";
        transferEventResponse.log = new Log();
        transferEventResponse.to = "";
        transferEventResponse.value = BigInteger.TEN;
        transferEventResponses.add(transferEventResponse);
        when(erc20Contract.getTransferEvents(any())).thenReturn(transferEventResponses);
        when(erc20Contract.getTransferEvents(any())).thenReturn(transferEventResponses);


        List<Erc20Contract.ApprovalEventResponse> approvalEventResponses = new ArrayList<>();
        Erc20Contract.ApprovalEventResponse approvalEventResponse = new Erc20Contract.ApprovalEventResponse();
        approvalEventResponse.log = new Log();
        approvalEventResponse.value = BigInteger.TEN;
        approvalEventResponse.owner = "";
        approvalEventResponse.spender = "";
        approvalEventResponses.add(approvalEventResponse);
        when(erc20Contract.getApprovalEvents(any())).thenReturn(approvalEventResponses);
    }


    @Test
    public void test_getBalance() throws Exception {
        BigInteger balance = this.target.getBalance("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj", "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        Assert.assertEquals(balance.longValue(), BigInteger.ZERO.longValue());
    }

    @Test
    public void test_getErcData() throws Exception {
        ERCData ercData = this.target.getErcData("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        Assert.assertNotNull(ercData);
    }

    @Test
    public void test_getTransferEvents() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setContractAddress("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        List<TransferEvent> transferEvents = this.target.getTransferEvents(transactionReceipt);
        Assert.assertNotNull(transferEvents);
    }

    @Test
    public void test_getApprovalEvents() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setContractAddress("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        List<TransferEvent> transferEvents = this.target.getApprovalEvents(transactionReceipt);
        Assert.assertTrue(true);
    }
}
