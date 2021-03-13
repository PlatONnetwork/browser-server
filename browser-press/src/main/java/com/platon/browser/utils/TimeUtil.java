package com.platon.browser.utils;

public class TimeUtil {

    public static String getTime(long lastTime, long startTime, long endTime) {
        long h = (endTime - startTime + lastTime) / 1000 / 60 / 60;
        long m = ((endTime - startTime + lastTime) / 1000 / 60) % 60;
        return h + "h:" + m + "m";
    }

    public static long resolving(String time) {
        String[] times = time.split(":");
        String h = times[0].substring(0, times[0].length() - 1);
        String m = times[1].substring(0, times[1].length() - 1);
        System.out.println("打印结果为：" + h + "   " + m);
        long hh = new Long(h) * 60 * 60 * 1000;
        long mm = new Long(m) * 60 * 1000;
        return hh + mm;
    }

    public static void main(String[] args) {
        // System.out.println("打印结果为：" + TimeUtil.getTime(98, 10000000));
        System.out.println("打印结果为：" + TimeUtil.resolving("12h:10m"));
    }

}
