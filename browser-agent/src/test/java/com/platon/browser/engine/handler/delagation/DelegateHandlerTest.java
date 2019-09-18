package com.platon.browser.engine.handler.delagation;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.delegation.DelegateHandler;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 委托处理业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(DelegateHandlerTest.class);
    @Spy
    private DelegateHandler handler;
    @Mock
    private CacheHolder cacheHolder;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
    }

    /*
    *    委托测试方法
    */
    @Test
    public void testHandler () throws CacheConstructException, NoSuchBeanException {
        NodeCache nodeCache = mock(NodeCache.class);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        CustomNode node = mock(CustomNode.class);
        when(nodeCache.getNode(any())).thenReturn(node);
        when(node.getLatestStaking()).thenReturn(stakings.get(0));

        EventContext context = new EventContext();
        context.setTransaction(transactions.get(0));
        handler.handle(context);

        transactions.stream()
            .filter(tx->CustomTransaction.TxTypeEnum.DELEGATE.code.equals(tx.getTxType()))
            .forEach(context::setTransaction);
        handler.handle(context);

        verify(handler, times(2)).handle(any(EventContext.class));
    }
}
