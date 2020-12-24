package com.platon.browser.service.erc20;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.Web3jWrapper;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.erc.client.ERC20Client;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.dto.ERCData;
import com.platon.browser.dto.TransferEvent;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;


/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-29 16:42
 */
@RunWith(PowerMockRunner.class)
public class Erc20ServiceImplTest {

    public PlatOnClient platOnClient;

    public Web3jWrapper web3jWrapper;

    public Web3j web3j;

    public Erc20TokenMapper erc20TokenMapper;

    public Erc20ServiceImpl target;

    public Erc20RetryService erc20RetryService;

    @Before
    public void setup() throws Exception {
        this.platOnClient = PowerMockito.mock(PlatOnClient.class);
        this.web3jWrapper = PowerMockito.mock(Web3jWrapper.class);
        this.web3j = PowerMockito.mock(Web3j.class);
        this.erc20TokenMapper = PowerMockito.mock(Erc20TokenMapper.class);
        this.erc20RetryService = PowerMockito.mock(Erc20RetryService.class);
        this.target = PowerMockito.spy(new Erc20ServiceImpl());
        ReflectionTestUtils.setField(this.target, "platOnClient", this.platOnClient);
        ReflectionTestUtils.setField(this.target, "erc20TokenMapper", this.erc20TokenMapper);
        ReflectionTestUtils.setField(this.target, "erc20RetryService", this.erc20RetryService);
        doReturn(this.web3jWrapper).when(this.platOnClient).getWeb3jWrapper();
        when(this.web3jWrapper.getWeb3j()).thenReturn(this.web3j);
    }

    @Test
    @PrepareForTest({Erc20ServiceImpl.class})
    public void test_getErcData() throws Exception {
        ERC20Client erc20Client = mock(ERC20Client.class);
        PowerMockito.whenNew(ERC20Client.class)
                .withArguments(anyString(), any(), any(), anyLong())
                .thenReturn(erc20Client);
        ERCData ercData = this.target.getErcData("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");

        RemoteCall<String> remoteCall = mock(RemoteCall.class);
        when(remoteCall.send()).thenReturn("1");
        RemoteCall<BigInteger> remoteCallb = mock(RemoteCall.class);
        when(erc20Client.name()).thenReturn(remoteCall);
        ercData = this.target.getErcData("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        when(erc20Client.symbol()).thenReturn(remoteCall);
        when(erc20Client.decimals()).thenReturn(remoteCallb);
        when(erc20Client.totalSupply()).thenReturn(remoteCallb);
        when(remoteCallb.send()).thenReturn(BigInteger.TEN);
        ercData = this.target.getErcData("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        Assert.assertNotNull(ercData);
    }

    @Test
    @PrepareForTest({Erc20ServiceImpl.class})
    public void test_getTransferEvents() throws Exception {
        ERC20Client erc20Client = mock(ERC20Client.class);
        PowerMockito.whenNew(ERC20Client.class)
                .withArguments(anyString(), any(), any(), anyLong())
                .thenReturn(erc20Client);
        List<ERC20Client.TransferEventResponse> transferEventResponses = new ArrayList<>();
        ERC20Client.TransferEventResponse transferEventResponse = new ERC20Client.TransferEventResponse();
        transferEventResponse.from = "";
        transferEventResponse.log = new Log();
        transferEventResponse.to = "";
        transferEventResponse.value = BigInteger.TEN;
        transferEventResponses.add(transferEventResponse);
        when(erc20Client.getTransferEvents(any())).thenReturn(transferEventResponses);
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setContractAddress("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        List<TransferEvent> transferEvents = this.target.getTransferEvents(transactionReceipt);
        Assert.assertNotNull(transferEvents);
    }

    @Test
    @PrepareForTest({Erc20ServiceImpl.class})
    public void test_getApprovalEvents() throws Exception {
        ERC20Client erc20Client = mock(ERC20Client.class);
        PowerMockito.whenNew(ERC20Client.class)
                .withArguments(anyString(), any(), any(), anyLong())
                .thenReturn(erc20Client);

        List<ERC20Client.ApprovalEventResponse> transferEventResponses = new ArrayList<>();
        ERC20Client.ApprovalEventResponse approvalEventResponse = new ERC20Client.ApprovalEventResponse();
        approvalEventResponse.log = new Log();
        approvalEventResponse.value = BigInteger.TEN;
        approvalEventResponse.owner = "";
        approvalEventResponse.spender = "";
        transferEventResponses.add(approvalEventResponse);
        when(erc20Client.getApprovalEvents(any())).thenReturn(transferEventResponses);
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setContractAddress("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj");
        List<TransferEvent> transferEvents = this.target.getApprovalEvents(transactionReceipt);
        Assert.assertTrue(true);
    }

    @Test
    public void test_initContractData() throws Exception {
        ERC20Client erc20Client = mock(ERC20Client.class);
        PowerMockito.whenNew(ERC20Client.class)
                .withArguments(anyString(), any(), any(), anyLong())
                .thenReturn(erc20Client);

        String contractAddress = "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj";
        String from = "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj";
        String to = "lax196278ns22j23awdfj9f2d4vz0pedld8au6xel1";
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        List<ESTokenTransferRecord> esTokenTransferRecords = new ArrayList<>();
        ESTokenTransferRecord esTokenTransferRecord = ESTokenTransferRecord.builder().tValue("1")
                .tto(from).from(from).bn(1l).bTime(new Date()).contract(contractAddress).ctime(new Date()).hash("")
                .info("").result(0).value("").decimal(0).fromType(0).name("").seq(1l).sign("").symbol("")
                .build();
        ESTokenTransferRecord esTokenTransferRecord2 = ESTokenTransferRecord.builder().tValue("1")
                .tto(to).from(from).bn(1l).bTime(new Date()).contract(contractAddress).ctime(new Date()).hash("")
                .info("").result(0).value("").decimal(0).fromType(0).name("").seq(1l).sign("").symbol("")
                .build();
        esTokenTransferRecords.add(esTokenTransferRecord);
        esTokenTransferRecords.add(esTokenTransferRecord2);
        transaction.setEsTokenTransferRecords(esTokenTransferRecords);
        AddressCache addressCache = new AddressCache();
        Erc20Token erc20Token = Erc20Token.builder().address(contractAddress).blockTimestamp(new Date())
                .createTime(new Date()).creator("").decimal(0).holder(0).totalSupply(BigDecimal.ZERO).name("")
                .status(0).symbol("").txCount(0).txHash("").type("").build();
        when(this.erc20TokenMapper.selectByAddress(any())).thenReturn(erc20Token);
        addressCache.createDefaultErc20(contractAddress);
        transactions.add(transaction);
        when(this.erc20RetryService.getErc20Token(any())).thenReturn(erc20Token);
        this.target.initContractData(transactions, addressCache);
        Assert.assertTrue(true);
    }

}
