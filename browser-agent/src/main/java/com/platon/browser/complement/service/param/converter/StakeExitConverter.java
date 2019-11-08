package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.stake.StakeExit;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.StakeBusinessMapper;
import com.platon.browser.param.StakeExitParam;

/**
 * @description: 退出质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeExitConverter extends BusinessParamConverter<Optional<ComplementNodeOpt>> {

    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
	
    @Override
    public Optional<ComplementNodeOpt> convert(CollectionEvent event, CollectionTransaction tx) {
        // 撤销质押
        StakeExitParam txParam = tx.getTxParam(StakeExitParam.class);
        StakeExit businessParam= StakeExit.builder()
        		.nodeId(txParam.getNodeId())
        		.stakingBlockNum(txParam.getStakingBlockNum())
        		.time(tx.getTime())
                .stakingReductionEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .build();
        
        stakeBusinessMapper.exit(businessParam);
        
        return Optional.ofNullable(null);
    }
}
