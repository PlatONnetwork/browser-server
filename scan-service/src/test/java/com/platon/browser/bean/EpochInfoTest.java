package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochInfoTest {
    @Test
    public void test(){
        EpochInfo epochInfo = new EpochInfo();
        epochInfo.setAvgPackTime(BigDecimal.ONE);
        epochInfo.setPackageReward("0x1");
        epochInfo.setRemainEpoch(BigDecimal.ONE);
        epochInfo.setStakingReward("0x1");
        epochInfo.setYearEndBlockNum(BigDecimal.ONE);
        epochInfo.setChainAge(BigDecimal.ONE);
        epochInfo.setYearStartBlockNum(BigDecimal.ONE);
        assertEquals(epochInfo.getAvgPackTime(), BigDecimal.ONE);
        assertEquals(epochInfo.getPackageReward(), BigDecimal.ONE);
        assertEquals(epochInfo.getRemainEpoch(), BigDecimal.ONE);
        assertEquals(epochInfo.getStakingReward(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearEndBlockNum(), BigDecimal.ONE);
        assertEquals(epochInfo.getChainAge(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearStartBlockNum(), BigDecimal.ONE);
    }
}
