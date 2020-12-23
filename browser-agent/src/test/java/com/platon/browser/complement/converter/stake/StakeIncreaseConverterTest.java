package com.platon.browser.complement.converter.stake;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @description: 增持质押转换器测试类
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeIncreaseConverterTest extends AgentTestBase  {

    @Mock
    private StakeBusinessMapper stakeBusinessMapper;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private CollectionEvent collectionEvent;
    @Mock
    private NetworkStatCache networkStatCache;
    @InjectMocks
    @Spy
    private StakeIncreaseConverter target;

    @Before
    public void setup()throws Exception{
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0xbfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf")
                .nodeName("zrj-node1")
                .stakingBlockNum(new BigInteger("20483"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
        when(networkStatCache.getAndIncrementNodeOptSeq()).thenReturn(3l);
    }

    @Test
    public void convert(){
        CollectionTransaction collectionTransaction = null;
        for(CollectionTransaction transaction : transactionList){
            if(transaction.getTypeEnum().equals(Transaction.TypeEnum.STAKE_INCREASE))
                collectionTransaction = transaction;
        }

        target.convert(collectionEvent,collectionTransaction);
    }
}
