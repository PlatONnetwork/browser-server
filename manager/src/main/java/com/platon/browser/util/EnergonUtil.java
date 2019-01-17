package com.platon.browser.util;

public class EnergonUtil {
    private final static String PATTERN = ".##################";
    public static String convert(Object number){
        String result = NumberUtil.format(number,PATTERN);
        if("0.0".equals(result)) return "0";
        if(result.startsWith(".")) result = "0"+result;
        return result;
    }
}
