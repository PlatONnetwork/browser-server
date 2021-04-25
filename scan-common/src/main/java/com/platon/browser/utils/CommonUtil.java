package com.platon.browser.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

/**
 * 公共工具类
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/17
 */
public class CommonUtil {

    /**
     * 获取trace-id
     *
     * @param
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/17
     */
    public static String getTraceId() {
        return StrUtil.removeAll(UUID.randomUUID().toString(), "-");
    }

}
