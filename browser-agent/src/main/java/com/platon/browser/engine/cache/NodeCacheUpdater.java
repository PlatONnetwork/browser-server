package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/27 11:16
 * @Description: 节点缓存更新器
 */
@Component
public class NodeCacheUpdater {
    private static Logger logger = LoggerFactory.getLogger(NodeCacheUpdater.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private CacheHolder cacheHolder;
    /**
     * 更新node表中的节点出块数信息: stat_block_qty, 由blockChain.execute()调用
     * 添加量
     */
    public void updateStatBlockQty() throws NoSuchBeanException {
        NodeCache nodeCache=cacheHolder.getNodeCache();
        BlockChainStage stageData = cacheHolder.getStageData();
    	logger.debug("NodeCacheUpdater updateStatBlockQty");
        CustomBlock curBlock = bc.getCurBlock();
        CustomNode node = nodeCache.getNode(curBlock.getNodeId());
        node.setStatBlockQty(node.getStatBlockQty()+1);
        node.setStatRewardValue(new BigDecimal(node.getStatRewardValue()).add(bc.getBlockReward()).setScale(0, RoundingMode.FLOOR).toString());
        stageData.getStakingStage().updateNode(node);
    }
}
