package com.platon.browser.old.engine.handler;

import com.platon.browser.exception.*;
import com.platon.browser.old.exception.BlockChainException;
import com.platon.browser.old.exception.BusinessException;
import com.platon.browser.old.exception.CandidateException;
import com.platon.browser.old.exception.SettleEpochChangeException;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:11
 * @Description: 事件处理器接口
 */
public interface EventHandler {
    void handle(EventContext context) throws NoSuchBeanException, CandidateException, SettleEpochChangeException, BusinessException, BlockChainException, InterruptedException;
}
