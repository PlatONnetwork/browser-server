package com.platon.browser.analyzer.statistic;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochInfo;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.utils.CalculateUtils;
import com.platon.browser.utils.EpochUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Service
public class StatisticsNetworkAnalyzer {

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private NodeCache nodeCache;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private StatisticBusinessMapper statisticBusinessMapper;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private PlatOnClient platOnClient;

    /**
     * 年份
     */
    private volatile int yearNum = 0;

    /**
     * 总发行量
     */
    private volatile BigDecimal totalIssueValue = BigDecimal.ZERO;

    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void analyze(CollectionEvent event, Block block, EpochMessage epochMessage) throws Exception {
        long startTime = System.currentTimeMillis();
        log.debug("区块入库统计：区块[{}],交易数[{}],共识周期轮数[{}],结算周期轮数[{}],增发周期轮数[{}]",
                  block.getNum(),
                  event.getTransactions().size(),
                  epochMessage.getConsensusEpochRound(),
                  epochMessage.getSettleEpochRound(),
                  epochMessage.getIssueEpochRound());
        // 网络统计
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        networkStat.setNodeId(block.getNodeId());
        networkStat.setNodeName(nodeCache.getNode(block.getNodeId()).getNodeName());
        networkStat.setNextSettle(CalculateUtils.calculateNextSetting(chainConfig.getSettlePeriodBlockCount(), epochMessage.getSettleEpochRound(), epochMessage.getCurrentBlockNumber()));
        setTotalIssueValue(block.getNum(), event.getEpochMessage().getSettleEpochRound(), networkStat);
        statisticBusinessMapper.networkChange(networkStat);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

    /**
     * 设置总发行量
     *
     * @param curBlockNum:      当前块高
     * @param settleEpochRound: 结算周期轮数
     * @param networkStat:      网络统计对象实例
     * @return: void
     * @date: 2021/11/9
     */
    private void setTotalIssueValue(Long curBlockNum, BigInteger settleEpochRound, NetworkStat networkStat) throws Exception {
        try {
            // 结算周期的区块获取总发行量
            if ((curBlockNum - 1) % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
                log.info("当前块高[{}]在第[{}]结算周期获取总发行量", curBlockNum, settleEpochRound);
                yearNum = getYearNum(curBlockNum);
                totalIssueValue = getTotalIssueValue(yearNum);
                networkStat.setYearNum(yearNum);
                networkStat.setIssueValue(totalIssueValue);
            } else {
                // 非结算周期的区块则取本地内存中的值，如果值校验不对则重新获取
                if (yearNum < 1 || ObjectUtil.isNull(totalIssueValue) || totalIssueValue.compareTo(BigDecimal.ZERO) <= 0) {
                    // agent重启，并未追到结算周期时，本地内存失效则重新获取
                    log.info("本地内存中的年份小于1或者总发行量为空或者总发行量小于等于0，将重新获取年份和总发行量");
                    yearNum = getYearNum(curBlockNum);
                    totalIssueValue = getTotalIssueValue(yearNum);
                }
                networkStat.setYearNum(yearNum);
                networkStat.setIssueValue(totalIssueValue);
                log.info("当前块高[{}]在第[{}]结算周期获取本地内存的年份[{}]和总发行量[{}]成功", curBlockNum, settleEpochRound, yearNum, totalIssueValue.toPlainString());
            }
        } catch (Exception e) {
            log.error(StrUtil.format("当前块高[{}]在第[{}]结算周期获取总发行量异常，即将重试", curBlockNum, settleEpochRound), e);
            // 如果当前区块获取总发行量异常，则重置本地内存的值
            yearNum = 0;
            totalIssueValue = BigDecimal.ZERO;
            throw e;
        }
    }

    /**
     * 当前块高
     *
     * @param currentNumber: 当前块高
     * @return: int
     * @date: 2021/11/9
     */
    private int getYearNum(Long currentNumber) throws Exception {
        // 上一结算周期最后一个块号
        BigInteger preSettleEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(Convert.toBigInteger(currentNumber), chainConfig.getSettlePeriodBlockCount());
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
     * 获取总发行量
     *
     * @param yearNum: 第几年
     * @return: java.math.BigDecimal
     * @date: 2021/11/9
     */
    private BigDecimal getTotalIssueValue(int yearNum) throws Exception {
        // 获取初始发行金额
        BigDecimal initIssueAmount = chainConfig.getInitIssueAmount();
        initIssueAmount = com.platon.utils.Convert.toVon(initIssueAmount, com.platon.utils.Convert.Unit.KPVON);
        // 每年固定增发比例
        BigDecimal addIssueRate = chainConfig.getAddIssueRate();
        BigDecimal issueValue = initIssueAmount.multiply(addIssueRate.add(new BigDecimal(1L)).pow(yearNum)).setScale(4, BigDecimal.ROUND_HALF_UP);
        log.info("总发行量[{}]=初始发行量[{}]*(1+增发比例[{}])^第[{}]年", issueValue.toPlainString(), initIssueAmount.toPlainString(), addIssueRate.toPlainString(), yearNum);
        if (issueValue.compareTo(BigDecimal.ZERO) <= 0 || issueValue.compareTo(initIssueAmount) <= 0) {
            throw new Exception(StrUtil.format("获取总发行量[{}]错误,不能小于等于0或者小于等于初始发行量", issueValue.toPlainString()));
        }
        return issueValue;
    }

}
