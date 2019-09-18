package com.platon.browser.engine.handler.slash;

import com.platon.browser.TestBase;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
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
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 多签举报业务处理测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ReportValidatorHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(ReportValidatorHandlerTest.class);
    @Spy
    private ReportValidatorHandler handler;
    @Mock
    private CacheHolder cacheHolder;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private BlockChain bc;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(handler, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(handler, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(handler, "bc", bc);
    }

    /**
     *  多签举报测试方法
     */
    @Test
    public void testHandler () throws NoSuchBeanException, CacheConstructException {
        NodeCache nodeCache = new NodeCache();
        nodeCache.init(nodes,stakings,delegations,unDelegations);
        when(cacheHolder.getNodeCache()).thenReturn(nodeCache);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        when(chainConfig.getDuplicateSignSlashRate()).thenReturn(BigDecimal.valueOf(100));
        when(bc.getCurBlock()).thenReturn(blocks.get(0));
        when(bc.getCurSettingEpoch()).thenReturn(BigInteger.ONE);

        EventContext context = new EventContext();
        context.setTransaction(transactions.get(0));
        handler.handle(context);

        transactions.stream()
                .filter(tx->CustomTransaction.TxTypeEnum.REPORT_VALIDATOR.code.equals(tx.getTxType()))
                .forEach(context::setTransaction);
        handler.handle(context);

        verify(handler, times(2)).handle(any(EventContext.class));
    }
}
