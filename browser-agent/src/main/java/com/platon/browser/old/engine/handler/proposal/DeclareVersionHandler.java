package com.platon.browser.old.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.cache.NodeCache;
import com.platon.browser.old.engine.handler.EventContext;
import com.platon.browser.old.engine.handler.EventHandler;
import com.platon.browser.old.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DeclareVersionParam;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 声明版本事件处理类
 */
//@Component
public class DeclareVersionHandler implements EventHandler {
    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle ( EventContext context ) throws NoSuchBeanException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        CustomTransaction tx = context.getTransaction();
        DeclareVersionParam param = tx.getTxParam(DeclareVersionParam.class);
        try {
            CustomNode node = nodeCache.getNode(param.getActiveNode());
            String nodeName = node.getLatestStaking().getStakingName();
            param.setNodeName(nodeName);
            //交易txinfo回填
            tx.setTxInfo(JSON.toJSONString(param));

            // 记录操作日志
            CustomNodeOpt nodeOpt = new CustomNodeOpt(node.getNodeId(), CustomNodeOpt.TypeEnum.VERSION);
            nodeOpt.updateWithCustomTransaction(tx);
            String desc = CustomNodeOpt.TypeEnum.VERSION.getTpl()
                    .replace("NODE_NAME",param.getNodeName()==null?"":param.getNodeName())
                    .replace("VERSION",param.getVersion()==null?"":param.getVersion().toString())
                    .replace("ACTIVE_NODE", param.getActiveNode()==null?"":param.getActiveNode());
            nodeOpt.setDesc(desc);
            stakingStage.insertNodeOpt(nodeOpt);
        }catch (NoSuchBeanException e){
            throw new NoSuchBeanException("缓存中找不到对应的节点信息:"+e.getMessage());
        }
    }
}
