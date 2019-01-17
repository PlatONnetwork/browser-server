package com.platon.browser.util;

public class EnergonUtil {
    private final static String PATTERN = ".##################";
    public static String convert(Object number){
        return NumberUtil.format(number,PATTERN);
    }
}
