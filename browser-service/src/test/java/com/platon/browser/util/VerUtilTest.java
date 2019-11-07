package com.platon.browser.util;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

import com.platon.browser.utils.VerUtil;

/**
 * 
 *  @file VerUtilTest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年9月9日
 */
public class VerUtilTest {

    @Test
    public void shouldEquals() {
        BigInteger version = VerUtil.toInteger("1.1.0");
        assertEquals(BigInteger.valueOf(65792), version);
        try {
			version = VerUtil.toInteger("abcdef");
		} catch (Exception e) {
			assertTrue(e instanceof NumberFormatException);
		}
    }


    @Test
    public void shouldVersionEquests() {
        String version = VerUtil.toVersion(BigInteger.valueOf(65792));
        assertEquals("1.1.0", version);
    }
    
    @Test
    public void TestTransferBigVersion() {
    	BigInteger version = VerUtil.transferBigVersion(BigInteger.valueOf(65792));
        assertEquals("65792", version.toString());
    }
}
