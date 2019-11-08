package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.delegate.DelegateCreate;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.persistence.dao.mapper.DelegateBusinessMapper;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class DelegateCreateConverter extends BusinessParamConverter<DelegateCreate> {
	
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;

    @Override
    public DelegateCreate convert(CollectionEvent event, CollectionTransaction tx) {
        DelegateCreateParam txParam = tx.getTxParam(DelegateCreateParam.class);

        DelegateCreate businessParam= DelegateCreate.builder()
        		.nodeId(txParam.getNodeId())
        		.amount(new BigDecimal(txParam.getAmount()))
        		.blockNumber(BigInteger.valueOf(tx.getNum()))
        		.txFrom(tx.getFrom())
        		.sequence(BigInteger.valueOf(tx.getSeq()))
        		.stakingBlockNumber(txParam.getStakingBlockNum())
                .build();
        
        delegateBusinessMapper.create(businessParam);
        return businessParam;
    }
}
