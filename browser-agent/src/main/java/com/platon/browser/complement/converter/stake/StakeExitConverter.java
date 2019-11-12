package com.platon.browser.complement.converter.stake;

import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.StakeBusinessMapper;
import com.platon.browser.complement.dao.param.stake.StakeExit;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.StakeExitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
        // 撤销质押
        StakeExitParam txParam = tx.getTxParam(StakeExitParam.class);
        // 补充节点名称
        String nodeId=txParam.getNodeId();
        try {
            NodeItem nodeItem = nodeCache.getNode(nodeId);
            txParam.setNodeName(nodeItem.getNodeName());
            txParam.setStakingBlockNum(nodeItem.getStakingBlockNum());
            tx.setInfo(txParam.toJSONString());
        } catch (NoSuchBeanException e) {
            log.warn("缓存中找不到节点[{}]信息,无法补节点名称和质押区块号",nodeId);
        }
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

        long startTime = System.currentTimeMillis();

        // 撤销质押
        StakeExit businessParam= StakeExit.builder()
        		.nodeId(nodeId)
        		.stakingBlockNum(txParam.getStakingBlockNum())
        		.time(tx.getTime())
                .stakingReductionEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .build();
        
        // 查询质押金额
        BigDecimal stakingValue = stakeBusinessMapper.queryStakingValue(businessParam);
        
        // 质押撤销
        stakeBusinessMapper.exit(businessParam);
        
        // 补充txInfo
        txParam.setAmount(stakingValue);
        tx.setInfo(txParam.toJSONString());
    
        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(null);
    }
}
