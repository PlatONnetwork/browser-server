package com.platon.browser.collection.service.epoch;

import com.platon.browser.common.dto.EpochMessage;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * 周期切换服务
 *
 * 1、根据区块号计算周期切换相关值：
 *      名称/含义                                                                   变量名称
 *   当前区块号                                                                  currentBlockNumber
 *   当前所处共识周期轮数                                                         consensusEpochRound
 *   当前所处结算周期轮数                                                         settleEpochRound
 *   当前所处结算周期轮数                                                         issueEpochRound
 */
@Slf4j
@Service
public class EpochService {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochRetryService epochRetryService;

    @Getter private BigInteger currentBlockNumber; // 当前区块号
    @Getter private BigInteger consensusEpochRound=BigInteger.ZERO; // 当前所处共识周期轮数
    @Getter private BigInteger settleEpochRound=BigInteger.ZERO; // 当前所处结算周期轮数
    @Getter private BigInteger issueEpochRound=BigInteger.ZERO; // 当前所处结算周期轮数

    /**
     * 使用区块号更新服务内部状态
     * @param blockNumber
     */
    public EpochMessage getEpochMessage(Long blockNumber) throws BlockNumberException {
        this.currentBlockNumber=BigInteger.valueOf(blockNumber);

        // 暂存未计算前的增发周期值
        BigInteger oldIssueEpochRound = this.issueEpochRound;
        // 计算共识周期轮数
        BigInteger oldConsensusEpochRound = this.consensusEpochRound;
        this.consensusEpochRound=EpochUtil.getEpoch(currentBlockNumber,chainConfig.getConsensusPeriodBlockCount());
        if(oldConsensusEpochRound.compareTo(this.consensusEpochRound)!=0){
            // 如果共识周期与根据当前区块号算出来的不一致，则需要重新计算结算周期

            // 共识周期变更
            try {
                epochRetryService.consensusEpochChange(currentBlockNumber);
            } catch (Exception e) {
                log.error("共识周期变更执行失败:",e);
            }

            BigInteger oldSettleEpochRound = this.settleEpochRound;
            this.settleEpochRound=EpochUtil.getEpoch(currentBlockNumber,chainConfig.getSettlePeriodBlockCount());
            if(oldSettleEpochRound.compareTo(this.settleEpochRound)!=0){
                // 如果结算周期与根据当前区块号算出来的不一致，则需要重新计算增发周期

                // 结算周期变更
                try {
                    epochRetryService.settlementEpochChange(currentBlockNumber);
                } catch (Exception e) {
                    log.error("结算周期变更执行失败:",e);
                }

                this.issueEpochRound=EpochUtil.getEpoch(currentBlockNumber,chainConfig.getAddIssuePeriodBlockCount());
            }
        }

        if(oldIssueEpochRound.compareTo(this.issueEpochRound)!=0){
            try {
                epochRetryService.issueEpochChange(currentBlockNumber);
            } catch (Exception e) {
                log.error("增发周期变更执行失败:",e);
            }
        }
        return EpochMessage.newInstance()
                .updateWithEpochService(this)
                .updateWithEpochRetryService(epochRetryService);
    }
}
