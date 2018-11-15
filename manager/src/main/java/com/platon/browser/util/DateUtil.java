package com.platon.browser.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
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
