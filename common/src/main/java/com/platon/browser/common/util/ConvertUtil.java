package com.platon.browser.common.util;

import java.math.BigInteger;

public class ConvertUtil {
    public static BigInteger hexToBigInteger(String hexStr){
        hexStr = hexStr.replace("0x","");
        return new BigInteger(hexStr,16);
    }
}
