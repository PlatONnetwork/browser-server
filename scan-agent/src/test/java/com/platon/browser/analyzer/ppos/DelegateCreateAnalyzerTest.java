package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.DelegateBusinessMapper;
import com.platon.browser.dao.custommapper.CustomGasEstimateMapper;
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
 * @Auther: dongqile
 * @Date: 2019/11/13
 * @Description: 委托创建转换器测试类
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateCreateAnalyzerTest extends AgentTestBase {

    @Mock
    private DelegateBusinessMapper delegateBusinessMapper;
    @Mock
    private CollectionEvent collectionEvent;
    @Mock
    private NodeCache nodeCache;
    @Mock
    private CustomGasEstimateMapper customGasEstimateMapper;
    @InjectMocks
    @Spy
    private DelegateCreateAnalyzer target;


    @Before
    public void setup() throws Exception{
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
    }

    @Test
    public void convert() throws Exception {
        CollectionTransaction tx = null;
        for (CollectionTransaction collectionTransaction : transactionList) {
            if(collectionTransaction.getTypeEnum()== Transaction.TypeEnum.DELEGATE_CREATE)
                tx=collectionTransaction;
        }
        target.analyze(collectionEvent,tx);
    }
}