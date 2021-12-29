package com.platon.browser.cache;

import com.platon.browser.AgentTestBase;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatCacheTest extends AgentTestBase {

    @Mock
    private TpsCalcCache tpsCalcCache;

    @Spy
    private NetworkStatCache networkStatCache;

    @Mock
    private NetworkStatMapper networkStatMapper;

    @Test
    public void test() {
        List<NetworkStat> networkStatList = new ArrayList<>();
        when(networkStatMapper.selectByExample(any())).thenReturn(networkStatList);
        ReflectionTestUtils.setField(this.networkStatCache, "tpsCalcCache", this.tpsCalcCache);
        this.networkStatCache.init(this.networkStatList.get(0));
        TpsCalcCache tpsCalcCache = new TpsCalcCache();
        this.networkStatCache.setTpsCalcCache(tpsCalcCache);
        assertEquals(this.networkStatCache.getTpsCalcCache(), tpsCalcCache);
        this.networkStatCache.setNetworkStat(this.networkStatList.get(0));
        assertEquals(this.networkStatCache.getNetworkStat(), this.networkStatList.get(0));
//        this.networkStatCache.updateByBlock(this.blockList.get(0));
        this.networkStatCache.updateByTask(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE);
        this.networkStatCache.getNetworkStat();
        this.networkStatCache.getTpsCalcCache();
    }

}
