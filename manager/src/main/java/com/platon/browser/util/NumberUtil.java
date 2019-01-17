package com.platon.browser.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
    public static String format(Object data,String pattern){
        DecimalFormat NF = (DecimalFormat) NumberFormat.getInstance();
        NF.applyPattern(pattern);
        return NF.format(data);
    }
}
