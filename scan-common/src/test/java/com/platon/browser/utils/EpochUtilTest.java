package com.platon.browser.utils;

import com.platon.browser.exception.BlockNumberException;
import org.junit.Test;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EpochUtilTest {

	@Test
	public void testGetEpoch() throws BlockNumberException {
		assertTrue((EpochUtil.getEpoch(BigInteger.valueOf(1), BigInteger.valueOf(5)).intValue()==1));
	}

	@Test
	public void testGetPreEpochLastBlockNumber() throws BlockNumberException {
		assertEquals(0,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(1),BigInteger.valueOf(5)).longValue());
		assertEquals(0,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(5),BigInteger.valueOf(5)).longValue());
		assertEquals(5,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(6),BigInteger.valueOf(5)).longValue());
		assertEquals(5,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(10),BigInteger.valueOf(5)).longValue());
		assertEquals(10,EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(11),BigInteger.valueOf(5)).longValue());
	}

	@Test
	public void testGetCurEpochLastBlockNumber() throws BlockNumberException {
		assertEquals(5,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(1),BigInteger.valueOf(5)).longValue());
		assertEquals(5,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(5),BigInteger.valueOf(5)).longValue());
		assertEquals(10,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(6),BigInteger.valueOf(5)).longValue());
		assertEquals(10,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(10),BigInteger.valueOf(5)).longValue());
		assertEquals(15,EpochUtil.getCurEpochLastBlockNumber(BigInteger.valueOf(11),BigInteger.valueOf(5)).longValue());
	}

	@Test
	public void testScanAgentSpeedCalculate() throws BlockNumberException {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime date1 = LocalDateTime.parse("2023-03-16 09:54:58", inputFormatter);
		LocalDateTime date2 = LocalDateTime.parse("2023-03-17 14:44:51", inputFormatter);
		long seconds = Duration.between(date1, date2).getSeconds();

		long total = 11615350;

		System.out.println(total / seconds);
	}

	@Test
	public void testFmisSpeedCalculate() throws BlockNumberException {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime date1 = LocalDateTime.parse("2023-03-16 08:09:16", inputFormatter);
		LocalDateTime date2 = LocalDateTime.parse("2023-03-16 20:55:28", inputFormatter);
		long seconds = Duration.between(date1, date2).getSeconds();

		long total = 55713793L-53809695L;

		System.out.println(total / seconds);
	}

}
