package com.platon.browser.analyzer.ppos;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.custommapper.CustomGasEstimateMapper;
import com.platon.browser.dao.custommapper.DelegateBusinessMapper;
import com.platon.browser.dao.param.ppos.DelegateCreate;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateCreateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class DelegateCreateAnalyzer extends PPOSAnalyzer<DelegateCreate> {
	
    @Resource
    private DelegateBusinessMapper delegateBusinessMapper;
    @Resource
    private CustomGasEstimateMapper customGasEstimateMapper;

    @Override
    public DelegateCreate analyze(CollectionEvent event, Transaction tx) {
        // 发起委托
        DelegateCreateParam txParam = tx.getTxParam(DelegateCreateParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        DelegateCreate businessParam= DelegateCreate.builder()
        		.nodeId(txParam.getNodeId())
        		.amount(txParam.getAmount())
        		.blockNumber(BigInteger.valueOf(tx.getNum()))
        		.txFrom(tx.getFrom())
        		.sequence(BigInteger.valueOf(tx.getSeq()))
        		.stakingBlockNumber(txParam.getStakingBlockNum())
                .build();

        delegateBusinessMapper.create(businessParam);

        // 1. 新增 估算gas委托未计算周期 epoch = 0: 直接入库到mysql数据库
        List<GasEstimate> estimates = new ArrayList<>();
        GasEstimate estimate = new GasEstimate();
        estimate.setNodeId(txParam.getNodeId());
        estimate.setSbn(txParam.getStakingBlockNum().longValue());
        estimate.setAddr(tx.getFrom());
        estimate.setEpoch(0L);
        estimates.add(estimate);
        customGasEstimateMapper.batchInsertOrUpdateSelective(estimates, GasEstimate.Column.values());

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
