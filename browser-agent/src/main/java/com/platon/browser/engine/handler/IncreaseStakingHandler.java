package com.platon.browser.engine.handler;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.IncreaseStakingParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 增持质押(增加自有质押)事件处理类
 */
@Component
public class IncreaseStakingHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(IncreaseStakingHandler.class);

    @Override
    public void handle(EventContext context) {
        CustomTransaction tx = context.getTransaction();
        NodeCache nodeCache = context.getNodeCache();
        StakingStage stakingStage = context.getStakingStage();
        // 获取交易入参
        IncreaseStakingParam param = tx.getTxParam(IncreaseStakingParam.class);
        logger.debug("增持质押(增加自有质押):{}", JSON.toJSONString(param));
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithIncreaseStakingParam(param);
            stakingStage.updateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
}
