package com.platon.browser.util;

import java.math.BigInteger;

import org.junit.Test;
import org.web3j.rlp.RlpString;

import com.platon.browser.util.Resolver;

public class ResolverTest {

	@Test
	public void testBigIntegerResolver() {
		RlpString rlpString = RlpString.create(new BigInteger("123"));
		BigInteger num = Resolver.bigIntegerResolver(rlpString);
		System.out.println(num);
	}

	@Test
	public void testStringResolver() {
		RlpString rlpString = RlpString.create(new BigInteger("123"));
		String num = Resolver.StringResolver(rlpString);
		System.out.println(num);
	}

	@Test
	public void testObjectResolver() {
		// TODO
	}

}
