package com.platon.browser.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.platon.browser.exception.BlockNumberException;

import java.math.BigInteger;

public class EpochUtilTest {

	@Test
	public void testGetEpoch() throws BlockNumberException {
		assertTrue((EpochUtil.getEpoch(BigInteger.valueOf(1), 5l)==1));
	}

	@Test
	public void testGetPreEpochLastBlockNumber() throws BlockNumberException {
		assertEquals(0,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(1),5L).longValue());
		assertEquals(0,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(5),5L).longValue());
		assertEquals(5,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(6),5L).longValue());
		assertEquals(5,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(10),5L).longValue());
		assertEquals(10,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(11),5L).longValue());
	}

	@Test
	public void testGetCurEpochLastBlockNumber() throws BlockNumberException {
		assertEquals(5,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(1),5L).longValue());
		assertEquals(5,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(5),5L).longValue());
		assertEquals(10,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(6),5L).longValue());
		assertEquals(10,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(10),5L).longValue());
		assertEquals(15,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(11),5L).longValue());
	}

}
