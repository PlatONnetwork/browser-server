package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

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
    /**
     * 更新node表中的节点出块数信息: stat_block_qty, 由blockChain.execute()调用
     * 添加量
     */
    public void updateStatBlockQty() throws NoSuchBeanException {
    	logger.debug("NodeCacheUpdater updateStatBlockQty");
        CustomBlock curBlock = bc.getCurBlock();
        CustomNode node = NODE_CACHE.getNode(curBlock.getNodeId());
        node.setStatBlockQty(node.getStatBlockQty()+1);
        STAGE_DATA.getStakingStage().updateNode(node);
    }
}
