package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.exception.BlockCollectingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 09:32
 * @Description: 区块服务单元测试
 */
@RunWith(MockitoJUnitRunner.class)
public class BlockServiceTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(BlockServiceTest.class);
    private ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    @Mock
    private PlatonClient client;
    @Mock
    private BlockService blockService;
    @Mock
    private Web3j web3j;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BlockCollectingException
     */
    @Before
    public void setup() throws IOException, BlockCollectingException {
        ReflectionTestUtils.setField(blockService, "executor", THREAD_POOL);
        ReflectionTestUtils.setField(blockService, "client", client);
        when(client.getWeb3j()).thenReturn(web3j);
        when(blockService.collect(anySet())).thenCallRealMethod();
        when(blockService.getBlock(any(Web3j.class),any(BigInteger.class))).thenAnswer((Answer<CustomBlock>) invocation->{
            BigInteger blockNumber = invocation.getArgument(1);
            CustomBlock block = new CustomBlock();
            block.setNumber(blockNumber.longValue());
            return block;
        });
    }

    /**
     * 执行区块搜集测试
     * @throws BlockCollectingException
     */
    @Test
    public void testCollect() throws BlockCollectingException {
        Set<BigInteger> blockNumbers = new HashSet<>();
        for (int i=0;i<20;i++) blockNumbers.add(BigInteger.valueOf(i));
        List<CustomBlock> blocks = blockService.collect(blockNumbers);
        // 数量相等
        assertEquals(blockNumbers.size(),blocks.size());
        Set<BigInteger> resultNumbers = new HashSet<>();
        blocks.forEach(block -> resultNumbers.add(block.getBlockNumber()));
        // 区块号相等
        assertTrue(blockNumbers.containsAll(resultNumbers));
    }
}
