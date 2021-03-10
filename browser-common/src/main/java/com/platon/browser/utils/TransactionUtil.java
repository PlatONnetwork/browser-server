package com.platon.browser.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 交易工具类
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/3/10
 */
public class TransactionUtil {

    /**
     * 自定义的段
     */
    public static final String CUSTOM_TX_HASH = "-0";

    /**
     * 删除交易hash自定义的段
     *
     * @param txhash 交易hash
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/3/10
     */
    public static String delCustomTXHash(String txhash) {
        if (StringUtils.isNotBlank(txhash) && txhash.endsWith(CUSTOM_TX_HASH)) {
            return StringUtils.removeEnd(txhash, CUSTOM_TX_HASH);
        } else {
            return txhash;
        }
    }

    public static void main(String[] args) {
        System.out.println("打印结果为：" + TransactionUtil.delCustomTXHash("sfqwefwq-0"));
    }

}
