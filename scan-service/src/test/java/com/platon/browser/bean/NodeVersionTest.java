package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NodeVersionTest {
    @Test
    public void test(){
        NodeVersion nv = new NodeVersion();
        nv.setProgramVersion(65321);
        nv.setNodeId("0xdsfsdffs");
        nv.getBigVersion();
        nv.getNodeId();

        assertTrue(true);
    }
}
