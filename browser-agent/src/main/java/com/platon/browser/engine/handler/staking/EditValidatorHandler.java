package com.platon.browser.engine.handler.staking;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.EditValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.NODE_NAME_MAP;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 修改质押信息(编辑验证人)事件处理类
 */
@Component
public class EditValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(EditValidatorHandler.class);

    @Override
    public void handle(EventContext context) {
        CustomTransaction tx = context.getTransaction();
        StakingStage stakingStage = context.getStakingStage();
        // 获取交易入参
        EditValidatorParam param = tx.getTxParam(EditValidatorParam.class);
        logger.debug("修改质押信息(编辑验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = NODE_CACHE.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithEditValidatorParam(param);
            stakingStage.modifyStaking(latestStaking,tx);
            //添加本次修改质押信息对应质押时的快高
            param.setPerNodeName(latestStaking.getStakingName());
            param.setBlockNumber(latestStaking.getStakingBlockNum().toString());
            tx.setTxInfo(JSON.toJSONString(param));
            // 更新节点名称映射缓存
            NODE_NAME_MAP.put(latestStaking.getNodeId(),latestStaking.getStakingName());
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
}
