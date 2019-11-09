package com.platon.browser.complement.converter.delegate;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateCreate;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateCreateParam;
import lombok.extern.slf4j.Slf4j;
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
    public DelegateCreate convert(CollectionEvent event, Transaction tx) {
        long startTime = System.currentTimeMillis();

        DelegateCreateParam txParam = tx.getTxParam(DelegateCreateParam.class);

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
