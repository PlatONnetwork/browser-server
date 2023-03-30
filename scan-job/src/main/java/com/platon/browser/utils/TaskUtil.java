package com.platon.browser.utils;

import cn.hutool.core.util.StrUtil;
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
        // lvxiaoyi: 没有配置是否输出信息到 xxljob，这里都屏蔽掉
        // XxlJobHelper.log(msg);
        log.debug(msg);
    }

}
