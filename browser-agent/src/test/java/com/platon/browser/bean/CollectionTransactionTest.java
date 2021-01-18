//package com.platon.browser.bean;
//
//import com.alaya.protocol.Web3j;
//import com.alaya.protocol.core.Request;
//import com.alaya.protocol.core.methods.response.PlatonGetCode;
//import com.alaya.protocol.core.methods.response.Transaction;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.cache.AddressCache;
//import com.platon.browser.client.PlatOnClient;
//import com.platon.browser.client.SpecialApi;
//import com.platon.browser.client.Web3jWrapper;
//import com.platon.browser.elasticsearch.dto.Block;
//import com.platon.browser.exception.BeanCreateOrUpdateException;
//import com.platon.browser.exception.BlankResponseException;
//import com.platon.browser.exception.ContractInvokeException;
//import com.platon.browser.service.erc20.Erc20ResolveServiceImpl;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.BeanUtils;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * @Auther: dongqile
// * @Date: 2019/12/5 15:09
// * @Description: 年化率信息bean单元测试
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class CollectionTransactionTest extends AgentTestBase {
//
//    @Mock
//    protected PlatOnClient client;
//    @Mock
//    protected AddressCache addressCache;
//    @Mock
//    protected SpecialApi specialApi;
//    @Mock
//    protected Erc20ResolveServiceImpl erc20ResolveServiceImpl;
//
//    @Before
//    public void setup() throws IOException {
//        Web3jWrapper web3jWrapper = mock(Web3jWrapper.class);
//        when(client.getWeb3jWrapper()).thenReturn(web3jWrapper);
//        Web3j web3j = mock(Web3j.class);
//        when(web3jWrapper.getWeb3j()).thenReturn(web3j);
//        Request request = mock(Request.class);
//        when(web3j.platonGetCode(any(),any())).thenReturn(request);
//        PlatonGetCode platonGetCode = mock(PlatonGetCode.class);
//        when(request.send()).thenReturn(platonGetCode);
//        when(platonGetCode.getCode()).thenReturn("code");
//    }
//
//    @Test
//    public void test() throws InvocationTargetException, IllegalAccessException, BeanCreateOrUpdateException, IOException, ContractInvokeException, BlankResponseException {
//        CollectionTransaction transaction = CollectionTransaction.newInstance();
//
//        Transaction transaction1 = new Transaction();
//        transaction1.setBlockNumber("0x0");
//        transaction1.setValue("0x0");
//        transaction1.setTransactionIndex("0x1");
//        transaction1.setGasPrice("0x0");
//        transaction1.setGas("0x0");
//        transaction1.setNonce("0x0");
//        transaction.updateWithRawTransaction(transaction1);
//
//        Block block = blockList.get(0);
//        CollectionBlock collectionBlock = CollectionBlock.newInstance();
//
//        BeanUtils.copyProperties(block,collectionBlock);
//
//        transaction.setNum(block.getNum());
//        transaction.setTo(transactionList.get(0).getTo());
//        transaction.setBHash(block.getHash());
//        transaction.updateWithBlock(collectionBlock);
//
//
//        ReceiptResult receipt = receiptResultList.get(0);
//        Receipt receipt1 = receipt.getResult().get(0);
//
//        assertTrue(true);
//    }
//}
