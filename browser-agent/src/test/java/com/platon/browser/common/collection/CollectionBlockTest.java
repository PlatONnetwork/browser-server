package com.platon.browser.common.collection;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class CollectionBlockTest extends AgentTestBase {

    @Test
    public void test() throws BeanCreateOrUpdateException {
        CollectionBlock block = CollectionBlock.newInstance();
        block.updateWithRawBlockAndReceiptResult(rawBlockList.get(0),receiptResultList.get(0));
    }
}
