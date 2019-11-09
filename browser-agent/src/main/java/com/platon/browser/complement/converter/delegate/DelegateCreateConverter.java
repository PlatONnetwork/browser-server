package com.platon.browser.complement.converter.delegate;

import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateCreate;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DelegateCreateParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class DelegateCreateConverter extends BusinessParamConverter<DelegateCreate> {
	
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;

    @Override
    public DelegateCreate convert(CollectionEvent event, Transaction tx) throws NoSuchBeanException {
        // 发起委托
        DelegateCreateParam txParam = tx.getTxParam(DelegateCreateParam.class);
        // 补充节点名称
        String nodeId=txParam.getNodeId();
        NodeItem nodeItem = nodeCache.getNode(nodeId);
        txParam.setNodeName(nodeItem.getNodeName());
        txParam.setStakingBlockNum(nodeItem.getStakingBlockNum());
        tx.setInfo(txParam.toJSONString());
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        DelegateCreate businessParam= DelegateCreate.builder()
        		.nodeId(txParam.getNodeId())
        		.amount(txParam.getAmount())
        		.blockNumber(BigInteger.valueOf(tx.getNum()))
        		.txFrom(tx.getFrom())
        		.sequence(BigInteger.valueOf(tx.getSeq()))
        		.stakingBlockNumber(txParam.getStakingBlockNum())
                .build();
        
        delegateBusinessMapper.create(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
