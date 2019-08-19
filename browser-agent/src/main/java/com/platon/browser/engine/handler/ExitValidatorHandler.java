package com.platon.browser.engine.handler;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.result.StakingExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateValidatorParam;
import com.platon.browser.param.ExitValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 撤销质押(退出验证人)事件处理类
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description:
 */
@Component
public class ExitValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ExitValidatorHandler.class);

    @Override
    public void handle(EventContext context) {
        CustomTransaction tx = context.getTransaction();
        NodeCache nodeCache = context.getNodeCache();
        StakingExecuteResult executeResult = context.getExecuteResult();
        // 获取交易入参
        ExitValidatorParam param = tx.getTxParam(ExitValidatorParam.class);
        logger.debug("撤销质押(退出验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithExitValidatorParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
}
