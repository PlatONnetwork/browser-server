package com.platon.browser.engine.handler.proposal;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 版本声明业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class DeclareVersionHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(DeclareVersionHandlerTest.class);
    @Spy
    private DeclareVersionHandler declareVersionHandler;
    @Mock
    private CacheHolder cacheHolder;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(declareVersionHandler, "cacheHolder", cacheHolder);
    }

    /**
     *  版本声明测试方法
     */
    @Test
    public void testHandler () throws CacheConstructException, NoSuchBeanException {
        NodeCache nodeCache = new NodeCache();
        nodeCache.init(nodes,stakings,delegations,unDelegations);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);

        EventContext context = new EventContext();
        transactions.stream()
                .filter(tx->CustomTransaction.TxTypeEnum.DECLARE_VERSION.code.equals(tx.getTxType()))
                .forEach(context::setTransaction);
        declareVersionHandler.handle(context);

        verify(declareVersionHandler, times(1)).handle(any(EventContext.class));
    }
}
