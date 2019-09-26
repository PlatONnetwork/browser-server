package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.task.bean.TransactionBean;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 09:32
 * @Description: 交易服务单元测试
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionServiceTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(TransactionServiceTest.class);
    private ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    @Mock
    private PlatOnClient client;
    @Mock
    private TransactionService target;
    @Mock
    private BlockChainConfig chainConfig;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() throws IOException, BeanCreateOrUpdateException, InterruptedException {
        ReflectionTestUtils.setField(target, "executor", THREAD_POOL);
        ReflectionTestUtils.setField(target, "client", client);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        when(target.analyze(anyList())).thenCallRealMethod();
        when(target.updateTransaction(any(CustomTransaction.class))).thenCallRealMethod();
        when(target.getReceipt(any(TransactionBean.class))).thenAnswer((Answer<Optional<TransactionReceipt>>) invocation->{
            TransactionBean tx = invocation.getArgument(0);
            TransactionReceipt receipt = new TransactionReceipt();
            BeanUtils.copyProperties(tx,receipt);
            receipt.setBlockNumber(tx.getBlockNumber().toString());
            receipt.setTransactionHash(tx.getHash());
            receipt.setTransactionIndex(tx.getTransactionIndex().toString());
            Optional<TransactionReceipt> optional = Optional.ofNullable(receipt);
            return optional;
        });

    }

    /**
     * 执行交易分析测试
     */
    @Test
    public void testAnalyze() throws InterruptedException {
        // 交易信息置空
        blocks.forEach(block->block.getTransactionList().forEach(tx->{
            tx.setTxType(null);
            tx.setTxInfo(null);
        }));
        List<CustomBlock> result = target.analyze(blocks);
        // 数量相等
        assertEquals(blocks.size(),result.size());
        // 验证交易信息被解析出来
        boolean flag = true;
        for (CustomBlock block:result){
            for (CustomTransaction tx:block.getTransactionList()){
                if (tx.getTxType() == null || tx.getTxInfo() == null) {
                    flag = false;
                    break;
                }
            }
        }
        assertTrue(flag);
    }
}
