package com.platon.browser.complement.converter.restricting;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.RestrictingBusinessMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;



/**
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 创建锁仓计划转换器测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class RestrictingCreateConverterTest extends AgentTestBase {

    @Mock
    private RestrictingBusinessMapper restrictingBusinessMapper;
    @InjectMocks
    @Spy
    private RestrictingCreateConverter target;

    @Before
    public void setup()throws Exception{
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        Transaction tx = new Transaction();
        for(CollectionTransaction collectionTransaction : transactionList){
            if(collectionTransaction.getTypeEnum().equals(Transaction.TypeEnum.RESTRICTING_CREATE)){
                tx = collectionTransaction;
            }
        }
        target.convert(collectionEvent,tx);
    }
}