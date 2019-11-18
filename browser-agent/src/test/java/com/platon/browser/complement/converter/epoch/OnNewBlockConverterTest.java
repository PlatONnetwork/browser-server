package com.platon.browser.complement.converter.epoch;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.NewBlockMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class OnNewBlockConverterTest  extends AgentTestBase {
	
    @Mock
    private NodeCache nodeCache;
    @Mock
    private NewBlockMapper newBlockMapper;
    @Mock
    private NetworkStatCache networkStatCache;

    @Spy
    private OnNewBlockConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
        ReflectionTestUtils.setField(target,"newBlockMapper",newBlockMapper);
        ReflectionTestUtils.setField(target,"networkStatCache",networkStatCache);
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(any())).thenReturn(nodeItem);
        when(networkStatCache.getNetworkStat()).thenReturn(new NetworkStat());
    }


    @Test
    public void convert()throws Exception{
        Block block = blockList.get(0);
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setBlockReward(new BigDecimal("200000"));
        CollectionEvent collectionEvent = CollectionEvent.builder()
                .block(block)
                .epochMessage(epochMessage)
                .build();
        target.convert(collectionEvent,block);
    }
}
