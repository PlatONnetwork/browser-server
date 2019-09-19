package com.platon.browser.engine.handler;

import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.exception.SettleEpochChangeException;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:11
 * @Description: 事件处理器接口
 */
public interface EventHandler {
    void handle(EventContext context) throws NoSuchBeanException, CandidateException, SettleEpochChangeException, BusinessException;
}
