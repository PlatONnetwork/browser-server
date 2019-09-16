package com.platon.browser.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.platon.browser.exception.BlockNumberException;

public class EpochUtilTest {

	@Test
	public void testGetEpoch() throws BlockNumberException {
		assertTrue((EpochUtil.getEpoch(1l, 5l)==1));
	}

	@Test
	public void testGetPreEpochLastBlockNumber() throws BlockNumberException {
		assertTrue((EpochUtil.getPreEpochLastBlockNumber(1l, 5l)==0));
	}

	@Test
	public void testGetCurEpochLastBlockNumber() throws BlockNumberException {
		assertTrue((EpochUtil.getCurEpochLastBlockNumber(1l, 5l)==5));
	}

}
