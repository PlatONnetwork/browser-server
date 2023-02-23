package com.platon.browser.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
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
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.service.account.AccountService;
import com.platon.browser.utils.CommonUtil;
import com.platon.browser.utils.EpochUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
        return CommonUtil.ofNullable(() -> networkStat.getTurnValue()).orElse(BigDecimal.ZERO);
    }

    /**
     * 返回当前区块的总流通量(单位：wei)
     * 流通量 = 总发行量 - 总锁仓合约未释放余额 - 激励池合约余额 - 参与计算的基金会账户余额
     * @param blockNumber
     * @return
     */
    public BigDecimal getCirculationValue(Long blockNumber) {
        if (blockNumber==null) {
            NetworkStat networkStat = statisticCacheService.getNetworkStatCache();
              return CommonUtil.ofNullable(() -> networkStat.getTurnValue()).orElse(BigDecimal.ZERO);
        }else{
            BigDecimal issueValue = BigDecimal.ZERO;
            BigDecimal restrictingValue = BigDecimal.ZERO;
            BigDecimal inciteValue = BigDecimal.ZERO;
            BigDecimal foundationValue = BigDecimal.ZERO;
            //计算流通量 = 总发行量 - 总锁仓 -  激励池余额 - 基金会
            //总发行量
            try {
                issueValue = getCurrentBlockIssueValue(blockNumber);
            } catch (Exception e) {
                log.error("获取取总发行量异常", e);
                return BigDecimal.ZERO;
            }

            //总锁仓
            BigDecimal rpNotExpiredValue = customRpPlanMapper.getRPNotExpiredValue(blockChainConfig.getSettlePeriodBlockCount().longValue(), blockNumber);
            restrictingValue = Optional.ofNullable(rpNotExpiredValue).orElse(BigDecimal.ZERO);

            //激励池余额
            inciteValue = accountService.getInciteBalance(BigInteger.valueOf(blockNumber));

            // 基金会
            List<String> addressList = customInternalAddressMapper.listCalculableFoundationAddress();
            String addressParamStr = String.join(";", addressList);

            try {
                List<RestrictingBalance> balanceList = specialApi.getRestrictingBalance(platOnClient.getWeb3jWrapper().getWeb3j(), addressParamStr, blockNumber);
                BigInteger value = balanceList.stream().map(RestrictingBalance::getDlFreeBalance).reduce(BigInteger.ZERO, BigInteger::add);
                foundationValue = new BigDecimal(value);
            } catch (Exception e) {
                log.error("获取基金会余额异常", e);
                return BigDecimal.ZERO;
            }

            BigDecimal circulationValue = issueValue.subtract(restrictingValue).subtract(inciteValue).subtract(foundationValue);
            log.debug("区块：{}上的流通量：{}", blockNumber, circulationValue.toPlainString());
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

    @Data
    static class User{
        BigDecimal num1;
        BigDecimal num2;
    }
    public static void main(String[] args) {

        List<User> list=new ArrayList<>();

        User user1=new User();
        user1.setNum1(new BigDecimal(123));
        user1.setNum2(new BigDecimal(100));
        list.add(user1);

        User user2=new User();
        user2.setNum1(new BigDecimal(100));
        user2.setNum2(new BigDecimal(100));
        list.add(user2);
        BigDecimal sum=list.stream().map(User::getNum1).reduce(BigDecimal::add).get();
        System.out.println(sum);
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


    /**
     * 返回当前块高的总发行量：整数，单位：wei
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public BigDecimal getCurrentBlockIssueValue(Long blockNumber) throws Exception {
           //  根据块高算出year
        int yearNum = getYearNum(blockNumber);
           // 算出总发行量
        BigDecimal issueValue = getTotalIssueValue(yearNum);
        log.info("当前块高：{}的总发行量(wei)：{}", blockNumber, issueValue.toPlainString());

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
        BigInteger preSettleEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(Convert.toBigInteger(currentNumber), blockChainConfig.getSettlePeriodBlockCount());
        // 从特殊接口获取
        EpochInfo epochInfo = specialApi.getEpochInfo(platOnClient.getWeb3jWrapper().getWeb3j(), preSettleEpochLastBlockNumber);
        // 第几年
        int yearNum = epochInfo.getYearNum().intValue();
        if (yearNum < 1) {
            throw new Exception(StrUtil.format("当前区块[{}],上一结算周期最后一个块号[{}]获取年份[{}]异常", currentNumber, preSettleEpochLastBlockNumber, yearNum));
        }
        return yearNum;
    }

    /**
     * 返回总发行量：整数，单位：wei
     * @param yearNum
     * @return
     * @throws Exception
     */
    private BigDecimal getTotalIssueValue(int yearNum) throws Exception {
        // 获取初始发行金额(wei)
        BigDecimal initIssueAmount = blockChainConfig.getInitIssueAmount();
        //initIssueAmount = com.platon.utils.Convert.toVon(initIssueAmount, com.platon.utils.Convert.Unit.KPVON);
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
