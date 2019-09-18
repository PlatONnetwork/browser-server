package com.platon.browser.engine.handler.epoch;

import com.platon.browser.TestBase;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.handler.EventContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description:
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NewIssueEpochHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(NewConsensusEpochHandlerTest.class);
    @Spy
    private NewIssueEpochHandler handler;
    @Mock
    private BlockChain bc;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "bc", bc);
    }

    @Test
    public void testHandle() throws Exception {
        when(bc.getCurBlock()).thenReturn(blocks.get(0));

        EventContext context = new EventContext();
        handler.handle(context);

        verify(handler, times(1)).handle(any(EventContext.class));
    }
}
