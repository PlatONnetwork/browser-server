package com.platon.browser.utils;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 15:36
 * @Description:
 */
public class HexTool {
    private HexTool(){}
    /**
     * 为十六进制字符串添加"0x"前缀
     * @param hexVal
     * @return
     */
    public static String prefix(String hexVal){
        if(hexVal.startsWith("0x")) return hexVal;
        return "0x"+hexVal;
    }
}
