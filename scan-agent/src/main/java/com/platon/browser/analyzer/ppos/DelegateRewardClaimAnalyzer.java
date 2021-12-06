package com.platon.browser.analyzer.ppos;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomGasEstimateMapper;
import com.platon.browser.dao.custommapper.DelegateBusinessMapper;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.param.ppos.DelegateRewardClaim;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.param.claim.Reward;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 领取委托奖励业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2020-01-02 14:40:10
 **/
@Slf4j
@Service
public class DelegateRewardClaimAnalyzer extends PPOSAnalyzer<DelegationReward> {

    @Resource
    private DelegateBusinessMapper delegateBusinessMapper;

    @Resource
    private AddressCache addressCache;

    @Resource
    private CustomGasEstimateMapper customGasEstimateMapper;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    /**
     * 领取奖励
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.DelegationReward
     * @date 2021/6/15
     */
    @Override
    public DelegationReward analyze(CollectionEvent event, Transaction tx) {
        // 发起委托
        DelegateRewardClaimParam txParam = tx.getTxParam(DelegateRewardClaimParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();

        DelegateRewardClaim businessParam = DelegateRewardClaim.builder().address(tx.getFrom()) // 领取者地址
                                                               .rewardList(txParam.getRewardList()) // 领取的奖励列表
                                                               .build();

        delegateBusinessMapper.claim(businessParam);

        // 累计总的委托奖励
        BigDecimal txTotalReward = BigDecimal.ZERO;
        List<DelegationReward.Extra> extraList = new ArrayList<>();
        List<DelegationReward.Extra> extraCleanList = new ArrayList<>();

        // 1. 领取委托奖励 估算gas委托未计算周期 epoch = 0: 直接入库到mysql数据库
        List<GasEstimate> estimates = new ArrayList<>();
        for (Reward reward : businessParam.getRewardList()) {
            DelegationReward.Extra extra = new DelegationReward.Extra();
            extra.setNodeId(reward.getNodeId());
            extra.setNodeName(reward.getNodeName());
            extra.setReward(reward.getReward().toString());
            extraList.add(extra);
            if (extra.decimalReward().compareTo(BigDecimal.ZERO) > 0) {
                extraCleanList.add(extra);
            }
            txTotalReward = txTotalReward.add(reward.getReward());

            GasEstimate estimate = new GasEstimate();
            estimate.setNodeId(reward.getNodeId());
            estimate.setSbn(reward.getStakingNum().longValue());
            estimate.setAddr(tx.getFrom());
            estimate.setEpoch(0L);
            estimates.add(estimate);
        }

        DelegationReward delegationReward = null;
        if (txTotalReward.compareTo(BigDecimal.ZERO) > 0) {
            // 如果总奖励大于零，则记录领取明细
            delegationReward = new DelegationReward();
            delegationReward.setHash(tx.getHash());
            delegationReward.setAddr(tx.getFrom());
            delegationReward.setTime(tx.getTime());
            delegationReward.setCreTime(new Date());
            delegationReward.setUpdTime(new Date());
            delegationReward.setExtra(JSON.toJSONString(extraList));
            delegationReward.setExtraClean(JSON.toJSONString(extraCleanList));
        }

        updateAddressHaveReward(businessParam);

        // 直接入库到mysql数据库
        customGasEstimateMapper.batchInsertOrUpdateSelective(estimates, GasEstimate.Column.values());

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        return delegationReward;
    }

    /**
     * 更新地址已领取委托奖励
     *
     * @param businessParam:
     * @return: void
     * @date: 2021/12/2
     */
    private void updateAddressHaveReward(DelegateRewardClaim businessParam) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Reward reward : businessParam.getRewardList()) {
            totalAmount = totalAmount.add(reward.getReward());
        }
        Address addressInfo = addressMapper.selectByPrimaryKey(businessParam.getAddress());
        if (ObjectUtil.isNull(addressInfo)) {
            // db不存在则在缓存中创建一个新的地址，并更新已领取委托奖励
            Address address = addressCache.createDefaultAddress(businessParam.getAddress());
            address.setHaveReward(totalAmount);
            addressMapper.insertSelective(address);
        } else {
            customAddressMapper.updateAddressHaveReward(businessParam.getAddress(), totalAmount);
        }
    }

}
