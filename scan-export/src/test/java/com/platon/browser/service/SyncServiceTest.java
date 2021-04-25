//package com.platon.browser.service;
//
//import com.platon.browser.elasticsearch.bean.ESResult;
//import com.platon.browser.elasticsearch.BlockESRepository;
//import com.platon.browser.elasticsearch.TransactionESRepository;
//import com.platon.browser.elasticsearch.dto.Block;
//import com.platon.browser.elasticsearch.dto.Transaction;
//import com.platon.browser.elasticsearch.query.ESQueryBuilderConstructor;
//import com.platon.browser.service.redis.RedisBlockService;
//import com.platon.browser.service.redis.RedisTransactionService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class SyncServiceTest {
//    @Mock
//    private BlockESRepository blockESRepository;
//    @Mock
//    private TransactionESRepository transactionESRepository;
//    @Mock
//    private RedisBlockService redisBlockService;
//    @Mock
//    private RedisTransactionService redisTransactionService;
//
//    @Spy
//    private ExportService target;
//
//    @Before
//    public void setup() throws IOException {
//        ReflectionTestUtils.setField(target, "blockESRepository", blockESRepository);
//        ReflectionTestUtils.setField(target, "transactionESRepository", transactionESRepository);
//        ReflectionTestUtils.setField(target, "redisBlockService", redisBlockService);
//        ReflectionTestUtils.setField(target, "redisTransactionService", redisTransactionService);
//        ReflectionTestUtils.setField(target, "blockPageCount", 20);
//        ReflectionTestUtils.setField(target, "blockPageSize", 20);
//        ReflectionTestUtils.setField(target, "transactionPageCount", 20);
//        ReflectionTestUtils.setField(target, "transactionPageSize", 20);
//        ESResult blockRes = mock(ESResult.class);
//        List<Block> blocks = new ArrayList<>();
//        Block block = new Block();
//        blocks.add(block);
//        when(blockRes.getRsData()).thenReturn(blocks);
//        when(blockESRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(blockRes);
//        ESResult transactionRes = mock(ESResult.class);
//        List<Transaction> transactions = new ArrayList<>();
//        Transaction transaction = new Transaction();
//        transactions.add(transaction);
//        when(transactionRes.getRsData()).thenReturn(transactions);
//        when(transactionESRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(transactionRes);
//    }
//
//    @Test(expected = Exception.class)
//    public void test() throws IOException {
//        target.syncBlock();
//        verify(target, times(1)).syncBlock();
//        target.syncTransaction();
//        verify(target, times(1)).syncTransaction();
//
//        doThrow(new RuntimeException("all shards failed")).when(blockESRepository).search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt());
//        doThrow(new RuntimeException("all shards failed")).when(transactionESRepository).search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt());
//        target.syncBlock();
//        target.syncTransaction();
//
//        doThrow(new RuntimeException("aaaa")).when(blockESRepository).search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt());
//        doThrow(new RuntimeException("aaa")).when(transactionESRepository).search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt());
//        target.syncBlock();
//        target.syncTransaction();
//
//        ReflectionTestUtils.setField(target, "blockPageCount", 1);
//        ReflectionTestUtils.setField(target, "blockPageSize", 1);
//        ReflectionTestUtils.setField(target, "transactionPageCount", 1);
//        ReflectionTestUtils.setField(target, "transactionPageSize", 1);
//        target.syncBlock();
//        target.syncTransaction();
//
//
//        when(blockESRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(null);
//        when(transactionESRepository.search(any(ESQueryBuilderConstructor.class),any(),anyInt(),anyInt())).thenReturn(null);
//        target.syncBlock();
//        target.syncTransaction();
//
//        doThrow(new Exception("")).when(redisTransactionService).save(anySet(),anyBoolean());
//        target.syncTransaction();
//
//    }
//
//    @Test(expected = Exception.class)
//    public void test2() throws IOException {
//        doThrow(new Exception("")).when(redisBlockService).save(anySet(),anyBoolean());
//        target.syncBlock();
//        ExportService.isTransactionSyncDone();
//        ExportService.isBlockSyncDone();
//    }
//}
