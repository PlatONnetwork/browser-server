package com.platon.browser.util;

import com.platon.browser.config.BlockChainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 20:18
 */

public class RoundCalculation {

    /**
     * 投票结束轮数转化区块高度
     * 结束块高 = 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + 提案入参轮数 * 共识周期块数 - 20
     */


    private static Logger logger = LoggerFactory.getLogger(RoundCalculation.class);


    public static BigDecimal endBlockNumCal ( String blockNumber, String endRound, BlockChainConfig bc ) {
        try {
            //交易所在区块高度
            BigDecimal txBlockNumber = new BigDecimal(blockNumber);
            //治理交易定义投票结束轮数
            BigDecimal txEndRound = new BigDecimal(endRound);
            //共识周期块数
            BigDecimal consensusCount = new BigDecimal(bc.getConsensusPeriodBlockCount());
            //提案交易所在块高%共识周期块数,交易所在第几个共识轮
            BigDecimal[] belongToConList = txBlockNumber.divideAndRemainder(consensusCount);
            BigDecimal belongToCon = belongToConList[1];
            //转换结束快高
            BigDecimal endBlockNumber = txBlockNumber.add(consensusCount).subtract(belongToCon).add(txEndRound.multiply(consensusCount)).subtract(new BigDecimal(20));
            return endBlockNumber;
        } catch (Exception e) {
            logger.error("[RoundCalculation] exception");
            return BigDecimal.ZERO;
        }
    }

    /**
     * 生效轮数转化区块高度
     * 生效块高 = 提案交易所在块高 + 共识周期块数 - 提案交易所在块高%共识周期块数 + (提案入参轮数+4) * 共识周期块数  + 1
     */
    public static BigDecimal activeBlockNumCal ( String blockNumber, String endRound, BlockChainConfig bc ) {
        try {
            //交易所在区块高度
            BigDecimal txBlockNumber = new BigDecimal(blockNumber);
            //治理交易生效轮数
            BigDecimal txActiveRound = new BigDecimal(endRound).add(new BigDecimal(bc.getVersionProposalActiveConsensusRounds()).subtract(BigDecimal.ONE));
            //共识周期块数
            BigDecimal consensusCount = new BigDecimal(bc.getConsensusPeriodBlockCount());
            //提案交易所在块高%共识周期块数,交易所在第几个共识轮
            BigDecimal[] belongToConList = txBlockNumber.divideAndRemainder(consensusCount);
            BigDecimal belongToCon = belongToConList[1];
            //转换生效块高
            BigDecimal activeBlockNum = txBlockNumber.add(consensusCount).subtract(belongToCon).add(txActiveRound.multiply(consensusCount)).add(BigDecimal.ONE);
            return activeBlockNum;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}