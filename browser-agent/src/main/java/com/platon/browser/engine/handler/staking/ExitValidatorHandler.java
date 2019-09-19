package com.platon.browser.engine.handler.staking;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.ExitValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 撤销质押(退出验证人)事件处理类
 */
@Component
public class ExitValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ExitValidatorHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle(EventContext context) {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        CustomTransaction tx = context.getTransaction();

        // 获取交易入参
        ExitValidatorParam param = tx.getTxParam(ExitValidatorParam.class);
        String msg = JSON.toJSONString(param);
        logger.debug("撤销质押(退出验证人):{}", msg);
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            param.setAmount(latestStaking.integerStakingHas().add(latestStaking.integerStakingLocked()).toString());
            latestStaking.updateWithExitValidatorParam(param,bc.getCurSettingEpoch());
            //交易info数据回填补充
            param.setNodeName(latestStaking.getStakingName());
            param.setStakingBlockNum(latestStaking.getStakingBlockNum().toString());
            String txinfo = JSON.toJSONString(param);
            tx.setTxInfo(txinfo);
            stakingStage.modifyStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
}
