package com.platon.browser.complement.converter.proposal;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.VersionDeclareParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class VersionDeclareConverter extends BusinessParamConverter<Optional<NodeOpt>> {
	
    @Autowired
    private NetworkStatCache networkStatCache;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
        VersionDeclareParam txParam = tx.getTxParam(VersionDeclareParam.class);
        // 补充节点名称
        String nodeId=txParam.getActiveNode();
        try {
            NodeItem nodeItem = nodeCache.getNode(nodeId);
            txParam.setNodeName(nodeItem.getNodeName());
            tx.setInfo(txParam.toJSONString());
        } catch (NoSuchBeanException e) {
            log.warn("缓存中找不到节点[{}]信息,无法补节点名称",nodeId);
        }
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

        long startTime = System.currentTimeMillis();


        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(null);
    }
}
