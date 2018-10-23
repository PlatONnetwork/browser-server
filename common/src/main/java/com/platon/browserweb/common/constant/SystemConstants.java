package com.platon.browserweb.common.constant;

import java.math.BigDecimal;

/**
 * <p>SystemConstants.java</p>
 * <p/>
 * <p>Description:</p>
 * <p/>
 * <p>Copyright (c) 2017. juzhen.io. All rights reserved.</p>
 * <p/>
 *
 * @version 1.0.0
 * @author: lvxiaoyi
 * <p/>
 * Revision History:
 * 2017/5/11, lvxiaoyi, Initial Version.
 */
public class SystemConstants {

    /**
     * 系统中，对于要用到日期的字符串形式的值时，统一用格式
     */
    public static final String DF_yyyyMMdd = "yyyyMMdd";

    public static final String DF_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    //缺省放大倍率（和缺省精度匹配，用户在redis存取累计值）
    public static final BigDecimal DEFAULT_MULTIPLIER = new BigDecimal(10000000000L);

    //缺省精度（小数点位数）
    public static final int DEFAULT_PRECISION = 10;

 }
