package com.platon.browser.utils;

import com.platon.bech32.Bech32;

/**
 * 地址工具类
 *
 * @date 2021/2/9
 */
public class AddressUtil {

    /**
     * 0地址
     */
    public static final String TO_ADDR_ZERO = "0x0000000000000000000000000000000000000000";

    /**
     * 判断地址是否是0地址
     *
     * @param addr
     * @return boolean
     * @date 2021/2/9
     */
    public static boolean isAddrZero(String addr) {
        if (TO_ADDR_ZERO.equalsIgnoreCase(addr)) {
            return true;
        }
        String address = Bech32.addressDecodeHex(addr);
        if (TO_ADDR_ZERO.equalsIgnoreCase(address)) {
            return true;
        }
        return false;
    }

    /**
     * 判断from或to是否为0地址，只要其中一个为0地址则为true
     *
     * @param from
     * @param to
     * @return boolean
     * @date 2021/2/9
     */
    public static boolean isAddrZero(String from, String to) {
        return isAddrZero(from) || isAddrZero(to);
    }

}
