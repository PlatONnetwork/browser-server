//package com.platon.browser.service.erc20;
//
//import com.alaya.protocol.Web3j;
//import com.platon.browser.cache.AddressCache;
//import com.platon.browser.client.PlatOnClient;
//import com.platon.browser.client.Web3jWrapper;
//import com.platon.browser.dao.entity.Erc20Token;
//import com.platon.browser.elasticsearch.dto.OldErcTx;
//import com.platon.browser.elasticsearch.dto.Transaction;
//import com.platon.browser.v0151.contract.Erc20Contract;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.powermock.api.mockito.PowerMockito;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.doReturn;
//
//
///**
// * @program: browser-server
// * @description:
// * @author: Rongjin Zhang
// * @create: 2020-09-29 16:42
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class Erc20ResolveServiceImplTest {
//    @Mock
//    public PlatOnClient platOnClient;
//    @Mock
//    public Web3jWrapper web3jWrapper;
//    @Mock
//    public Web3j web3j;
//    @Mock
//    public Erc20RetryService erc20RetryService;
//    @InjectMocks
//    @Spy
//    public Erc20ResolveServiceImpl target;
//
//    @Before
//    public void setup() throws Exception {
//        doReturn(this.web3jWrapper).when(this.platOnClient).getWeb3jWrapper();
//        when(this.web3jWrapper.getWeb3j()).thenReturn(this.web3j);
//    }
//
//    @Test
//    public void test_initContractData() throws Exception {
//        Erc20Contract erc20Contract = mock(Erc20Contract.class);
//        PowerMockito.whenNew(Erc20Contract.class)
//                .withArguments(anyString(), any(), any(), anyLong())
//                .thenReturn(erc20Contract);
//
//        String contractAddress = "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj";
//        String from = "lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj";
//        String to = "lax196278ns22j23awdfj9f2d4vz0pedld8au6xel1";
//        List<Transaction> transactions = new ArrayList<>();
//        Transaction transaction = new Transaction();
//        List<OldErcTx> oldErcTxes = new ArrayList<>();
//        OldErcTx oldErcTx = OldErcTx.builder().tValue("1")
//                .tto(from).from(from).bn(1l).bTime(new Date()).contract(contractAddress).ctime(new Date()).hash("")
//                .info("").result(0).value("").decimal(0).fromType(0).name("").seq(1l).sign("").symbol("")
//                .build();
//        OldErcTx oldErcTx2 = OldErcTx.builder().tValue("1")
//                .tto(to).from(from).bn(1l).bTime(new Date()).contract(contractAddress).ctime(new Date()).hash("")
//                .info("").result(0).value("").decimal(0).fromType(0).name("").seq(1l).sign("").symbol("")
//                .build();
//        oldErcTxes.add(oldErcTx);
//        oldErcTxes.add(oldErcTx2);
//        transaction.setOldErcTxes(oldErcTxes);
//        AddressCache addressCache = new AddressCache();
//        Erc20Token erc20Token = Erc20Token.builder().address(contractAddress).blockTimestamp(new Date())
//                .createTime(new Date()).creator("").decimal(0).holder(0).totalSupply(BigDecimal.ZERO).name("")
//                .status(0).symbol("").txCount(0).txHash("").type("").build();
//
//        addressCache.createDefaultErc20(contractAddress);
//        transactions.add(transaction);
//        when(this.erc20RetryService.getErc20Token(any())).thenReturn(erc20Token);
//        this.target.initContractAddressCache(transactions);
//        Assert.assertTrue(true);
//    }
//
//}
