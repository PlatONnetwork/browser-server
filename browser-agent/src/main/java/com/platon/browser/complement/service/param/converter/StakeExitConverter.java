package com.platon.browser.complement.service.param.converter;

import com.platon.browser.complement.dao.param.stake.StakeExit;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeExitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description: 退出质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class StakeExitConverter extends BusinessParamConverter<Optional<NodeOpt>> {

    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
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
