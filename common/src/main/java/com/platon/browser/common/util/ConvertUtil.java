package com.platon.browser.common.util;

public class ConvertUtil {
    public static Integer hexToInt(String hexStr){
        hexStr=hexStr.replace("0x","");
        return Integer.valueOf(hexStr,16);
    }
}
