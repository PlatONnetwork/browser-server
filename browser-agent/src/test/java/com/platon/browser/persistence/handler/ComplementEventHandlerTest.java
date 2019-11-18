package com.platon.browser.persistence.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.persistence.queue.publisher.PersistenceEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComplementEventHandlerTest extends AgentTestBase {
    @Mock
    private PersistenceEventPublisher persistenceEventPublisher;

    @Spy
    private ComplementEventHandler target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "persistenceEventPublisher", persistenceEventPublisher);
    }

    @Test(expected = Exception.class)
    public void test() throws Exception {
        ComplementEvent event = ComplementEvent.builder()
                .block(blockList.get(0))
                .transactions(new ArrayList<>(transactionList))
                .nodeOpts(nodeOptList)
                .build();
        target.onEvent(event,33,false);
        verify(target, times(1)).onEvent(any(),anyLong(),anyBoolean());

        doThrow(new RuntimeException("")).when(persistenceEventPublisher).publish(any(),anyList(),anyList());
        target.onEvent(event,33,false);
    }
}
