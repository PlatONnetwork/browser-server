package com.platon.browser.util;


import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import com.platon.browser.util.VerUtil;

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
    }


    @Test
    public void shouldVersionEquests() {
        String version = VerUtil.toVersion(BigInteger.valueOf(65792));
        assertEquals("1.1.0", version);
    }
}
