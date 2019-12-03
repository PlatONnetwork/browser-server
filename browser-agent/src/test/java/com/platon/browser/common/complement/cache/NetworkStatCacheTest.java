package com.platon.browser.common.complement.cache;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatCacheTest extends AgentTestBase {

    @Spy
    private NetworkStatCache networkStatCache;
    @Test
    public void test(){
        networkStatCache.init(networkStatList.get(0));
        TpsCalcCache tpsCalcCache = new TpsCalcCache();
        networkStatCache.setTpsCalcCache(tpsCalcCache);
        networkStatCache.setNetworkStat(networkStatList.get(0));
        networkStatCache.updateByBlock(blockList.get(0),333);
        networkStatCache.updateByTask(BigDecimal.TEN,BigDecimal.ONE,BigDecimal.TEN,BigDecimal.ONE,33,33,BigDecimal.TEN);
        networkStatCache.getNetworkStat();
        networkStatCache.getAndIncrementNodeOptSeq();
        networkStatCache.getTpsCalcCache();
    }
}
