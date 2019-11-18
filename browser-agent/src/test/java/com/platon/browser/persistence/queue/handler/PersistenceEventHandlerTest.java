package com.platon.browser.persistence.queue.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.service.elasticsearch.EsImportService;
import com.platon.browser.common.service.redis.RedisImportService;
import com.platon.browser.persistence.handler.ComplementEventHandler;
import com.platon.browser.persistence.queue.event.PersistenceEvent;
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
public class PersistenceEventHandlerTest extends AgentTestBase {
    @Mock
    private EsImportService esImportService;
    @Mock
    private RedisImportService redisImportService;
    @Mock
    private NetworkStatCache networkStatCache;

    @Spy
    private PersistenceEventHandler target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "esImportService", esImportService);
        ReflectionTestUtils.setField(target, "redisImportService", redisImportService);
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
    }

    @Test(expected = Exception.class)
    public void test() throws Exception {
        PersistenceEvent event = PersistenceEvent.builder()
                .block(blockList.get(0))
                .transactions(new ArrayList<>(transactionList))
                .nodeOpts(nodeOptList)
                .build();
        target.setBatchSize(100);
        target.onEvent(event,33,false);
        target.setBatchSize(1);
        target.onEvent(event,33,false);

        target.setBatchSize(1);
        transactionList.clear();
        nodeOptList.clear();
        target.onEvent(event,33,false);

        target.getMaxBlockNumber();
        target.getBatchSize();
        verify(target, times(3)).onEvent(any(),anyLong(),anyBoolean());



        doThrow(new RuntimeException("")).when(esImportService).batchImport(any(),anySet(),anySet());
        target.onEvent(event,33,false);
    }
}
