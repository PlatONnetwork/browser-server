package com.platon.browser.service;

import com.platon.browser.bean.CountBalance;
import com.platon.browser.bean.StakingBO;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.dao.custommapper.CustomRpPlanMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    private CustomNodeMapper customNodeMapper;

    @Resource
    private BlockChainConfig blockChainConfig;

    @Resource
    private CustomInternalAddressMapper customInternalAddressMapper;

    @Resource
    private CustomRpPlanMapper customRpPlanMapper;

    @Resource
    private StatisticCacheService statisticCacheService;

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

}
