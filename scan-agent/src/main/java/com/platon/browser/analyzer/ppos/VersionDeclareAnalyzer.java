package com.platon.browser.analyzer.ppos;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.VersionDeclareParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class VersionDeclareAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private NetworkStatCache networkStatCache;

    /**
     * 版本声明分析
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        VersionDeclareParam txParam = tx.getTxParam(VersionDeclareParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        String nodeId = txParam.getActiveNode();
        String nodeName = txParam.getNodeName();

        String desc = NodeOpt.TypeEnum.VERSION.getTpl()
                .replace("NODE_NAME",nodeName)
                .replace("ACTIVE_NODE",nodeId)
                .replace("VERSION",String.valueOf(txParam.getVersion()));

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(nodeId);
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.VERSION.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(event.getBlock().getNum());
        nodeOpt.setTime(tx.getTime());

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return nodeOpt;
    }
}
