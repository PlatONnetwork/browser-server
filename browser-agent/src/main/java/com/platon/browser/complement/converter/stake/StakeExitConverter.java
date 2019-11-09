package com.platon.browser.complement.converter.stake;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeExit;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeExitParam;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 退出质押业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeExitConverter extends BusinessParamConverter<Optional<NodeOpt>> {

    @Autowired
    private StakeBusinessMapper stakeBusinessMapper;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) throws Exception {
        long startTime = System.currentTimeMillis();
        
        // 获得参数
        StakeExitParam txParam = tx.getTxParam(StakeExitParam.class);
        String nodeId = txParam.getNodeId();
        NodeItem nodeItem = nodeCache.getNode(nodeId);
        String nodeName = nodeItem.getNodeName();
        BigInteger stakingBlockNum = nodeItem.getStakingBlockNum();
        
        // 撤销质押
        StakeExit businessParam= StakeExit.builder()
        		.nodeId(nodeId)
        		.stakingBlockNum(stakingBlockNum)
        		.time(tx.getTime())
                .stakingReductionEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .build();
        
        // 查询质押金额
        BigDecimal stakingValue = stakeBusinessMapper.queryStakingValue(businessParam);
        
        // 质押撤销
        stakeBusinessMapper.exit(businessParam);
        
        // 补充txInfo
        txParam.setNodeName(nodeName);
        txParam.setStakingBlockNum(stakingBlockNum);
        txParam.setAmount(stakingValue);
        tx.setInfo(txParam.toJSONString());
    
        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(null);
    }
}
