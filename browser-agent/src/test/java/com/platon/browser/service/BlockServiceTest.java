package com.platon.browser.service;

import com.platon.browser.bean.CollectResult;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.exception.BlockCollectingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 09:32
 * @Description: 区块服务单元测试
 */
@RunWith(MockitoJUnitRunner.class)
public class BlockServiceTest {
    private static Logger logger = LoggerFactory.getLogger(BlockServiceTest.class);
    @Mock
    private PlatonClient client;
    @InjectMocks
    private BlockService blockService;

    @Before
    public void setUp() throws IOException {
        Web3j web3j = mock(Web3j.class);
        when(client.getWeb3j()).thenReturn(web3j);
        Request<?,PlatonBlock> request = mock(Request.class);
        PlatonBlock platonBlock = mock(PlatonBlock.class);
        DefaultBlockParameter parameter = mock(DefaultBlockParameter.class);
        //when(web3j.platonGetBlockByNumber(parameter,true)).thenReturn(request);
        when(request.send()).thenReturn(platonBlock);

        when(platonBlock.getBlock()).thenAnswer((Answer<PlatonBlock.Block>) invocation -> {
            PlatonBlock.Block block = mock(PlatonBlock.Block.class);
            return block;
        });;
        ReflectionTestUtils.setField(blockService, "EXECUTOR", Executors.newFixedThreadPool(10));
    }

    @Test
    public void testCollect() throws IOException, BlockCollectingException {
        Set<BigInteger> blockNumbers = new HashSet<>();
        blockNumbers.add(BigInteger.valueOf(1));
        blockNumbers.add(BigInteger.valueOf(2));
        blockNumbers.add(BigInteger.valueOf(3));
        blockService.collect(blockNumbers);
        System.out.println(CollectResult.getSortedBlocks());
    }

}
