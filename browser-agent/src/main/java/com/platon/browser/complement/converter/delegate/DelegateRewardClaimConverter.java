package com.platon.browser.complement.converter.delegate;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateRewardClaim;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateRewardClaimParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 领取委托奖励业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2020-01-02 14:40:10
 **/
@Slf4j
@Service
public class DelegateRewardClaimConverter extends BusinessParamConverter<DelegationReward> {
	
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
    @Autowired
    private AddressCache addressCache;

    @Override
    public DelegationReward convert(CollectionEvent event, Transaction tx) {
        // 发起委托
        DelegateRewardClaimParam txParam = tx.getTxParam(DelegateRewardClaimParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        DelegateRewardClaim businessParam= DelegateRewardClaim.builder()
                .address(tx.getFrom()) // 领取者地址
                .rewardList(txParam.getRewardList()) // 领取的奖励列表
                .build();

        delegateBusinessMapper.claim(businessParam);

        DelegationReward delegationReward = new DelegationReward();
        delegationReward.setHash(tx.getHash());
        delegationReward.setAddr(tx.getFrom());
        delegationReward.setTime(tx.getTime());
        delegationReward.setCreTime(new Date());
        delegationReward.setUpdTime(new Date());

        List<DelegationReward.Extra> extraList = new ArrayList<>();
        businessParam.getRewardList().forEach(reward -> {
            DelegationReward.Extra extra = new DelegationReward.Extra();
            extra.setNodeId(reward.getNodeId());
            extra.setNodeName(reward.getNodeName());
            extra.setReward(reward.getReward().toString());
            extraList.add(extra);
        });
        delegationReward.setExtra(JSON.toJSONString(extraList));

        addressCache.update(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return delegationReward;
    }
}
