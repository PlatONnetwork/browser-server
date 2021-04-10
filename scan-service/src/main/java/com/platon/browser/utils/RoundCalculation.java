package com.platon.browser.utils;

import com.platon.browser.config.BlockChainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 20:18
 */

public class RoundCalculation {

    private RoundCalculation(){}

    /**
     * 投票结束轮数转化区块高度
     * 结束块高 = 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + 提案入参轮数 * 共识周期块数 - 20
     */


    private static Logger logger = LoggerFactory.getLogger(RoundCalculation.class);

    /**
     * 取参数提案投票结束区块号
     * @param proposalTxBlockNumber 提案交易所在区块号
     * @param chainConfig 链配置
     * @return
     */
    public static BigDecimal getParameterProposalVoteEndBlockNum (long proposalTxBlockNumber, BlockChainConfig chainConfig ) {
        try {
            //交易所在区块高度
            BigDecimal txBlockNumber = BigDecimal.valueOf(proposalTxBlockNumber);
            //结算周期块数
            BigDecimal settlePeriodBlockCount = new BigDecimal(chainConfig.getSettlePeriodBlockCount());
            //【治理】参数提案的投票持续最长的时间（单位：s）
            BigDecimal paramProposalVoteDurationSeconds= new BigDecimal(chainConfig.getParamProposalVoteDurationSeconds());
            // 出块间隔
            BigDecimal blockInterval = BigDecimal.ONE;
            // 计算投票截止区块号：
            // (CEILING(参数提案所在区块号/结算周期块数)+FLOOR(参数提案的投票持续最长的时间/(出块间隔*结算周期块数)))*结算周期块数
            return txBlockNumber
                    .divide(settlePeriodBlockCount,0, RoundingMode.CEILING)
                    .add(paramProposalVoteDurationSeconds.divide(blockInterval.multiply(settlePeriodBlockCount),0,RoundingMode.FLOOR))
                    .multiply(settlePeriodBlockCount);
        } catch (Exception e) {
            logger.error("[RoundCalculation] exception");
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal endBlockNumCal ( String blockNumber, BigDecimal consensusRound, BlockChainConfig chainConfig ) {
        try {
            //交易所在区块高度
            BigDecimal txBlockNumber = new BigDecimal(blockNumber);
            //共识周期块数
            BigDecimal consensusCount = new BigDecimal(chainConfig.getConsensusPeriodBlockCount());
            //提案交易所在块高%共识周期块数,交易所在第几个共识轮
            BigDecimal[] belongToConList = txBlockNumber.divideAndRemainder(consensusCount);
            BigDecimal belongToCon = belongToConList[1];
            //转换结束快高
            return txBlockNumber.add(consensusCount).subtract(belongToCon).add(consensusRound.multiply(consensusCount)).subtract(new BigDecimal(20));
        } catch (Exception e) {
            logger.error("[RoundCalculation] exception");
            return BigDecimal.ZERO;
        }
    }

    /**
     * 生效轮数转化区块高度
     * 生效块高 = 投票结束区块  + 共识周期块数 - 投票结束区块%共识周期块数  + 1
     */
    public static BigDecimal activeBlockNumCal ( BigDecimal voteNum, BlockChainConfig chainConfig ) {
        try {
            //共识周期块数
            BigDecimal consensusCount = new BigDecimal(chainConfig.getConsensusPeriodBlockCount());
            //提案交易所在块高%共识周期块数,交易所在第几个共识轮
            BigDecimal[] belongToConList = voteNum.divideAndRemainder(consensusCount);
            BigDecimal belongToCon = belongToConList[1];
            //转换生效块高
            return voteNum.add(consensusCount).subtract(belongToCon).add(BigDecimal.ONE);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
