package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NodeCacheTest extends AgentTestBase {
    @Spy
    private NodeCache target;
    @Before
    public void setup() {


    }

    @Test
    public void test() throws NoSuchBeanException {
        target.init(new ArrayList<>(nodeList));
        target.getNode(nodeList.get(0).getNodeId());
        NodeItem nodeItem = NodeItem.builder().build();
        BeanUtils.copyProperties(nodeList.get(0),nodeItem);
        target.addNode(nodeItem);

    }
}
