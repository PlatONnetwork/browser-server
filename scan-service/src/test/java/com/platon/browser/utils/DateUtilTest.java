package com.platon.browser.utils;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class DateUtilTest {

	private DateUtil dateUtil;

	@Before
	public void setUp() throws Exception {
		dateUtil = new DateUtil();
		dateUtil.setLocalLANG("en");
	}

	@Test
	public void testGetYearFirstDate() {
		Date date = new Date();
		DateUtil.getYearFirstDate(date);
		assertTrue(true);
	}

	@Test
	public void testGetCST() throws ParseException {
		// 1568083463000
		// Tue, 10 Sep 2019 10:44:23 CST
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		Date date = df.parse("Tue, 10 Sep 2019 10:44:23 CST");
		assertTrue(date.getTime()==1568083463000L);
	}

	@Test
	public void testGetGMT() {
		// 1568083846564
		// Tue, 10 Sep 2019 02:50:46 GMT
		Date date = new Date(1568083846564L);
		String gmtStr = DateUtil.getGMT(date);
		assertTrue("Tue Sep 10 2019 02:50:46".equals(gmtStr));
	}

	@Test
	public void testTimeZoneTransfer() throws ParseException{
		Date date = new Date(1568083846564L);
		String dataStr = DateUtil.timeZoneTransfer(date, "0", "+8");
		assertTrue("星期二 九月 10 2019 18:50:46".equals(dataStr));
		dataStr = DateUtil.timeZoneTransfer(date, "EEE MMM dd yyyy HH:mm:ss", "0", "+8");
		assertTrue("星期二 九月 10 2019 18:50:46".equals(dataStr));
		dataStr = DateUtil.timeZoneTransferUTC(date, "EEE MMM dd yyyy HH:mm:ss");
		assertTrue("星期二 九月 10 2019 18:50:46".equals(dataStr));
	}

	@Test
	public void testConverTime(){
		DateUtil.covertTime(new Date());
	}

}
