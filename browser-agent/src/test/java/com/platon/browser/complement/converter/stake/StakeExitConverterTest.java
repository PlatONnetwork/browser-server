package com.platon.browser.complement.converter.stake;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 退出验证人(退出质押)转化器测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeExitConverterTest extends AgentTestBase {

    @Mock
    private StakeBusinessMapper stakeBusinessMapper;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private NetworkStatCache networkStatCache;
    @Spy
    private StakeExitConverter target;

    @Before
    public void setup()throws Exception{
        ReflectionTestUtils.setField(target,"stakeBusinessMapper",stakeBusinessMapper);
        ReflectionTestUtils.setField(target,"networkStatCache",networkStatCache);
        ReflectionTestUtils.setField(target,"nodeCache",nodeCache);
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0xbfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf")
                .nodeName("zrj-node1")
                .stakingBlockNum(new BigInteger("20483"))
                .build();

        when(nodeCache.getNode(any())).thenReturn(nodeItem);
    }


    @Test
    public void convert(){
        Transaction tx = new Transaction();
        for (Transaction transaction : transactionList){
            if(transaction.getTypeEnum().equals(Transaction.TypeEnum.STAKE_EXIT))
                tx=transaction;
        }
        EpochMessage epochMessage = EpochMessage.newInstance();
        epochMessage.setSettleEpochRound(new BigInteger("13"));
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setEpochMessage(epochMessage);
        target.convert(collectionEvent,tx);
    }
}