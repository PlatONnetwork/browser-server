package com.platon.browser.util;

import java.math.BigInteger;

/**
 * 数据转换工具类
 * 
 * @file ConvertUtil.java
 * @description
 * @author zhangrj
 * @data 2019年8月31日
 */
public class ConvertUtil {
	private ConvertUtil() {
	}

	public static BigInteger hexToBigInteger(String hexStr) {
		hexStr = hexStr.replace("0x", "");
		return new BigInteger(hexStr, 16);
	}

	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}
}
