package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HistoryLowRateTest {
    @Test
    public void test(){
    	HistoryLowRateSlash historyLowRateSlash = new HistoryLowRateSlash ();
    	historyLowRateSlash.setNodeId("0x1");
    	historyLowRateSlash.setAmount(BigInteger.ONE);
        assertEquals(historyLowRateSlash.getNodeId(), "0x1");
        assertEquals(historyLowRateSlash.getAmount(), BigInteger.ONE);
    }
}
