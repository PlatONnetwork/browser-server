package com.platon.browser.utils;

public class TimeUtil {

    public static String getTime(long startTime, long endTime) {
        long h = (endTime - startTime) / 1000 / 60 / 60;
        long m = ((endTime - startTime) / 1000 / 60) % 60;
        return h + "h:" + m + "m";
    }

    public static void main(String[] args) {
        System.out.println("打印结果为：" + TimeUtil.getTime(98, 10000000));
    }

}
