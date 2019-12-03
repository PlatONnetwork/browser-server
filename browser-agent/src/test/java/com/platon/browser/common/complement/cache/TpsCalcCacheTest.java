package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.exception.NoSuchBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TpsCalcCacheTest extends AgentTestBase {

    @Spy
    private TpsCalcCache tpsCalcCache;
    @Test
    public void test() throws NoSuchBeanException {
        tpsCalcCache.update(blockList.get(0));
        tpsCalcCache.getTps();
    }
}
