package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 09:32
 * @Description: 交易服务单元测试
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(TransactionServiceTest.class);
    private ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    @Mock
    private PlatonClient client;
    @Mock
    private TransactionService transactionService;

    @Before
    public void setUp() throws IOException, BeanCreateOrUpdateException {
        ReflectionTestUtils.setField(transactionService, "executor", THREAD_POOL);
        ReflectionTestUtils.setField(transactionService, "client", client);
        when(transactionService.analyze(Mockito.anyList())).thenCallRealMethod();
        when(transactionService.updateTransaction(Mockito.any(CustomTransaction.class))).thenCallRealMethod();
        when(transactionService.getReceipt(Mockito.any(CustomTransaction.class))).thenAnswer((Answer<Optional<TransactionReceipt>>) invocation->{
            CustomTransaction tx = invocation.getArgument(0,CustomTransaction.class);
            TransactionReceipt receipt = new TransactionReceipt();
            BeanUtils.copyProperties(tx,receipt);
            receipt.setBlockNumber(tx.getBlockNumber().toString());
            receipt.setTransactionHash(tx.getHash());
            receipt.setTransactionIndex(tx.getTransactionIndex().toString());
            receipt.setRoot("0xeda9c0ec817590b0741c4910506ec5605a681d3207f1510151113229e4868553");
            receipt.setCumulativeGasUsed("0x9f54");
            receipt.setGasUsed("0x9f54");
            receipt.setLogsBloom("0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100080000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000");
            Optional<TransactionReceipt> optional = Optional.ofNullable(receipt);

            //ReflectionTestUtils.setField(optional, "value", new Object());
            return optional;
        });

    }

    @Test
    public void testAnalyze() {
        List<CustomBlock> blocks = transactionService.analyze(this.blocks);
        assertEquals(this.blocks.size(),blocks.size());
    }

}
