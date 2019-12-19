package com.platon.browser.collection.service.block;

import com.platon.browser.AgentTestBase;
import com.platon.browser.collection.exception.CollectionBlockException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net 资金
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class BlockServiceTest extends AgentTestBase {
    @Mock
    private BlockRetryService retryService;
    @Spy
    private BlockService target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "retryService", retryService);
    }

    @Test
    public void test() throws IOException, CollectionBlockException {
        target.getBlockAsync(1L);
        when(retryService.getBlock(any())).thenThrow(new RuntimeException(""));
        target.getBlockAsync(1L);

        target.checkBlockNumber(1L);
        doThrow(new RuntimeException("")).when(retryService).checkBlockNumber(anyLong());
        target.checkBlockNumber(1L);

    }

}
