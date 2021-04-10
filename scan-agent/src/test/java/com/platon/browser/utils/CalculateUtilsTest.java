//package com.platon.browser.utils;
//
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.complement.dto.AnnualizedRateInfo;
//import com.platon.browser.complement.dto.PeriodValueElement;
//import com.platon.browser.dao.entity.Staking;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class CalculateUtilsTest extends AgentTestBase {
//
//    @Test
//    public void test(){
//        long ib = CalculateUtils.calculateAddIssueBegin(BigInteger.TEN,BigInteger.valueOf(15L));
//        assertEquals(140L,ib);
//
//        long ie = CalculateUtils.calculateAddIssueEnd(BigInteger.TEN,BigInteger.valueOf(15L));
//        assertEquals(150L,ie);
//
//        long ns = CalculateUtils.calculateNextSetting(BigInteger.TEN,BigInteger.TEN,BigInteger.valueOf(95L));
//        assertEquals(5L,ns);
//
//        AnnualizedRateInfo ari = new AnnualizedRateInfo();
//        List<PeriodValueElement> cost = new ArrayList<>();
//        PeriodValueElement pv = new PeriodValueElement();
//        pv.setPeriod(1L);
//        pv.setValue(BigDecimal.TEN);
//        cost.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(2L);
//        pv.setValue(BigDecimal.TEN);
//        cost.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(3L);
//        pv.setValue(BigDecimal.TEN);
//        cost.add(pv);
//        List<PeriodValueElement> profit = new ArrayList<>();
//        pv = new PeriodValueElement();
//        pv.setPeriod(1L);
//        pv.setValue(BigDecimal.TEN);
//        profit.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(2L);
//        pv.setValue(BigDecimal.TEN);
//        profit.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(3L);
//        pv.setValue(BigDecimal.TEN);
//        profit.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(4L);
//        pv.setValue(BigDecimal.TEN);
//        profit.add(pv);
//        ari.setStakeCost(cost);
//        ari.setStakeProfit(profit);
//
//        BigDecimal ar = CalculateUtils.calculateAnnualizedRate(ari,blockChainConfig);
//
//        pv = new PeriodValueElement();
//        pv.setPeriod(6L);
//        pv.setValue(BigDecimal.TEN);
//        cost.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(8L);
//        pv.setValue(BigDecimal.TEN);
//        cost.add(pv);
//
//        pv = new PeriodValueElement();
//        pv.setPeriod(6L);
//        pv.setValue(BigDecimal.TEN);
//        profit.add(pv);
//        pv = new PeriodValueElement();
//        pv.setPeriod(8L);
//        pv.setValue(BigDecimal.TEN);
//        profit.add(pv);
//
//        ar = CalculateUtils.calculateAnnualizedRate(ari,blockChainConfig);
//
//
//        BigDecimal iv = CalculateUtils.calculationIssueValue(BigInteger.valueOf(3L),blockChainConfig,BigDecimal.valueOf(100099993333333L));
//
//        BigDecimal tv = CalculateUtils.calculationTurnValue(blockChainConfig,BigInteger.valueOf(3L),BigDecimal.valueOf(30000000000L),BigDecimal.valueOf(100000000000L),BigDecimal.valueOf(30000000L));
//
//
//        Staking staking = stakingList.get(0);
//
//        CalculateUtils.rotateProfit(staking,BigInteger.ONE,ari,blockChainConfig);
//
//        CalculateUtils.rotateCost(staking,BigInteger.ONE,ari,blockChainConfig);
//
//        assertTrue(true);
//    }
//
//
//}
