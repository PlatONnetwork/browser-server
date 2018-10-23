package com.platon.browserweb.common.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.github.fartherp.framework.common.util.DateUtil;
import com.platon.browserweb.common.constant.SystemConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * 格式转换工具类
 * 
 * @author niuxj
 *
 */
public class FormatUtils {

	public static String DATE_FORMAT = "yyyyMMddHHmmss";

	public static int PRICE_FACTOR = 10000;

	public static int VOLUME_FACTOR = 10000;

	public static final String _10E_10_S = "0.00000000";

	public static final BigDecimal _10E_10 = new BigDecimal(_10E_10_S);

	public static void main(String args[]){
		long a = 1531742258686L;
		Date date = new Date(a);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = calendar.getTime();
		System.out.println(start);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		Date end = calendar.getTime();
		System.out.println(end);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String starttime = sdf.format(start);
		String sdate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(start);
		System.out.println(starttime);
	}


	/**
	 * Date类型转String
	 * 
	 * @param date
	 * @return
	 */
	public static String toString(Date date) {
	    DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return (null != date) ? df.format(date) : null;
	}

	public static String toString(long millis) {
		Date date = new Date(millis);
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return (null != date) ? df.format(date) : null;
	}


	public static String toString(Date date, String format) {
	    DateFormat df = new SimpleDateFormat(format);
        return (null != date) ? df.format(date) : null;
    }
	
	public static Date toDate(String date){
	    try {
	        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            return (null != date) ? df.parse(date) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	public static Date toDate(String date, String format){
        try {
            DateFormat df = new SimpleDateFormat(format);
            return (null != date) ? df.parse(date) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static Date timestampToDate(String timestamp){
		long _timestamp = Long.valueOf(timestamp);
		if(StringUtils.length(timestamp) == 10){
			_timestamp = _timestamp * 1000;
		}
		return new Date(_timestamp);
	}


	public final static String getCoinPairKey(String baseCoin, String tradeCoin){
		return baseCoin + "/" + tradeCoin;
	}

	public final static long zoomInToLong(BigDecimal bigDecimal){
		return bigDecimal.multiply(SystemConstants.DEFAULT_MULTIPLIER).longValue();
	}
	public final static BigDecimal zoomOutToBigDecimal(long number){
		return new BigDecimal(number).divide(SystemConstants.DEFAULT_MULTIPLIER, SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_UP);
	}

	public final static long zoomInToLong(BigDecimal bigDecimal, long multiplier){
		return bigDecimal.multiply(new BigDecimal(multiplier)).longValue();
	}

	public final static String toString(BigDecimal bigDecimal, int precision){
		if(bigDecimal==null){
			return null;
		}
		return bigDecimal.setScale(precision, BigDecimal.ROUND_FLOOR).toPlainString();
	}

	public final static String toString(BigDecimal bigDecimal){
		if(bigDecimal==null){
			return null;
		}
		return bigDecimal.setScale(SystemConstants.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	public static String toString10(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			bigDecimal = BigDecimal.ZERO;
		}
		return bigDecimal.setScale(10, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	public final static BigDecimal toBigDecimal(String number){
		return StringUtils.isBlank(number)? BigDecimal.ZERO : new BigDecimal(number);
	}


	public static String getCurrentGMTString () {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.CHINESE);
		return DateUtil.format("yyyy-MM-dd HH:mm:ss '(GMT+8)'", cal.getTime());
	}
}
