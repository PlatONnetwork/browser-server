package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DeclareVersionParam;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 声明版本事件处理类
 */
@Component
public class DeclareVersionHandler implements EventHandler {
    @Override
    public void handle ( EventContext context ) throws NoSuchBeanException {
        CustomTransaction tx = context.getTransaction();
        DeclareVersionParam param = tx.getTxParam(DeclareVersionParam.class);
        try {
            String nodeName = NODE_CACHE.getNode(param.getActiveNode()).getLatestStaking().getStakingName();
            param.setNodeName(nodeName);
            //交易txinfo回填
            tx.setTxInfo(JSON.toJSONString(param));
        }catch (NoSuchBeanException e){
            throw new NoSuchBeanException("缓存中找不到对应的节点信息:"+e.getMessage());
        }
    }
}
