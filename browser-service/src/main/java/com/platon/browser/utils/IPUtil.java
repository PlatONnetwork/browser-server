package com.platon.browser.utils;

import java.util.regex.Pattern;

/**
 * 区分ip的类型工具类
 *  @file IPUtil.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class IPUtil {
    private IPUtil() {}
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    /**
     * 判断是否ipv4地址
     * @method isIPv4Address
     * @param input
     * @return
     */
    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * 判断是否ipv6地址(输入为十进制)
     * @method isIPv6StdAddress
     * @param input
     * @return
     */
    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    /**
     * 判断是否ipv6地址(输入为十六进制)
     * @method isIPv6HexCompressedAddress
     * @param input
     * @return
     */
    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    /**
     * 判断是否ipv6地址
     * @method isIPv6HexCompressedAddress
     * @param input
     * @return
     */
    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }
}
