package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class TpsCalcCacheTest extends AgentTestBase {
    @Spy
    private TpsCalcCache target;
    @Before
    public void setup() {


    }

    @Test
    public void test() throws NoSuchBeanException {
        target.update(333,4544343434343l);

        target.getTps();
    }
}
