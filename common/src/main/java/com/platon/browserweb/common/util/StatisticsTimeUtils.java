package com.platon.browserweb.common.util;

import com.platon.browserweb.common.enums.KlineType;
import com.platon.browserweb.common.enums.StatisticsType;

import java.util.Calendar;
import java.util.Date;

/**
 * User: dongqile
 * Date: 2018/8/31
 * Time: 16:37
 */
public class StatisticsTimeUtils {

    public static Date getStatisticsTime( StatisticsType statisticsType, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int[] zeros = statisticsType.getZeros();
        for (int zero : zeros) {
            c.set(zero, 0);
        }

        if (KlineType.WEEKLY.equals(statisticsType)) {
            c.add(statisticsType.getIdentify(), 2 - c.get(statisticsType.getIdentify()));
        } else if (statisticsType.getCurrentDiff() != 0) {
            int diff = c.get(statisticsType.getIdentify());
            c.set(statisticsType.getIdentify(), diff / statisticsType.getCurrentDiff() * statisticsType.getCurrentDiff());
        }
        return c.getTime();
    }

    public static void main(String args[]){
        Date date = new Date();
        Date newDate = getStatisticsTime(StatisticsType.MIN_5_COUNT,date);
        System.out.println(newDate);
    }
}