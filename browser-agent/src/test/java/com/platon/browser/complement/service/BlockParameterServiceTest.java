package com.platon.browser.complement.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.epoch.OnConsensusConverter;
import com.platon.browser.complement.converter.epoch.OnElectionConverter;
import com.platon.browser.complement.converter.epoch.OnNewBlockConverter;
import com.platon.browser.complement.converter.epoch.OnSettleConverter;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockParameterServiceTest extends AgentTestBase {
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private OnNewBlockConverter onNewBlockConverter;
    @Mock
    private OnElectionConverter onElectionConverter;
    @Mock
    private OnConsensusConverter onConsensusConverter;
    @Mock
    private OnSettleConverter onSettleConverter;

    @Spy
    private BlockParameterService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "onNewBlockConverter", onNewBlockConverter);
        ReflectionTestUtils.setField(target, "onElectionConverter", onElectionConverter);
        ReflectionTestUtils.setField(target, "onConsensusConverter", onConsensusConverter);
        ReflectionTestUtils.setField(target, "onSettleConverter", onSettleConverter);
        when(chainConfig.getElectionBackwardBlockCount()).thenReturn(BigInteger.ONE);
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(40));
        when(onElectionConverter.convert(any(),any())).thenReturn(Optional.of(nodeOptList));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(80));
    }

    @Test
    public void test() throws Exception {
        Block block = blockList.get(0);
        EpochMessage epochMessage=EpochMessage.newInstance();
        CollectionEvent event = CollectionEvent.builder()
                .block(block)
                .transactions(new ArrayList<>(transactionList))
                .epochMessage(epochMessage)
                .build();

        block.setNum(79L);
        epochMessage.setConsensusEpochRound(BigInteger.TEN);
        target.getParameters(event);
        block.setNum(80L);
        target.getParameters(event);
        verify(target, times(2)).getParameters(any());
    }
}
