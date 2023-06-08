package com.platon.browser.bean;

import com.platon.browser.bean.EpochInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochInfoTest {
    @Test
    public void test(){
        EpochInfo epochInfo = new EpochInfo();
        epochInfo.setAvgPackTime(BigDecimal.ONE);
        epochInfo.setPackageReward(new BigInteger("1"));
        epochInfo.setRemainEpoch(BigDecimal.ONE);
        epochInfo.setStakingReward(new BigInteger("1"));
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
