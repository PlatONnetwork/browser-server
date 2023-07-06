package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.RestrictingBusinessMapper;
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
    private RestrictingCreateAnalyzer target;

    @Before
    public void setup()throws Exception{
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        Transaction tx = new Transaction();
        for(com.platon.browser.elasticsearch.dto.Transaction  dtoTransaction : transactionList){
            if(dtoTransaction.getTypeEnum().equals(Transaction.TypeEnum.RESTRICTING_CREATE)){
                tx = dtoTransaction;
            }
        }
        target.analyze(collectionEvent,tx);
    }
}
