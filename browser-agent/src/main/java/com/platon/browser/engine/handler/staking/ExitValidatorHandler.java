package com.platon.browser.engine.handler.staking;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.ExitValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 撤销质押(退出验证人)事件处理类
 */
@Component
public class ExitValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ExitValidatorHandler.class);

    @Override
    public void handle(EventContext context) {
        CustomTransaction tx = context.getTransaction();
        StakingStage stakingStage = context.getStakingStage();
        // 获取交易入参
        ExitValidatorParam param = tx.getTxParam(ExitValidatorParam.class);
        logger.debug("撤销质押(退出验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = NODE_CACHE.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithExitValidatorParam(param,context.getBlockChain().getCurSettingEpoch());
            stakingStage.updateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
}
