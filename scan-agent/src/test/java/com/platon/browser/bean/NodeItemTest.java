package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NodeItemTest {

    @Test
    public void test(){
        NodeItem nodeItem = NodeItem.builder()
                .nodeId("0x1m3ei2rjn3tn2493nfr932f234")
                .nodeName("Test")
                .stakingBlockNum(BigInteger.TEN)
                .build();
        nodeItem.setNodeName("test1");
        nodeItem.setStakingBlockNum(BigInteger.TEN);
        nodeItem.setNodeId("test1");
        nodeItem.getNodeId();
        nodeItem.getStakingBlockNum();
        nodeItem.getNodeName();
    }
}
