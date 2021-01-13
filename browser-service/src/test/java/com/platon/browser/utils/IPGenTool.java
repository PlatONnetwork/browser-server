package com.platon.browser.utils;

import java.util.Random;

public class IPGenTool {
    public static String getForeignIp(){
        int a = 108 * 256 * 256 + 1 * 256 + 5;
        int b = 145 * 256 * 256 + 110 * 256 + 35;
        int c = new Random().nextInt(b - a) + a;
        String ip = "192." + (c / (256 * 256)) + "." + ((c / 256) % 256) + "." + (c % 256);
        return ip;
    }

    public static String getChinaIp() {
        int[][] range = {
                { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
                { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
                { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
                { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
                { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
                { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
                { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
                { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
                { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
                { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
        };
        Random random = new Random();
        int index = random.nextInt(10);
        int data = range[index][0]+random.nextInt(range[index][1]-range[index][0]);
        int[] b = new int[]{((data >> 24) & 0xff),((data >> 16) & 0xff),((data >> 8) & 0xff),(data & 0xff)};
        return b[0]+"."+b[1]+"."+b[2]+"."+b[3];
    }

}
