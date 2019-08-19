package com.platon.browser.engine.handler;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.result.StakingExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.EditValidatorParam;
import com.platon.browser.param.ReportValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 举报多签(举报验证人)事件处理类
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description:
 */
@Component
public class ReportValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ReportValidatorHandler.class);

    @Override
    public void handle(EventContext context) {
        CustomTransaction tx = context.getTransaction();
        NodeCache nodeCache = context.getNodeCache();
        StakingExecuteResult executeResult = context.getExecuteResult();
        // 获取交易入参
        ReportValidatorParam param = tx.getTxParam(ReportValidatorParam.class);
        logger.debug("举报多签(举报验证人):{}", JSON.toJSONString(param));

    }
}
