package com.platon.browser.common.queue.complement.publisher;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.publisher.CollectionEventPublisher;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.persistence.queue.handler.PersistenceEventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.utils.TXTypeEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComplementEventPublisherTest extends AgentTestBase {
    @Mock
    private IComplementEventHandler handler;

    @Spy
    private ComplementEventPublisher target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "handler", handler);
        ReflectionTestUtils.setField(target, "ringBufferSize", 1024);
    }

    @Test
    public void test(){
        ReflectionTestUtils.invokeMethod(target,"init");
        EpochMessage epochMessage = EpochMessage.newInstance();
        Block block = blockList.get(0);
        List<Transaction> transactions = new ArrayList<>(transactionList);

        target.publish(block,transactions,nodeOptList);
        target.getRingBufferSize();
        target.info();
        target.getPublisherMap();
        target.register(target.getClass().getSimpleName(),target);
        target.unregister(target.getClass().getSimpleName());
        verify(target, times(1)).publish(any(),any(),any());
    }
}
