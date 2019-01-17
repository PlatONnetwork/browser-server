package com.platon.browser.util;

public class EnergonUtil {
    private final static String PATTERN = ".##################";
    public static String convert(Object number){
        String result = NumberUtil.format(number,PATTERN);
        if(".0".equals(result)) {
            return "0";
        }else
        if(result.endsWith(".0")) {
            return result.replace(".0","");
        } else
        if(result.startsWith(".")) {
            return "0"+result;
        }
        return result;
    }
}
