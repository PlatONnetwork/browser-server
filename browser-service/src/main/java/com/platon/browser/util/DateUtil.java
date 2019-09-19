package com.platon.browser.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 时间处理工具类
 * 
 * @file DateUtil.java
 * @description
 * @author zhangrj
 * @data 2019年8月31日
 */
@Service
public class DateUtil {
	
	private static final String DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

	public static DateFormat df;
	
	public DateUtil() {
		DateUtil.df = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
	}
	
	private static String localLANG;
	
	/**
	 * 获取年的第一天
	 * 
	 * @method getYearFirstDate
	 * @param date
	 * @return
	 */
	public static Date getYearFirstDate(Date date) {
		SimpleDateFormat y = new SimpleDateFormat("yyyy");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		String year = y.format(date);
		String firstStr = year + "-01-01";
		Date firstDate = null;
		try {
			firstDate = ymd.parse(firstStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return firstDate;
	}

	/**
	 * 转换成CST
	 * @method getCST
	 * @param strGMT
	 * @return
	 * @throws ParseException
	 */
	public static Date getCST(String strGMT) throws ParseException { 
		DateFormat df = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
		return df.parse(strGMT); 
	}

	/**
	 * 转换成GMT
	 * @method getGMT
	 * @param dateCST
	 * @return
	 */
	public static String getGMT(Date dateCST) {
		Locale locale = Locale.forLanguageTag(localLANG);
		DateFormat df = new SimpleDateFormat(DATE_PATTERN, locale);
		df.setTimeZone(TimeZone.getTimeZone("GMT")); // modify Time Zone.
		return (df.format(dateCST));
	}

	public String getLocalLANG() {
		return localLANG;
	}

	@Value("${localLANG:en}")
	public void setLocalLANG(String localLANG) {
		DateUtil.localLANG = localLANG;
	}
	

}
