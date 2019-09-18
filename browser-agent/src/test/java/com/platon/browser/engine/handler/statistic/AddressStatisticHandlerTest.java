package com.platon.browser.engine.handler.statistic;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.CacheConstructException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 地址统计业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressStatisticHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(AddressStatisticHandlerTest.class);
    @Spy
    private AddressStatisticHandler handler;
    @Mock
    private CacheHolder cacheHolder;

    /**
     * 测试开始前，设置相关行为属性
     *
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
    }

    @Test
    public void testHandler () throws CacheConstructException {
        NodeCache nodeCache = new NodeCache();
        nodeCache.init(nodes,stakings,delegations,unDelegations);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        CustomNetworkStat networkStatCache = new CustomNetworkStat();
        when(cacheHolder.getNetworkStatCache()).thenReturn(networkStatCache);

        EventContext context = new EventContext();
        context.setTransaction(transactions.get(0));
        handler.handle(context);

        transactions.forEach(context::setTransaction);
        handler.handle(context);

        verify(handler, times(2)).handle(any(EventContext.class));
    }
}
