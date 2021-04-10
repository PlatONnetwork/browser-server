package com.platon.browser.service;

import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.redis.RedisTransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionSyncServiceTest {
    @Mock
    private EsTransactionRepository esRepository;
    @Mock
    private RedisTransactionService redisService;
    @Spy
    private TransactionSyncService target;

    @Before
    public void setup() throws IOException {
        ReflectionTestUtils.setField(target, "esRepository", esRepository);
        ReflectionTestUtils.setField(target, "redisService", redisService);
        ReflectionTestUtils.setField(target, "pageCount", 20);
        ReflectionTestUtils.setField(target, "pageSize", 20);
        ESResult blockRes = mock(ESResult.class);
        List<Block> blocks = new ArrayList<>();
        Block block = new Block();
        blocks.add(block);
        when(blockRes.getRsData()).thenReturn(blocks);
        when(esRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(blockRes);
        ESResult transactionRes = mock(ESResult.class);
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transactions.add(transaction);
        when(transactionRes.getRsData()).thenReturn(transactions);
        when(esRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(transactionRes);
    }

    @Test
    public void test() throws IOException {
        target.sync();
        verify(target, times(1)).sync();

        ReflectionTestUtils.setField(target, "pageCount", 1);
        ReflectionTestUtils.setField(target, "pageSize", 1);
        target.sync();

        when(esRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(null);
        target.sync();

        doThrow(new RuntimeException("all shards failed")).when(esRepository).search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt());
        target.sync();

        doThrow(new RuntimeException("aaaa")).when(esRepository).search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt());
        target.sync();
    }
}
