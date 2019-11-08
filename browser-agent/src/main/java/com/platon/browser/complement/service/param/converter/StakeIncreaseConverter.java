package com.platon.browser.complement.service.param.converter;

import com.platon.browser.complement.dao.param.stake.StakeIncrease;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeIncreaseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @description: 增持质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeIncreaseConverter extends BusinessParamConverter<Optional<NodeOpt>> {

    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
        // 增持质押
        StakeIncreaseParam txParam = tx.getTxParam(StakeIncreaseParam.class);
        StakeIncrease businessParam= StakeIncrease.builder()        		
        		.nodeId(txParam.getNodeId())
        		.amount(new BigDecimal(txParam.getAmount()))
        		.stakingBlockNum(txParam.getStakingBlockNum())
                .build();
        
        stakeBusinessMapper.increase(businessParam);
        return Optional.ofNullable(null);
    }
}
