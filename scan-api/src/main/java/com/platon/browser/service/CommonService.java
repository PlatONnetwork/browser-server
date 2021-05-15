package com.platon.browser.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.platon.browser.bean.EpochInfo;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.InternalAddress;
import com.platon.browser.dao.entity.InternalAddressExample;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.InternalAddressMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.utils.EpochUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 实现
 *
 * @author zhangrj
 * @file CommonServiceImpl.java
 * @description
 * @data 2019年11月11日
 */
@Slf4j
@Service
public class CommonService {

    @Resource
    private StatisticCacheService statisticCacheService;

    @Resource
    private CustomNodeMapper customNodeMapper;

    @Resource
    private BlockChainConfig blockChainConfig;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private InternalAddressMapper internalAddressMapper;

    public String getNodeName(String nodeId, String nodeName) {
        /**
         * 当nodeId为空或者nodeName不为空则直接返回name
         */
        if (StringUtils.isNotBlank(nodeName) || StringUtils.isBlank(nodeId)) {
            return nodeName;
        }
        return customNodeMapper.findNameById(nodeId);
    }

    /**
     * 获取总发行量
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    //@Cacheable(value = "getIssueValue")
    public BigDecimal getIssueValue() {
        BigDecimal issueValue = new BigDecimal(0);
        try {
            // 获取初始发行金额
            BigDecimal initIssueAmount = blockChainConfig.getInitIssueAmount();
            // 每年固定增发比例
            BigDecimal addIssueRate = blockChainConfig.getAddIssueRate();
            // 当前块高
            Long currentNumber = CommonUtil.ofNullable(() -> getBlockCurrentNumber()).orElse(0L);
            // 上一结算周期最后一个块号
            BigInteger preSettleEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(Convert.toBigInteger(currentNumber), blockChainConfig.getSettlePeriodBlockCount());
            // 从特殊接口获取
            EpochInfo epochInfo = specialApi.getEpochInfo(platOnClient.getWeb3jWrapper().getWeb3j(), preSettleEpochLastBlockNumber);
            issueValue = com.platon.utils.Convert.toVon(initIssueAmount, com.platon.utils.Convert.Unit.KPVON).multiply(addIssueRate.add(new BigDecimal(1L)).pow(epochInfo.getYearNum().intValue())).setScale(4, BigDecimal.ROUND_HALF_UP);
            if (issueValue.signum() == -1) {
                log.error("获取总发行量[{}]错误,不能为负数", issueValue.toString());
            }
        } catch (Exception e) {
            log.error("获取取总发行量异常", e);
        }
        return issueValue.abs();
    }

    /**
     * 获取当前块高
     *
     * @param
     * @return java.lang.Long
     * @date 2021/5/14
     */
    public Long getBlockCurrentNumber() {
        Long currentNumber = 0L;
        NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
        if (networkStatRedis == null) {
            return currentNumber;
        } else {
            currentNumber = networkStatRedis.getCurNumber();
            Long bNumber = networkStatRedis.getCurNumber();
            /* 查询缓存最新的八条区块信息 */
            List<Block> items = statisticCacheService.getBlockCache(0, 8);
            if (!items.isEmpty()) {
                /*
                 * 如果统计区块小于区块交易则重新查询新的区块
                 */
                Long dValue = items.get(0).getNum() - bNumber;
                if (dValue > 0) {
                    items = statisticCacheService.getBlockCacheByStartEnd(dValue, dValue + 8);
                }
                if (dValue < 0) {
                    currentNumber = items.get(0).getNum();
                }
            }
        }
        return currentNumber;
    }

    /**
     * 获取流通量
     * 流通量 = 本增发周期总发行量 - 实时锁仓合约余额 -  实时质押合约余额 - 实时委托奖励池合约余额 - 实时激励池余额 - 实时所有基金会账户余额
     *
     * @param
     * @return void
     * @date 2021/5/14
     */
    public BigDecimal getCirculationValue() {
        BigDecimal issueValue = getIssueValue();
        BigDecimal lockUpValue = getLockUpValue();
        BigDecimal stakingValue = getStakingValue();
        BigDecimal delegationValue = getDelegationValue();
        BigDecimal incentivePoolValue = getIncentivePoolValue();
        BigDecimal foundationValue = getFoundationValue();
        BigDecimal circulationValue = issueValue.subtract(lockUpValue).subtract(stakingValue).subtract(delegationValue).subtract(incentivePoolValue).subtract(foundationValue);
        if (circulationValue.signum() == -1) {
            log.error("获取流通量[{}]错误,不能为负数", issueValue.toString());
        }
        return circulationValue.abs();
    }

    /**
     * 获取实时锁仓合约余额
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    private BigDecimal getLockUpValue() {
        BigDecimal lockUpValue = new BigDecimal(0);
        InternalAddressExample example = new InternalAddressExample();
        InternalAddressExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(1);
        List<InternalAddress> lockUpLists = internalAddressMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(lockUpLists)) {
            for (InternalAddress internalAddress : lockUpLists) {
                lockUpValue = lockUpValue.add(internalAddress.getBalance());
            }
        }
        return lockUpValue;
    }

    /**
     * 获取实时质押合约余额
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    private BigDecimal getStakingValue() {
        BigDecimal stakingValue = new BigDecimal(0);
        InternalAddressExample example = new InternalAddressExample();
        InternalAddressExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(2);
        List<InternalAddress> stakingLists = internalAddressMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(stakingLists)) {
            for (InternalAddress internalAddress : stakingLists) {
                stakingValue = stakingValue.add(internalAddress.getBalance());
            }
        }
        return stakingValue;
    }

    /**
     * 获取实时委托奖励池合约余额
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    private BigDecimal getDelegationValue() {
        BigDecimal delegationValue = new BigDecimal(0);
        InternalAddressExample example = new InternalAddressExample();
        InternalAddressExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(6);
        List<InternalAddress> delegationLists = internalAddressMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(delegationLists)) {
            for (InternalAddress internalAddress : delegationLists) {
                delegationValue = delegationValue.add(internalAddress.getBalance());
            }
        }
        return delegationValue;
    }

    /**
     * 实时激励池余额
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    private BigDecimal getIncentivePoolValue() {
        BigDecimal incentivePoolValue = new BigDecimal(0);
        InternalAddressExample example = new InternalAddressExample();
        InternalAddressExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(3);
        List<InternalAddress> incentivePoolLists = internalAddressMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(incentivePoolLists)) {
            for (InternalAddress internalAddress : incentivePoolLists) {
                incentivePoolValue = incentivePoolValue.add(internalAddress.getBalance());
            }
        }
        return incentivePoolValue;
    }

    /**
     * 获取实时所有基金会账户余额
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    private BigDecimal getFoundationValue() {
        BigDecimal foundationValue = new BigDecimal(0);
        InternalAddressExample example = new InternalAddressExample();
        InternalAddressExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(0);
        List<InternalAddress> foundationLists = internalAddressMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(foundationLists)) {
            for (InternalAddress internalAddress : foundationLists) {
                foundationValue = foundationValue.add(internalAddress.getBalance());
            }
        }
        return foundationValue;
    }

    /**
     * 获取实时所有基金会账户锁仓余额
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    private BigDecimal getFoundationLockUpValue() {
        BigDecimal foundationLockUpValue = new BigDecimal(0);
        InternalAddressExample example = new InternalAddressExample();
        InternalAddressExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(0);
        List<InternalAddress> foundationLockUpLists = internalAddressMapper.selectByExample(example);
        if (CollUtil.isNotEmpty(foundationLockUpLists)) {
            for (InternalAddress internalAddress : foundationLockUpLists) {
                foundationLockUpValue = foundationLockUpValue.add(internalAddress.getRestrictingBalance());
            }
        }
        return foundationLockUpValue;
    }

    /**
     * 获取总质押
     * 总质押 = 实时质押合约余额
     *
     * @param
     * @return void
     * @date 2021/5/14
     */
    public BigDecimal getTotalStakingValue() {
        return getStakingValue();
    }

    /**
     * 获取质押率分母
     * 质押率分母 = 总发行量 - 实时激励池余额 - 实时委托奖励池合约余额 - 实时所有基金会账户余额 - 实时所有基金会账户锁仓余额
     *
     * @param
     * @return void
     * @date 2021/5/14
     */
    public BigDecimal getStakingDenominator() {
        BigDecimal issueValue = getIssueValue();
        BigDecimal delegationValue = getDelegationValue();
        BigDecimal incentivePoolValue = getIncentivePoolValue();
        BigDecimal foundationValue = getFoundationValue();
        BigDecimal foundationLockUpValue = getFoundationLockUpValue();
        BigDecimal stakingDenominator = issueValue.subtract(incentivePoolValue).subtract(delegationValue).subtract(foundationValue).subtract(foundationLockUpValue);
        if (stakingDenominator.signum() == -1) {
            log.error("获取质押率分母[{}]错误,不能为负数", stakingDenominator.toString());
        }
        return stakingDenominator.abs();
    }

}
