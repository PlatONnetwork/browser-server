package com.platon.browser.common.collection;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class CollectionTransactionTest extends AgentTestBase {

    @Test
    public void test() {
        CollectionTransaction transaction = CollectionTransaction.newInstance();
    }
}
