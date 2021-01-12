package com.platon.browser.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bean.ComplementEvent;
import com.platon.browser.publisher.PersistenceEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ComplementEventHandlerTest extends AgentTestBase {
    @Mock
    private PersistenceEventPublisher persistenceEventPublisher;
    @InjectMocks
    @Spy
    private ComplementEventHandler target;

    @Before
    public void setup() throws Exception {
    }

    @Test
    public void test() throws Exception {
        ComplementEvent event = new ComplementEvent();
        event.setBlock(blockList.get(0));
        event.setTransactions(new ArrayList<>(transactionList));
        event.setNodeOpts(nodeOptList);
        target.onEvent(event,33,false);
        verify(target, times(1)).onEvent(any(),anyLong(),anyBoolean());
    }
}
