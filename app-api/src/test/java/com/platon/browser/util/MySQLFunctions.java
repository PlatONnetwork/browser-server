package com.platon.browser.util;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/22 13:53
 * @Description:
 */
public class MySQLFunctions {
    public static long unixTimestamp(java.sql.Timestamp timestamp) {
        return timestamp.getTime() / 1000L;
    }
}
