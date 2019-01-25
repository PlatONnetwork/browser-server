package com.platon.browser.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EnergonUtil {
    private final static Integer DEFAULT_SHARP_NUM = 8;
    public static String format(Object number){
        return format(number,DEFAULT_SHARP_NUM);
    }
    public static String format(Object number,Integer sharpNum){
        if(!(number instanceof Number)) throw new RuntimeException("The param is not a Number!");
        DecimalFormat NF = (DecimalFormat) NumberFormat.getInstance();
        StringBuffer pattern = new StringBuffer(".");
        for(int i=0;i<sharpNum;i++) pattern.append("#");
        NF.applyPattern(pattern.toString());
        String result = NF.format(number);
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
