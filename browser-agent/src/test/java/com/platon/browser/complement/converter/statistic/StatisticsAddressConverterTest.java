package com.platon.browser.complement.converter.statistic;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.dao.mapper.AddressMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticsAddressConverterTest extends AgentTestBase {

	
    @Mock
    private AddressCache addressCache;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @Mock
	private AddressMapper addressMapper;

    @Spy
    private StatisticsAddressConverter target;


    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"addressCache",addressCache);
        ReflectionTestUtils.setField(target,"statisticBusinessMapper",statisticBusinessMapper);
        ReflectionTestUtils.setField(target,"addressMapper",addressMapper);
        when(addressCache.getAll()).thenReturn(new ArrayList <>(addressList));
        when(addressMapper.selectByExample(any())).thenReturn(new ArrayList <>(addressList));

    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setIssueEpochRound(BigInteger.TEN);
        epochMessage.setSettleEpochRound(BigInteger.TEN);
        epochMessage.setCurrentBlockNumber(BigInteger.TEN);
        epochMessage.setConsensusEpochRound(BigInteger.TEN);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        collectionEvent.setEpochMessage(epochMessage);
        collectionEvent.setTransactions(new ArrayList <>(transactionList));
        target.convert(collectionEvent,block,epochMessage);
    }
}
