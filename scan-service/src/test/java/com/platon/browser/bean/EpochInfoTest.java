package com.platon.browser.bean;

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
        epochInfo.setAvgPackTime(new BigInteger("1"));
        epochInfo.setPackageReward(new BigInteger("1"));
        epochInfo.setRemainEpoch(new BigInteger("1"));
        epochInfo.setStakingReward(new BigInteger("1"));
        epochInfo.setYearEndBlockNum(new BigInteger("1"));
        epochInfo.setChainAge(new BigInteger("1"));
        epochInfo.setYearStartBlockNum(new BigInteger("1"));
        assertEquals(epochInfo.getAvgPackTime(), BigDecimal.ONE);
        assertEquals(epochInfo.getPackageReward(), BigDecimal.ONE);
        assertEquals(epochInfo.getRemainEpoch(), BigDecimal.ONE);
        assertEquals(epochInfo.getStakingReward(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearEndBlockNum(), BigDecimal.ONE);
        assertEquals(epochInfo.getChainAge(), BigDecimal.ONE);
        assertEquals(epochInfo.getYearStartBlockNum(), BigDecimal.ONE);
    }
}
