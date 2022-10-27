package com.platon.browser.utils;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * jasypt加密工具类
 *
 * @date: 2022/7/6
 */
public class JasyptUtil {

    /**
     * 加密
     *
     * @param salt:    盐
     * @param encrypt: 需要加密的字符串
     * @return: java.lang.String
     * @date: 2022/7/6
     */
    public static String encryptor(String salt, String encrypt) {
        if (StrUtil.isBlank(salt)) {
            salt = "my123456";
        }
        if (StrUtil.isBlank(encrypt)) {
            return "";
        }
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(salt);
        return textEncryptor.encrypt(encrypt);
    }

    /**
     * 解密
     *
     * @param salt:    盐
     * @param encrypt: 需要解密的字符串
     * @return: java.lang.String
     * @date: 2022/7/6
     */
    public static String decryptor(String salt, String encrypt) {
        if (StrUtil.isBlank(salt)) {
            salt = "my123456";
        }
        if (StrUtil.isBlank(encrypt)) {
            return "";
        }
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(salt);
        return textEncryptor.decrypt(encrypt);
    }

    public static void main(String[] args) {
        Console.log("========{}", encryptor("", "platscan"));
        Console.log("========{}", encryptor("", "elastic"));
        Console.log("========{}", encryptor("", "changeme"));
        Console.log("========{}", encryptor("", "GOJ4hui834hGIhHIh33984dG3DER4Gh784u9dh"));
        Console.log("========{}", encryptor("", "f982e1a8f14444c7a484dc16bcad7741"));
    }

}
