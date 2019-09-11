package com.platon.browser.service.util;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.platon.browser.util.DateUtil;

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
		Date firstDate = DateUtil.getYearFirstDate(date);
		assertTrue("Tue Jan 01 00:00:00 CST 2019".equals(firstDate.toString()));
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
		assertTrue("Tue, 10 Sep 2019 02:50:46 GMT".equals(gmtStr));
	}

}
