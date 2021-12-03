package com.platon.browser.task;

import com.platon.browser.bean.CountBalance;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.dao.custommapper.CustomNOptBakMapper;
import com.platon.browser.dao.custommapper.CustomRpPlanMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.service.account.AccountService;
import com.platon.browser.service.elasticsearch.EsErc20TxRepository;
import com.platon.browser.service.elasticsearch.EsErc721TxRepository;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.task.bean.NetworkStatistics;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.CalculateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 网络统计相关信息更新任务
 */

@Component
@Slf4j
public class NetworkStatUpdateTask {

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private AccountService accountService;

    @Resource
    private StatisticBusinessMapper statisticBusinessMapper;

    @Resource
    private CustomRpPlanMapper customRpPlanMapper;

    @Resource
    private CustomInternalAddressMapper customInternalAddressMapper;

    @Resource
    private EsTransactionRepository esTransactionRepository;

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    @Resource
    private CustomNOptBakMapper customNOptBakMapper;

    @Scheduled(cron = "0/5  * * * * ?")
    public void networkStatUpdate() {
        // 只有程序正常运行才执行任务
        if (AppStatusUtil.isRunning()) start();
    }

    /**
     * 更新交易统计数
     *
     * @param :
     * @return: void
     * @date: 2021/12/1
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void updateQty() {
        ESQueryBuilderConstructor count = new ESQueryBuilderConstructor();
        //获取es交易数
        Long totalCount = 0L;
        try {
            ESResult<?> totalCountRes = esTransactionRepository.Count(count);
            totalCount = totalCountRes.getTotal();
        } catch (Exception e) {
            log.error("获取es交易数异常", e);
        }
        //获取erc20交易数
        Long erc20Count = 0L;
        try {
            ESResult<?> erc20Res = esErc20TxRepository.Count(count);
            erc20Count = erc20Res.getTotal();
        } catch (Exception e) {
            log.error("获取erc20交易数异常", e);
        }
        //获取erc721交易数
        Long erc721Count = 0L;
        try {
            ESResult<?> erc721Res = esErc721TxRepository.Count(count);
            erc721Count = erc721Res.getTotal();
        } catch (Exception e) {
            log.error("获取erc721交易数异常", e);
        }
        //获得地址数统计
        int addressQty = statisticBusinessMapper.getNetworkStatisticsFromAddress();
        //获得进行中的提案
        int doingProposalQty = statisticBusinessMapper.getNetworkStatisticsFromProposal();
        //获取提案总数
        int proposalQty = statisticBusinessMapper.getProposalQty();
        //获取节点操作数
        long nodeOptSeq = customNOptBakMapper.getLastNodeOptSeq();
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        networkStat.setTxQty(totalCount.intValue());
        networkStat.setErc20TxQty(erc20Count.intValue());
        networkStat.setErc721TxQty(erc721Count.intValue());
        networkStat.setAddressQty(addressQty);
        networkStat.setDoingProposalQty(doingProposalQty);
        networkStat.setProposalQty(proposalQty);
        networkStat.setNodeOptSeq(nodeOptSeq);
    }

    protected void start() {
        try {
            NetworkStat networkStat = networkStatCache.getNetworkStat();
            Long curNumber = networkStat.getCurNumber();
            //获取激励池余额
            BigDecimal inciteBalance = accountService.getInciteBalance(BigInteger.valueOf(curNumber));
            //计算流通量
            BigDecimal turnValue = getCirculationValue(networkStat);
            //计算可使用质押量
            BigDecimal availableStaking = CalculateUtils.calculationAvailableValue(networkStat.getIssueRates(), chainConfig, inciteBalance);
            //获得节点相关的网络统计
            NetworkStatistics networkStatistics = statisticBusinessMapper.getNetworkStatisticsFromNode();
            BigDecimal totalValue = networkStatistics.getTotalValue() == null ? BigDecimal.ZERO : networkStatistics.getTotalValue();
            BigDecimal stakingValue = networkStatistics.getStakingValue() == null ? BigDecimal.ZERO : networkStatistics.getStakingValue();
            networkStatCache.updateByTask(turnValue, availableStaking, totalValue, stakingValue);
        } catch (Exception e) {
            log.error("网络统计任务出错:", e);
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
     * 获取流通量
     * 流通量 = 本增发周期总发行量 - 锁仓未到期的金额 - 实时委托奖励池合约余额 - 实时激励池余额 - 实时所有基金会账户余额
     *
     * @param networkStat:
     * @return: java.math.BigDecimal
     * @date: 2021/11/24
     */
    private BigDecimal getCirculationValue(NetworkStat networkStat) {
        List<CountBalance> list = countBalance();
        // 锁仓未到期的金额
        BigDecimal rpNotExpiredValue = customRpPlanMapper.getRPNotExpiredValue(chainConfig.getSettlePeriodBlockCount().longValue(), networkStat.getCurNumber());
        rpNotExpiredValue = Optional.ofNullable(rpNotExpiredValue).orElse(BigDecimal.ZERO);
        // 获取实时委托奖励池合约余额
        CountBalance delegationValue = list.stream().filter(v -> v.getType() == 6).findFirst().orElseGet(CountBalance::new);
        // 实时激励池余额
        CountBalance incentivePoolValue = list.stream().filter(v -> v.getType() == 3).findFirst().orElseGet(CountBalance::new);
        // 获取实时所有基金会账户余额
        CountBalance foundationValue = list.stream().filter(v -> v.getType() == 0).findFirst().orElseGet(CountBalance::new);
        // agent刚启动的时候，如果第一次定时任务比主流程快，可能总发行量还没计算，则流通量可能为负数
        BigDecimal circulationValue = networkStat.getIssueValue()
                                                 .subtract(rpNotExpiredValue)
                                                 .subtract(delegationValue.getFree())
                                                 .subtract(incentivePoolValue.getFree())
                                                 .subtract(foundationValue.getFree());
        log.info("流通量[{}]=本增发周期总发行量[{}]-锁仓未到期的金额[{}]-实时委托奖励池合约余额[{}]-实时激励池余额[{}]-实时所有基金会账户余额[{}];当前块高[{}]结算周期总块数[{}]",
                 circulationValue.toPlainString(),
                 networkStat.getIssueValue().toPlainString(),
                 rpNotExpiredValue.toPlainString(),
                 delegationValue.getFree().toPlainString(),
                 incentivePoolValue.getFree().toPlainString(),
                 foundationValue.getFree().toPlainString(),
                 networkStat.getCurNumber(),
                 chainConfig.getSettlePeriodBlockCount().longValue());
        return circulationValue;
    }

}
