package com.platon.browser.old.engine;

import com.platon.browser.TestBase;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.old.engine.cache.AddressCache;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.handler.statistic.AddressStatisticHandler;
import com.platon.browser.old.engine.stage.BlockChainStage;
import com.platon.browser.exception.BeanCreateOrUpdateException;
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

import static org.mockito.Mockito.*;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 委托处理业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressEngineTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(AddressEngineTest.class);
    @Spy
    private AddressEngine target;
    @Mock
    private CacheHolder cacheHolder;

    @Mock
    private CustomAddressMapper customAddressMapper;
    @Mock
    private AddressStatisticHandler addressStatisticHandler;
    @Mock
    private BlockChainConfig chainConfig;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "cacheHolder", cacheHolder);
        ReflectionTestUtils.setField(target, "customAddressMapper", customAddressMapper);
        ReflectionTestUtils.setField(target, "addressStatisticHandler", addressStatisticHandler);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
    }

    @Test
    public void testInit () {
        AddressCache addressCache = mock(AddressCache.class);
        when(cacheHolder.getAddressCache()).thenReturn(addressCache);
        BlockChainStage stageData = new BlockChainStage();
        when(cacheHolder.getStageData()).thenReturn(stageData);
        when(customAddressMapper.selectAll()).thenReturn(addresses);
        when(chainConfig.getPlatOnFundAccount()).thenReturn("0xaaaaaaaaaaaaaaaaaa");
        when(chainConfig.getCommunityFundAccount()).thenReturn("0xbbbbbbbbbbbbbbbbbb");

        target.init();
        verify(target, times(1)).init();
    }

    @Test
    public void testExecute () {
        target.execute(transactions.get(0));
        verify(target, times(1)).execute(any(CustomTransaction.class));
    }
}
