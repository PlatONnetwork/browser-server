package com.platon.browser.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CountBalance;
import com.platon.browser.bean.EpochInfo;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
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

    public final static  BigDecimal ISSUE_VALUE = new BigDecimal("10250000000000000000000000000.0000");

    @Resource
    private CustomNodeMapper customNodeMapper;

    @Resource
    private BlockChainConfig blockChainConfig;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private CustomInternalAddressMapper customInternalAddressMapper;

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
     * 总发行量=初始发行量*(1+增发比例)^第几年
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    @Cacheable(value = "getIssueValue")
    public BigDecimal getIssueValue() {
        BigDecimal issueValue = new BigDecimal(0);
        log.info("进入获取总发行量方法");
        try {
            // 获取初始发行金额
            BigDecimal initIssueAmount = blockChainConfig.getInitIssueAmount();
            initIssueAmount = com.platon.utils.Convert.toVon(initIssueAmount, com.platon.utils.Convert.Unit.KPVON);
            // 每年固定增发比例
            BigDecimal addIssueRate = blockChainConfig.getAddIssueRate();
            // 第几年
            int yearNum = getYearNum();
            issueValue = initIssueAmount.multiply(addIssueRate.add(new BigDecimal(1L)).pow(yearNum)).setScale(4, BigDecimal.ROUND_HALF_UP);
            log.info("总发行量[{}]=初始发行量[{}]*(1+增发比例[{}])^第几年[{}];", issueValue.toPlainString(), initIssueAmount.toPlainString(), addIssueRate.toPlainString(), yearNum);
            if (issueValue.signum() == -1) {
                throw new Exception(StrUtil.format("获取总发行量[{}]错误,不能为负数", issueValue.toPlainString()));
            }
            if (initIssueAmount.compareTo(issueValue) >= 0) {
                throw new Exception(StrUtil.format("获取总发行量[{}]错误,小于或等于初始发行量", issueValue.toPlainString()));
            }
        } catch (Exception e) {
            log.error("获取取总发行量异常", e);
        }
        return issueValue;
    }

    /**
     * 获取年份(第几年)--从第1年开始算，而不是0
     *
     * @param
     * @return int
     * @date 2021/5/15
     */
    private int getYearNum() {
        int yearNum = 1;
        try {
            // 当前块高
            Long currentNumber = CommonUtil.ofNullable(() -> getBlockCurrentNumber()).orElse(0L);
            // 上一结算周期最后一个块号
            BigInteger preSettleEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(Convert.toBigInteger(currentNumber), blockChainConfig.getSettlePeriodBlockCount());
            // 从特殊接口获取
            EpochInfo epochInfo = specialApi.getEpochInfo(platOnClient.getWeb3jWrapper().getWeb3j(), preSettleEpochLastBlockNumber);
            // 第几年
            yearNum = epochInfo.getYearNum().intValue();
            log.info("获取年份：当前块高[{}]，上一结算周期最后一个块号[{}]，年份[{}]", currentNumber, preSettleEpochLastBlockNumber, yearNum);
        } catch (Exception e) {
            log.error("获取年份(第几年)异常", e);
        }
        return yearNum;
    }

    /**
     * 获取当前块高
     *
     * @param
     * @return java.lang.Long
     * @date 2021/5/14
     */
    public Long getBlockCurrentNumber() {
        BigInteger nowBlockNumber = new BigInteger("0");
        try {
            nowBlockNumber = platOnClient.getWeb3jWrapper().getWeb3j().platonBlockNumber().send().getBlockNumber();
        } catch (Exception e) {
            log.error("获取当前块高异常", e);
        }
        return Convert.toLong(nowBlockNumber, 0L);
    }

    /**
     * 获取流通量
     * 流通量 = 本增发周期总发行量 - 实时锁仓合约余额 -  实时质押合约余额 - 实时委托奖励池合约余额 - 实时激励池余额 - 实时所有基金会账户余额
     *
     * @param
     * @return void
     * @date 2021/5/14
     */
    @Cacheable(value = "getCirculationValue")
    public BigDecimal getCirculationValue() {
        List<CountBalance> list = countBalance();

        BigDecimal issueValue = getIssueValue();
        log.info("获取总发行量[{}]", issueValue.toPlainString());

        CommonService.check(issueValue);
        issueValue = CommonService.ISSUE_VALUE;

        // 获取实时锁仓合约余额
        CountBalance lockUpValue = list.stream().filter(v -> v.getType() == 1).findFirst().orElseGet(CountBalance::new);
        // 获取实时质押合约余额
        CountBalance stakingValue = list.stream().filter(v -> v.getType() == 2).findFirst().orElseGet(CountBalance::new);
        // 获取实时委托奖励池合约余额
        CountBalance delegationValue = list.stream().filter(v -> v.getType() == 6).findFirst().orElseGet(CountBalance::new);
        // 实时激励池余额
        CountBalance incentivePoolValue = list.stream().filter(v -> v.getType() == 3).findFirst().orElseGet(CountBalance::new);
        // 获取实时所有基金会账户余额
        CountBalance foundationValue = list.stream().filter(v -> v.getType() == 0).findFirst().orElseGet(CountBalance::new);
        BigDecimal circulationValue = issueValue.subtract(lockUpValue.getFree())
                                                .subtract(stakingValue.getFree())
                                                .subtract(delegationValue.getFree())
                                                .subtract(incentivePoolValue.getFree())
                                                .subtract(foundationValue.getFree());
        log.info("流通量[{}]=本增发周期总发行量[{}]-实时锁仓合约余额[{}]-实时质押合约余额[{}]-实时委托奖励池合约余额[{}]-实时激励池余额[{}]-实时所有基金会账户余额[{}];",
                 circulationValue.toPlainString(),
                 issueValue.toPlainString(),
                 lockUpValue.getFree().toPlainString(),
                 stakingValue.getFree().toPlainString(),
                 delegationValue.getFree().toPlainString(),
                 incentivePoolValue.getFree().toPlainString(),
                 foundationValue.getFree().toPlainString());
        if (circulationValue.signum() == -1) {
            log.error("获取流通量[{}]错误,不能为负数", circulationValue.toPlainString());
        }
        return circulationValue;
    }

    /**
     * 查询统计的余额
     *
     * @param
     * @return java.util.List<com.platon.browser.bean.CountBalance>
     * @date 2021/5/15
     */
    private List<CountBalance> countBalance() {
        List<CountBalance> list = customInternalAddressMapper.countBalance();
        return list;
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
        List<CountBalance> list = countBalance();
        // 获取实时质押合约余额
        CountBalance stakingValue = list.stream().filter(v -> v.getType() == 2).findFirst().orElseGet(CountBalance::new);
        return stakingValue.getFree();
    }

    /**
     * 获取质押率分母
     * 质押率分母 = 总发行量 - 实时激励池余额 - 实时委托奖励池合约余额 - 实时所有基金会账户余额 - 实时所有基金会账户锁仓余额
     *
     * @param
     * @return void
     * @date 2021/5/14
     */
    @Cacheable(value = "getStakingDenominator")
    public BigDecimal getStakingDenominator() {
        List<CountBalance> list = countBalance();
        BigDecimal issueValue = getIssueValue();
        log.info("获取总发行量[{}]", issueValue.toPlainString());

        CommonService.check(issueValue);
        issueValue = CommonService.ISSUE_VALUE;

        // 获取实时委托奖励池合约余额
        CountBalance delegationValue = list.stream().filter(v -> v.getType() == 6).findFirst().orElseGet(CountBalance::new);
        // 实时激励池余额
        CountBalance incentivePoolValue = list.stream().filter(v -> v.getType() == 3).findFirst().orElseGet(CountBalance::new);
        // 获取实时所有基金会账户余额
        CountBalance foundationValue = list.stream().filter(v -> v.getType() == 0).findFirst().orElseGet(CountBalance::new);
        BigDecimal stakingDenominator = issueValue.subtract(incentivePoolValue.getFree()).subtract(delegationValue.getFree()).subtract(foundationValue.getFree()).subtract(foundationValue.getLocked());
        log.debug("质押率分母[{}]=总发行量[{}]-实时激励池余额[{}]-实时委托奖励池合约余额[{}]-实时所有基金会账户余额[{}]-实时所有基金会账户锁仓余额[{}];",
                  stakingDenominator.toPlainString(),
                  issueValue.toPlainString(),
                  incentivePoolValue.getFree().toPlainString(),
                  delegationValue.getFree().toPlainString(),
                  foundationValue.getFree().toPlainString(),
                  foundationValue.getLocked().toPlainString());
        if (stakingDenominator.signum() == -1) {
            log.error("获取质押率分母[{}]错误,不能为负数", stakingDenominator.toPlainString());
        }
        return stakingDenominator;
    }

    public static void check( BigDecimal calculated){
        if(calculated == null){
            log.error("总发行量 check value error calculated is null" );
        }

        if(calculated.compareTo(ISSUE_VALUE) != 0){
            log.error("总发行量 check value error calculated = {}  inner = {}", calculated, ISSUE_VALUE);
        }
    }

}
