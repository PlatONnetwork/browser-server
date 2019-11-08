package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.param.delegate.DelegateExit;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.param.DelegateExitParam;
import com.platon.browser.complement.mapper.DelegateBusinessMapper;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class DelegateExitConverter extends BusinessParamConverter<DelegateExit> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
	
    @Override
    public DelegateExit convert(CollectionEvent event, CollectionTransaction tx) {
    	DelegateExitParam txParam = tx.getTxParam(DelegateExitParam.class);

        DelegateExit businessParam= DelegateExit.builder()
        		.nodeId(txParam.getNodeId())
        		.amount(new BigDecimal(txParam.getAmount()))
        		.blockNumber(BigInteger.valueOf(tx.getNum()))
        		.txFrom(tx.getFrom())
        		.stakingBlockNumber(txParam.getStakingBlockNum())
        		.minimumThreshold(chainConfig.getDelegateThreshold())
                .build();
        
        delegateBusinessMapper.exit(businessParam);
        return businessParam;
    }
}
