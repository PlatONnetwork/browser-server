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
        epochInfo.setYearEndNum(BigDecimal.ONE);
        epochInfo.setYearNum(BigDecimal.ONE);
        epochInfo.setYearStartNum(BigDecimal.ONE);
        assertEquals(epochInfo.getAvgPackTime(), BigDecimal.ONE);
        assertEquals(epochInfo.getPackageReward(), BigDecimal.ONE);
        assertEquals(epochInfo.getRemainEpoch(), BigDecimal.ONE);
        assertEquals(epochInfo.getStakingReward(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearEndNum(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearNum(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearStartNum(), BigDecimal.ONE);
    }
}
