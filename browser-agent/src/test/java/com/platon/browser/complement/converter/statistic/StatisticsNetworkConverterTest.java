package com.platon.browser.complement.converter.statistic;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsNetworkConverterTest extends AgentTestBase {
	
	@Mock
	private NetworkStatCache networkStatCache;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    
	
    @Spy
    private StatisticsNetworkConverter target;


    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"networkStatCache",networkStatCache);
        ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
        ReflectionTestUtils.setField(target,"chainConfig",chainConfig);
        ReflectionTestUtils.setField(target,"statisticBusinessMapper",statisticBusinessMapper);
        NetworkStat networkStat = new NetworkStat();
        networkStat.setNodeOptSeq(1L);
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0xbfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf")
                .nodeName("zrj-node1")
                .stakingBlockNum(new BigInteger("20483"))
                .build();
        when(networkStatCache.getNetworkStat()).thenReturn(networkStat);
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(blockChainConfig.getAddIssuePeriodBlockCount());
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(blockChainConfig.getAddIssuePeriodBlockCount());
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(blockChainConfig.getSettlePeriodBlockCount());

    }

    @Test
    public void convert()throws Exception{
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setBlockReward(new BigDecimal("100000"));
        epochMessage.setStakeReward(new BigDecimal("100000"));
        epochMessage.setIssueEpochRound(BigInteger.TEN);
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        epochMessage.setCurrentBlockNumber(BigInteger.TEN);
        epochMessage.setConsensusEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = CollectionEvent.builder()
                .block(block)
                .epochMessage(epochMessage)
                .transactions(new ArrayList <>(transactionList))
                .build();
        target.convert(collectionEvent,block,epochMessage);
    }
}
