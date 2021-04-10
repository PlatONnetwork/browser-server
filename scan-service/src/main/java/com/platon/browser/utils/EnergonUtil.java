package com.platon.browser.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数额转换工具类
 *  @file EnergonUtil.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class EnergonUtil {
    private EnergonUtil(){}
	/** 默认精度数 */
    private static final int DEFAULT_SHARP_NUM = 8;
    public static String format(Object number){
        return format(number,DEFAULT_SHARP_NUM);
    }
    /**
     * 数额转换
     * @method format
     * @param number
     * @param sharpNum
     * @return
     */
    public static String format(Object number,Integer sharpNum){
        if(!(number instanceof Number)) throw new NumberFormatException("The param is not a Number!");
        DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance();
        StringBuilder pattern = new StringBuilder(".");
        for(int i=0;i<sharpNum;i++) pattern.append("#");
        nf.applyPattern(pattern.toString());
        String result = nf.format(number);
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
