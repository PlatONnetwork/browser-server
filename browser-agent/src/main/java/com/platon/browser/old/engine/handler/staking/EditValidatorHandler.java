package com.platon.browser.old.engine.handler.staking;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomAddress;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.old.engine.BlockChain;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.cache.NodeCache;
import com.platon.browser.old.engine.handler.EventContext;
import com.platon.browser.old.engine.handler.EventHandler;
import com.platon.browser.old.engine.stage.StakingStage;
import com.platon.browser.old.exception.BlockChainException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.EditValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 修改质押信息(编辑验证人)事件处理类
 */
//@Component
public class EditValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(EditValidatorHandler.class);
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private BlockChain bc;

    @Override
    public void handle(EventContext context) throws BlockChainException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        Map<String,String> nodeNameMap = cacheHolder.getNodeNameMap();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        CustomTransaction tx = context.getTransaction();

        // 获取交易入参
        EditValidatorParam param = tx.getTxParam(EditValidatorParam.class);

        //记录参数中的地址
        bc.createAddress(param.getBenefitAddress(), CustomAddress.TypeEnum.ACCOUNT);
        String msg  = JSON.toJSONString(param);
        logger.debug("修改质押信息(编辑验证人):{}", msg);
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithEditValidatorParam(param);
            stakingStage.modifyStaking(latestStaking,tx);
            //添加本次修改质押信息对应质押时的快高
            param.setPerNodeName(latestStaking.getStakingName());
            param.setBlockNumber(latestStaking.getStakingBlockNum().toString());
            tx.setTxInfo(JSON.toJSONString(param));
            // 更新节点名称映射缓存
            nodeNameMap.put(latestStaking.getNodeId(),latestStaking.getStakingName());
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
}
