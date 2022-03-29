package com.platon.browser.utils;

import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskUtil {

    /**
     * 打印日志
     *
     * @param template:
     * @param params:
     * @return: void
     * @date: 2021/12/14
     */
    public static void console(CharSequence template, Object... params) {
        String msg = StrUtil.format(template, params);
        XxlJobHelper.log(msg);
        log.info(msg);
    }

}
