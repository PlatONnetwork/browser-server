package com.platon.browser.complement.converter.delegate;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.queue.gasestimate.event.ActionEnum;
import com.platon.browser.common.queue.gasestimate.event.GasEstimateEpoch;
import com.platon.browser.common.queue.gasestimate.event.GasEstimateEvent;
import com.platon.browser.common.queue.gasestimate.publisher.GasEstimateEventPublisher;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateCreate;
import com.platon.browser.dao.entity.GasEstimateLog;
import com.platon.browser.dao.mapper.CustomGasEstimateLogMapper;
import com.platon.browser.dao.mapper.GasEstimateLogMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateCreateParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class DelegateCreateConverter extends BusinessParamConverter<DelegateCreate> {
	
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;

    @Autowired
    private GasEstimateEventPublisher gasEstimateEventPublisher;

    @Autowired
    private CustomGasEstimateLogMapper customGasEstimateLogMapper;

    @Override
    public DelegateCreate convert(CollectionEvent event, Transaction tx) {
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

        // gas price估算数据信息
        List<GasEstimateEpoch> epoches = new ArrayList<>();
        GasEstimateEpoch epoch = GasEstimateEpoch.builder()
                .addr(tx.getFrom())
                .nodeId(txParam.getNodeId())
                .sbn(txParam.getStakingBlockNum().toString())
                .epoch(0L)
                .build();
        epoches.add(epoch);
        // 消息唯一序号
        Long seq = tx.getNum()*10000+tx.getIndex();

        // 入库mysql
        List<GasEstimateLog> gasEstimateLogs = new ArrayList<>();
        GasEstimateLog gsl = new GasEstimateLog();
        gsl.setSeq(seq);
        gsl.setJson(JSON.toJSONString(epoches));
        gasEstimateLogs.add(gsl);
        customGasEstimateLogMapper.batchInsertOrUpdateSelective(gasEstimateLogs, GasEstimateLog.Column.values());

        // 发布至ES入库队列
        gasEstimateEventPublisher.publish(seq,ActionEnum.ADD,epoches);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
