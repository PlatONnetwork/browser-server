package com.platon.browser.analyzer.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class VersionDeclareConverterTest extends AgentTestBase {

    @Mock
    private NodeCache nodeCache;
    @Mock
    private NetworkStatCache networkStatCache;
    @InjectMocks
    @Spy
    private VersionDeclareAnalyzer target;

    @Before
    public void setup() throws Exception{
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050")
                .nodeName("integration-node1")
                .stakingBlockNum(new BigInteger("88602"))
                .build();
        when(nodeCache.getNode(anyString())).thenReturn(nodeItem);
        when(networkStatCache.getAndIncrementNodeOptSeq()).thenReturn(1L);
    }

    @Test
    public void convert(){
        Block block = blockList.get(0);
        CollectionEvent collectionEvent = new CollectionEvent();
        collectionEvent.setBlock(block);
        Transaction tx = new Transaction();
        for(CollectionTransaction collectionTransaction : transactionList){
            if(collectionTransaction.getTypeEnum().equals(Transaction.TypeEnum.VERSION_DECLARE)){
                tx = collectionTransaction;
            }
        }
        target.analyze(collectionEvent,tx);
    }
	

}
