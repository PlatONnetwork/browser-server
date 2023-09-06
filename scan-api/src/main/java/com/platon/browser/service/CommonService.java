package com.platon.browser.service;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CountBalance;
import com.platon.browser.bean.EpochInfo;
import com.platon.browser.bean.RestrictingBalance;
import com.platon.browser.bean.StakingBO;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.dao.custommapper.CustomRpPlanMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.service.account.AccountService;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.utils.EpochUtil;
import com.platon.utils.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

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
    private CustomNodeMapper customNodeMapper;

    @Resource
    private BlockChainConfig blockChainConfig;

    @Resource
    private CustomInternalAddressMapper customInternalAddressMapper;

    @Resource
    private CustomRpPlanMapper customRpPlanMapper;

    @Resource
    private StatisticCacheService statisticCacheService;

    @Resource
    private AccountService accountService;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;
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
     * 获取总发行量(von)
     * 总发行量=初始发行量*(1+增发比例)^第几年
     *
     * @param
     * @return java.math.BigDecimal
     * @date 2021/5/14
     */
    public BigDecimal getIssueValue() {
        BigDecimal issueValue = BigDecimal.ZERO;
        try {
            NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
            issueValue = networkStat.getIssueValue();
        } catch (Exception e) {
            log.error("获取取总发行量异常", e);
        }
        return issueValue;
    }

    /**
     * 获取流通量
     * 流通量 = 本增发周期总发行量 - 锁仓未到期的金额 - 实时委托奖励池合约余额 - 实时激励池余额 - 实时所有基金会账户余额
     *
     * @param
     * @return void
     * @date 2021/5/14
     */
    public BigDecimal getCirculationValue() {
        NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
        return CommonUtil.ofNullable(() -> turnValueSubInit(networkStat.getTurnValue(), networkStat)).orElse(BigDecimal.ZERO);
    }

    /**
     * 返回当前区块的总流通量(单位：wei)
     * 流通量 = 总发行量 - 总锁仓合约未释放余额 - 激励池合约余额 - 参与计算的基金会账户余额
     * @param blockNumber
     * @return
     */
    public BigDecimal getCirculationValue(Long blockNumber) {
        if (blockNumber==null) {
           return getCirculationValue();
        }else{
            BigDecimal issueValue = BigDecimal.ZERO;
            BigDecimal restrictingValue = BigDecimal.ZERO;
            BigDecimal inciteValue = BigDecimal.ZERO;
            BigDecimal foundationValue = BigDecimal.ZERO;
            BigDecimal remainingOfFoundationInRestricting = BigDecimal.ZERO;

            int chainAge = 0;
            try {
                chainAge = this.getYearNum(blockNumber);
            } catch (Exception e) {
                log.error("根据块高计算链的年龄出错", e);
                return BigDecimal.ZERO;
            }
            //计算流通量 = 总发行量 - 总锁仓 -  激励池余额 - 基金会账户余额 - 基金会在锁仓合约中的剩余金额
            //总发行量
            try {
                issueValue = getCurrentBlockIssueValue(chainAge);
            } catch (Exception e) {
                log.error("获取取总发行量异常", e);
                return BigDecimal.ZERO;
            }

            //总锁仓
            BigDecimal rpNotExpiredValue = customRpPlanMapper.getRPNotExpiredValue(blockChainConfig.getSettlePeriodBlockCount().longValue(), blockNumber);
            restrictingValue = Optional.ofNullable(rpNotExpiredValue).orElse(BigDecimal.ZERO);

            //激励池余额
            inciteValue = accountService.getInciteBalance(BigInteger.valueOf(blockNumber));

            // 基金会账户余额
            List<String> addressList = customInternalAddressMapper.listCalculableFoundationAddress();
            if (!CollectionUtils.isEmpty(addressList)){
                String addressParamStr = String.join(";", addressList);
                try {
                    List<RestrictingBalance> balanceList = specialApi.getRestrictingBalance(platOnClient.getWeb3jWrapper(), addressParamStr, blockNumber);
                    BigInteger value = balanceList.stream().map(RestrictingBalance::getDelegationUnLockedFreeBalance).reduce(BigInteger.ZERO, BigInteger::add);
                    foundationValue = new BigDecimal(value);
                } catch (Exception e) {
                    log.error("获取基金会余额异常", e);
                    return BigDecimal.ZERO;
                }
            }

            // 基金会在锁仓合约中的剩余金额
            remainingOfFoundationInRestricting = this.getRemainingOfFoundationInRestricting(chainAge);

            BigDecimal circulationValue = issueValue.subtract(restrictingValue).subtract(inciteValue).subtract(foundationValue).subtract(remainingOfFoundationInRestricting);
            log.debug("区块：{}上的流通量(von)：{}", blockNumber, circulationValue.toPlainString());
            return circulationValue;
        }
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
     * 获取总质押和质押率分母
     *
     * @param networkStatRedis:
     * @return: com.platon.browser.bean.StakingBO
     * @date: 2021/11/24
     */
    public StakingBO getTotalStakingValueAndStakingDenominator(NetworkStat networkStatRedis) {
        StakingBO bo = new StakingBO();
        List<CountBalance> list = countBalance();
        // 获取实时质押合约余额
        CountBalance stakingValue = list.stream().filter(v -> v.getType() == 2).findFirst().orElseGet(CountBalance::new);
        bo.setTotalStakingValue(stakingValue.getFree());
        log.debug("实时质押合约余额(总质押)为[{}]", stakingValue.getFree().toPlainString());
        BigDecimal issueValue = networkStatRedis.getIssueValue();
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
        if (stakingDenominator.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("获取质押率分母[{}]错误,不能小于等于0", stakingDenominator.toPlainString());
        }
        bo.setStakingDenominator(stakingDenominator);
        return bo;
    }

    public BigDecimal turnValueSubInit(BigDecimal turn, NetworkStat networkStat){
        Integer yearNum = networkStat.getYearNum();
        BigDecimal remain = getRemainingOfFoundationInRestricting(yearNum);
        return turn.subtract(remain);
    }

    /**
     * 按块高获取基金会锁仓在锁仓合约中的剩余基金。
     * 基金会在创世块，会把一笔lat锁在锁仓合约中（注意，并非锁仓计划中），按年释放一部分，进入激励池
     * @param chainAge
     * @return
     */
    public BigDecimal getRemainingOfFoundationInRestricting(int chainAge){
        BigDecimal remain = BigDecimal.ZERO;
        for (Integer key: blockChainConfig.getFoundationSubsidies().keySet()){
            if(key.compareTo(chainAge) > 0){
                remain = remain.add(blockChainConfig.getFoundationSubsidies().get(key));
            }
        }
        if (remain.compareTo(BigDecimal.ZERO) > 0 ){
            remain = Convert.toVon(remain, Convert.Unit.KPVON);
        }
        return remain;
    }
    /**
     * 返回当前链龄的总发行量：整数（单位：von）
     * @param chainAge
     * @return
     * @throws Exception
     */
    public BigDecimal getCurrentBlockIssueValue(int chainAge) throws Exception {
           // 算出总发行量
        BigDecimal issueValue = getTotalIssueValue(chainAge);
        log.debug("当前链龄的：{}的总发行量(von)：{}", chainAge, issueValue.toPlainString());

        return issueValue;
    }

    /**
     * 计算块高的增发年度值
     *
     * @param currentNumber: 当前块高
     * @return: int
     * @date: 2021/11/9
     */
    private int getYearNum(Long currentNumber) throws Exception {
        // 上一结算周期最后一个块号
        BigInteger preSettleEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(BigInteger.valueOf(currentNumber), blockChainConfig.getSettlePeriodBlockCount());
        // 从特殊接口获取
        EpochInfo epochInfo = specialApi.getEpochInfo(platOnClient.getWeb3jWrapper(), preSettleEpochLastBlockNumber);
        // 第几年
        int yearNum = epochInfo.getYearNum().intValue();
        if (yearNum < 1) {
            throw new Exception(StrUtil.format("当前区块[{}],上一结算周期最后一个块号[{}]获取年份[{}]异常", currentNumber, preSettleEpochLastBlockNumber, yearNum));
        }
        return yearNum;
    }

    /**
     * 返回总发行量：整数（单位：von）
     * @param yearNum
     * @return
     * @throws Exception
     */
    private BigDecimal getTotalIssueValue(int yearNum) throws Exception {
        // 获取初始发行金额(LAT/ATP)
        BigDecimal initIssueAmount = blockChainConfig.getInitIssueAmount();
        initIssueAmount = com.platon.utils.Convert.toVon(initIssueAmount, com.platon.utils.Convert.Unit.KPVON);
        // 每年固定增发比例
        BigDecimal addIssueRate = blockChainConfig.getAddIssueRate();
        //BigDecimal issueValue = initIssueAmount.multiply(addIssueRate.add(new BigDecimal(1L)).pow(yearNum)).setScale(4, BigDecimal.ROUND_HALF_UP);
        //取整
        BigDecimal issueValue = initIssueAmount.multiply(addIssueRate.add(new BigDecimal(1L)).pow(yearNum)).setScale(0, BigDecimal.ROUND_HALF_UP);
        log.info("总发行量[{}]=初始发行量[{}]*(1+增发比例[{}])^第[{}]年", issueValue.toPlainString(), initIssueAmount.toPlainString(), addIssueRate.toPlainString(), yearNum);
        if (issueValue.compareTo(BigDecimal.ZERO) <= 0 || issueValue.compareTo(initIssueAmount) <= 0) {
            throw new Exception(StrUtil.format("获取总发行量[{}]错误,不能小于等于0或者小于等于初始发行量", issueValue.toPlainString()));
        }
        return issueValue;
    }
}
