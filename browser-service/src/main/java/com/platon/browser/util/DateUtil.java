package com.platon.browser.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理工具类
 *  @file DateUtil.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class DateUtil {
	/**
	 * 获取年的第一天
	 * @method getYearFirstDate
	 * @param date
	 * @return
	 */
    public static Date getYearFirstDate(Date date) {
        SimpleDateFormat y = new SimpleDateFormat("yyyy");
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        String year = y.format(date);
        String firstStr = year+"-01-01";
        Date firstDate = null;
        try {
            firstDate = ymd.parse(firstStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstDate;
    }
}
