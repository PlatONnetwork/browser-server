package com.platon.browser.complement.converter.delegate;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateRewardClaim;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateRewardClaimParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @description: 领取委托奖励业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2020-01-02 14:40:10
 **/
@Slf4j
@Service
public class DelegateRewardClaimConverter extends BusinessParamConverter<DelegateRewardClaim> {
	
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;

    @Override
    public DelegateRewardClaim convert(CollectionEvent event, Transaction tx) {
        // 发起委托
        DelegateRewardClaimParam txParam = tx.getTxParam(DelegateRewardClaimParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        // TODO: 从交易中解析并抽取入库参数：更改address表、node表、staking表
        DelegateRewardClaim businessParam= DelegateRewardClaim.builder()
                .reward(txParam.getRewards())
                .build();

        delegateBusinessMapper.claim(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
