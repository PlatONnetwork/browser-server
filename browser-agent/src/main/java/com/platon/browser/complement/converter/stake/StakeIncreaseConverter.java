package com.platon.browser.complement.converter.stake;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeIncrease;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeIncreaseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description: 增持质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeIncreaseConverter extends BusinessParamConverter<Optional<NodeOpt>> {

    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
    
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
        // 增持质押
        StakeIncreaseParam txParam = tx.getTxParam(StakeIncreaseParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

        long startTime = System.currentTimeMillis();


        StakeIncrease businessParam= StakeIncrease.builder()        		
        		.nodeId(txParam.getNodeId())
        		.amount(txParam.getAmount())
        		.stakingBlockNum(txParam.getStakingBlockNum())
                .build();
        
        stakeBusinessMapper.increase(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
        nodeOpt.setNodeId(txParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.INCREASE.getCode()));
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(tx.getNum());
        nodeOpt.setTime(tx.getTime());


        return Optional.ofNullable(nodeOpt);
    }
}
