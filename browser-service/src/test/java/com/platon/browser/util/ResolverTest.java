package com.platon.browser.util;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;
import org.web3j.rlp.RlpString;

public class ResolverTest {

	@Test
	public void testBigIntegerResolver() {
		RlpString rlpString = RlpString.create(new BigInteger("123"));
		BigInteger num = Resolver.bigIntegerResolver(rlpString);
		assertTrue(num.intValue()==123);
	}

	@Test
	public void testStringResolver() {
		RlpString rlpString = RlpString.create(new BigInteger("123"));
		String num = Resolver.StringResolver(rlpString);
		assertTrue("0x7b".equals(num));
	}

	@Test
	public void testObjectResolver() {
		
	}

}
