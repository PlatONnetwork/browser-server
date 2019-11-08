package com.platon.browser.complement.converter.delegate;

import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateExitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class DelegateExitConverter extends BusinessParamConverter<DelegateExit> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
	
    @Override
    public DelegateExit convert(CollectionEvent event, Transaction tx) {
        long startTime = System.currentTimeMillis();

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

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
