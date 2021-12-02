package com.platon.browser.cache;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.platon.browser.AgentTestBase;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatCacheTest extends AgentTestBase {

    @Mock
    private TpsCalcCache tpsCalcCache;

    @Spy
    private NetworkStatCache networkStatCache;

    @Test
    public void test() {
        ReflectionTestUtils.setField(this.networkStatCache, "tpsCalcCache", this.tpsCalcCache);
        this.networkStatCache.init(this.networkStatList.get(0));
        TpsCalcCache tpsCalcCache = new TpsCalcCache();
        this.networkStatCache.setTpsCalcCache(tpsCalcCache);
        assertEquals(this.networkStatCache.getTpsCalcCache(), tpsCalcCache);
        this.networkStatCache.setNetworkStat(this.networkStatList.get(0));
        assertEquals(this.networkStatCache.getNetworkStat(), this.networkStatList.get(0));
        this.networkStatCache.updateByBlock(this.blockList.get(0));
        this.networkStatCache.updateByTask(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
        this.networkStatCache.getNetworkStat();
        this.networkStatCache.getTpsCalcCache();
    }

}
