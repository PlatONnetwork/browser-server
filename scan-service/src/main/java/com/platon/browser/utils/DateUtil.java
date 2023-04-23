package com.platon.browser.utils;

import com.platon.browser.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


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
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final String DATE_PATTERN = "EEE MMM dd yyyy HH:mm:ss";

	private static String LOCAL_LANG;

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
			logger.error("日期错误:",e);
		}
		return firstDate;
	}

	/**
	 * 转换成GMT
	 * @method getGMT
	 * @param dateCST
	 * @return
	 */
	public static String getGMT(Date dateCST) {
		Locale locale = Locale.forLanguageTag(LOCAL_LANG);
		DateFormat df = new SimpleDateFormat(DATE_PATTERN, locale);
		df.setTimeZone(TimeZone.getTimeZone("GMT")); // modify Time Zone.
		return (df.format(dateCST));
	}

	@Value("${localLANG:en}")
	public void setLocalLANG(String localLANG) {
		LOCAL_LANG = localLANG;
	}

	/**
     * 时区转换
     * @param time 时间字符串
     * @param pattern 格式 "yyyy-MM-dd HH:mm"
     * @param nowTimeZone eg:+8，0，+9，-1 等等
     * @param targetTimeZone 同nowTimeZone
     * @return
     */
    public static String timeZoneTransfer(String time, String pattern, String nowTimeZone, String targetTimeZone) {
        if(StringUtils.isBlank(time)){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + nowTimeZone));
        Date date;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
        	logger.error("时间转换出错。", e);
            return "";
        }
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + targetTimeZone));
        return simpleDateFormat.format(date);
    }

    public static String timeZoneTransfer(Date time, String nowTimeZone, String targetTimeZone) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    	String timeStr = simpleDateFormat.format(time);
    	return DateUtil.timeZoneTransfer(timeStr, DATE_PATTERN, nowTimeZone, targetTimeZone);
    }

    public static String timeZoneTransfer(Date time, String pattern, String nowTimeZone, String targetTimeZone) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	String timeStr = simpleDateFormat.format(time);
    	return DateUtil.timeZoneTransfer(timeStr, pattern, nowTimeZone, targetTimeZone);
    }

    public static String timeZoneTransferUTC(Date time, String pattern) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	String timeStr = simpleDateFormat.format(time);
    	return DateUtil.timeZoneTransfer(timeStr, pattern, "0", "+8");
    }


	public static Date covertTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp now = new Timestamp(date.getTime());
		String str = df.format(now);
		Date newDate = null;
		try {
			newDate = df.parse(str);
		} catch (ParseException e) {
			throw new BusinessException("转换日期去掉毫秒异常");
		}
		return newDate;
	}
}
