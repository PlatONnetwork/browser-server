package com.platon.browser.analyzer.ppos;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.bean.DelegateExitResult;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.custommapper.CustomGasEstimateMapper;
import com.platon.browser.dao.custommapper.DelegateBusinessMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.GasEstimateMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.dao.param.ppos.DelegateExit;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DelegateExitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 撤销委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class DelegateExitAnalyzer extends PPOSAnalyzer<DelegateExitResult> {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private DelegateBusinessMapper delegateBusinessMapper;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private DelegationMapper delegationMapper;

    @Resource
    private AddressCache addressCache;

    @Resource
    private CustomGasEstimateMapper customGasEstimateMapper;

    @Resource
    private GasEstimateMapper gasEstimateMapper;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private CustomAddressMapper customAddressMapper;

    /**
     * 减持/撤销委托(赎回委托)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.bean.DelegateExitResult
     * @date 2021/6/15
     */
    @Override
    public DelegateExitResult analyze(CollectionEvent event, Transaction tx) {
        DelegateExitResult der = DelegateExitResult.builder().build();
        // 退出委托
        DelegateExitParam txParam = tx.getTxParam(DelegateExitParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus()) return der;

        long startTime = System.currentTimeMillis();

        // 查询出撤销委托交易对应的委托信息
        DelegationKey delegationKey = new DelegationKey();
        delegationKey.setDelegateAddr(tx.getFrom());
        delegationKey.setNodeId(txParam.getNodeId());
        delegationKey.setStakingBlockNum(txParam.getStakingBlockNum().longValue());
        Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);

        if (delegation == null) return der;
        // 查询出对应的节点信息
        StakingExample stakingExample = new StakingExample();
        stakingExample.createCriteria().andNodeIdEqualTo(delegation.getNodeId()).andStakingBlockNumEqualTo(delegation.getStakingBlockNum());
        List<Staking> stakings = stakingMapper.selectByExample(stakingExample);

        if (stakings.isEmpty()) throw new BusinessException("委托者:" + tx.getFrom() + "的质押节点:" + txParam.getNodeId() + "不存在");

        Staking staking = stakings.get(0);

        DelegateExit businessParam = DelegateExit.builder()
                                                 .nodeId(txParam.getNodeId())
                                                 .blockNumber(BigInteger.valueOf(tx.getNum()))
                                                 .txFrom(tx.getFrom())
                                                 .stakingBlockNumber(txParam.getStakingBlockNum())
                                                 .minimumThreshold(chainConfig.getDelegateThreshold())
                                                 .delegateReward(txParam.getReward() == null ? BigDecimal.ZERO : txParam.getReward())
                                                 .build();

        /**
         * 一、已退出 & 退出中的节点，其委记录的犹豫和锁定期金额全部已被移到待领取字段
         * 此时做赎回委托
         * 对委托表&节点表记录，从待领取字段扣除真实赎回金额 stat_delegate_released
         * 对质押表，从待领取字段扣除 stat_delegate_released
         *
         * 二、侯选中，先扣犹豫、不足后扣锁定
         * 犹豫够扣: 委托和质押表从stat_delegate_hes扣，节点表从stat_delegate_value扣
         * 犹豫不够扣: 委托和质押从stat_delegate_hes和stat_delegate_locked扣，节点表从stat_delegate_value扣
         */
        boolean isCandidate = true; // 对应节点是否候选中

        if (staking.getStatus() == CustomStaking.StatusEnum.EXITING.getCode() || staking.getStatus() == CustomStaking.StatusEnum.EXITED.getCode()) {
            // 节点是[退出中|已退出]
            businessParam.setCodeNodeIsLeave(true);
            isCandidate = false;
        }

        boolean isRefundAll = delegation.getDelegateHes() // 犹豫期金额
                                        .add(delegation.getDelegateLocked()) // +锁定期金额
                                        .add(delegation.getDelegateReleased()) // +待提取金额
                                        .subtract(txParam.getAmount()) // -申请退回金额
                                        .compareTo(chainConfig.getDelegateThreshold()) < 0; // 小于委托门槛
        // 计算真实退回金额
        BigDecimal realRefundAmount = txParam.getAmount();
        if (isRefundAll) {
            // 全部退回，委托置为历史
            realRefundAmount = delegation.getDelegateHes() // +犹豫期金额
                                         .add(delegation.getDelegateLocked()) // +锁定期金额
                                         .add(delegation.getDelegateReleased()); // +待提取金额
            businessParam.setCodeIsHistory(BusinessParam.YesNoEnum.YES.getCode()); // 委托状态置为历史
        } else {
            // 部分退回，委托置为非历史
            businessParam.setCodeIsHistory(BusinessParam.YesNoEnum.NO.getCode()); // 委托状态置为非历史
        }

        if (isCandidate) {
            // 候选中的节点
            if (delegation.getDelegateHes().compareTo(realRefundAmount) >= 0) {
                // 犹豫够扣: 委托和质押表从stat_delegate_hes扣，节点表从stat_delegate_value扣
                BigDecimal remainDelegateHes = delegation.getDelegateHes().subtract(realRefundAmount);
                // 委托记录本身的金额变动
                businessParam.getBalance().setDelegateHes(remainDelegateHes).setDelegateLocked(delegation.getDelegateLocked()) // 锁定委托金额保持不变
                             .setDelegateReleased(delegation.getDelegateReleased()); //待领取金额不变
                // 委托对应节点或质押应减掉的金额
                businessParam.getDecrease().setDelegateHes(realRefundAmount) // -真实扣除金额
                             .setDelegateLocked(BigDecimal.ZERO) // -0
                             .setDelegateReleased(BigDecimal.ZERO); // -0
            } else {
                // 犹豫不够扣: 委托和质押从stat_delegate_hes和stat_delegate_locked扣，节点表从stat_delegate_value扣
                BigDecimal remainDelegateLocked = delegation.getDelegateLocked() // +锁定委托金额
                                                            .add(delegation.getDelegateHes()) // +犹豫期委托金额
                                                            .subtract(realRefundAmount); // -真实扣除金额
                if (remainDelegateLocked.compareTo(BigDecimal.ZERO) < 0) remainDelegateLocked = BigDecimal.ZERO;
                // 委托记录本身的金额变动
                businessParam.getBalance().setDelegateHes(BigDecimal.ZERO) // 犹豫期金额置0
                             .setDelegateLocked(remainDelegateLocked) // 锁定金额+犹豫金额-真实扣除金额
                             .setDelegateReleased(delegation.getDelegateReleased()); //待领取金额不变
                // 委托对应节点或质押应减掉的金额
                businessParam.getDecrease().setDelegateHes(BigDecimal.ZERO) // -0
                             .setDelegateLocked(realRefundAmount) // -真实扣除金额
                             .setDelegateReleased(delegation.getDelegateReleased()); //待领取金额不变
            }
        } else {
            //退出中或已退出的节点
            // 对委托表&节点表记录，从待领取字段扣除真实赎回金额 stat_delegate_released
            // 对质押表，从待领取字段扣除 stat_delegate_released
            BigDecimal delegateReleasedBalance = delegation.getDelegateReleased().subtract(realRefundAmount);
            if (delegateReleasedBalance.compareTo(BigDecimal.ZERO) < 0) delegateReleasedBalance = BigDecimal.ZERO;
            businessParam.getBalance().setDelegateReleased(delegateReleasedBalance);
            businessParam.getBalance().setDelegateHes(BigDecimal.ZERO);
            businessParam.getBalance().setDelegateLocked(BigDecimal.ZERO);

            businessParam.getDecrease().setDelegateReleased(realRefundAmount);
            businessParam.getDecrease().setDelegateHes(BigDecimal.ZERO);
            businessParam.getDecrease().setDelegateLocked(BigDecimal.ZERO);
        }

        // 补充真实退款金额
        txParam.setRealAmount(realRefundAmount);
        tx.setInfo(txParam.toJSONString());

        businessParam.setRealRefundAmount(realRefundAmount);
        delegateBusinessMapper.exit(businessParam);

        der.setDelegateExit(businessParam);

        if (txParam.getReward().compareTo(BigDecimal.ZERO) > 0) {
            // 如果委托奖励为0，则无需记录领取记录
            DelegationReward delegationReward = new DelegationReward();
            delegationReward.setHash(tx.getHash());
            delegationReward.setAddr(tx.getFrom());
            delegationReward.setTime(tx.getTime());
            delegationReward.setCreTime(new Date());
            delegationReward.setUpdTime(new Date());

            List<DelegationReward.Extra> extraList = new ArrayList<>();
            DelegationReward.Extra extra = new DelegationReward.Extra();
            extra.setNodeId(businessParam.getNodeId());
            String nodeName = "Unknown";
            try {
                nodeName = nodeCache.getNode(businessParam.getNodeId()).getNodeName();
            } catch (NoSuchBeanException e) {
                log.error("{}", e.getMessage());
            }
            extra.setNodeName(nodeName);
            extra.setReward(txParam.getReward().toString());
            extraList.add(extra);
            delegationReward.setExtra(JSON.toJSONString(extraList));

            List<DelegationReward.Extra> extraCleanList = new ArrayList<>();
            if (extra.decimalReward().compareTo(BigDecimal.ZERO) > 0) {
                extraCleanList.add(extra);
            }
            delegationReward.setExtraClean(JSON.toJSONString(extraCleanList));

            der.setDelegationReward(delegationReward);
        }

        updateAddressHaveReward(businessParam);

        if (isRefundAll) {
            // 1. 全部赎回： 删除对应记录
            GasEstimateKey gek = new GasEstimateKey();
            gek.setNodeId(txParam.getNodeId());
            gek.setAddr(tx.getFrom());
            gek.setSbn(txParam.getStakingBlockNum().longValue());
            gasEstimateMapper.deleteByPrimaryKey(gek);
        } else {
            // 2. 部分赎回：1. 新增 委托未计算周期  记录， epoch = 0
            List<GasEstimate> estimates = new ArrayList<>();
            GasEstimate estimate = new GasEstimate();
            estimate.setNodeId(txParam.getNodeId());
            estimate.setSbn(txParam.getStakingBlockNum().longValue());
            estimate.setAddr(tx.getFrom());
            estimate.setEpoch(0L);
            estimates.add(estimate);
            customGasEstimateMapper.batchInsertOrUpdateSelective(estimates, GasEstimate.Column.values());
        }

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        return der;
    }

    /**
     * 更新地址已领取委托奖励
     *
     * @param businessParam:
     * @return: void
     * @date: 2021/12/2
     */
    private void updateAddressHaveReward(DelegateExit businessParam) {
        Address addressInfo = addressMapper.selectByPrimaryKey(businessParam.getTxFrom());
        if (ObjectUtil.isNull(addressInfo)) {
            // db不存在则在缓存中创建一个新的地址，并更新已领取委托奖励
            Address address = addressCache.createDefaultAddress(businessParam.getTxFrom());
            address.setHaveReward(businessParam.getDelegateReward());
            addressMapper.insertSelective(address);
        } else {
            customAddressMapper.updateAddressHaveReward(businessParam.getTxFrom(), businessParam.getDelegateReward());
        }
    }

}
