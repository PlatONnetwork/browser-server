package com.platon.browser.common.service.epoch;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static com.platon.browser.common.service.epoch.EpochRetryService.EPOCH_CHANGES;

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

    @Autowired
    private NetworkStatCache networkStatCache;

    /**
     * 使用区块号更新服务内部状态
     * @param blockNumber
     */
    public EpochMessage getEpochMessage(Long blockNumber) throws BlockNumberException {
        // 每个周期第一个块计算下一个周期的相关数据
        currentBlockNumber=BigInteger.valueOf(blockNumber);
        // 计算共识周期轮数

        BigInteger prevBlockNumber = currentBlockNumber.subtract(BigInteger.ONE);

        // 为防止在增发、结算、共识三个周期重叠时奖励金额计算错误，规定执行顺序为：增发周期变更->结算周期变更->共识周期变更
        issueEpochRound=EpochUtil.getEpoch(currentBlockNumber,chainConfig.getAddIssuePeriodBlockCount());
        if(prevBlockNumber.longValue()%chainConfig.getAddIssuePeriodBlockCount().longValue()==0){
            // 增发周期变更
            try {
                epochRetryService.issueChange(currentBlockNumber);
                applyConfigChange();
            } catch (Exception e) {
                log.error("增发周期变更执行失败:",e);
            }
        }

        settleEpochRound=EpochUtil.getEpoch(currentBlockNumber,chainConfig.getSettlePeriodBlockCount());
        if(prevBlockNumber.longValue()%chainConfig.getSettlePeriodBlockCount().longValue()==0){
            // 结算周期变更
            try {
                epochRetryService.settlementChange(currentBlockNumber);
                applyConfigChange();
            } catch (Exception e) {
                log.error("结算周期变更执行失败:",e);
            }
        }

        consensusEpochRound=EpochUtil.getEpoch(currentBlockNumber,chainConfig.getConsensusPeriodBlockCount());
        if(prevBlockNumber.longValue()%chainConfig.getConsensusPeriodBlockCount().longValue()==0){
            // 共识周期变更
            try {
                epochRetryService.consensusChange(currentBlockNumber);
            } catch (Exception e) {
                log.error("共识周期变更执行失败:",e);
            }
        }

        return EpochMessage.newInstance()
                .updateWithEpochService(this)
                .updateWithEpochRetryService(epochRetryService);
    }

    /**
     * 应用配置变更
     * 1、更新BlockChainConfig涉及的相关配置项
     * 2、更新网络统计缓存涉及的相关数据项
     */
    private void applyConfigChange(){
        ConfigChange summary = new ConfigChange();
        while (EPOCH_CHANGES.peek()!=null){
            ConfigChange configChange = EPOCH_CHANGES.poll();

            if(configChange.getIssueEpoch()!=null){
                // 更新增发周期轮数
                chainConfig.setIssueEpochRound(configChange.getIssueEpoch());
                summary.setIssueEpoch(configChange.getIssueEpoch());
            }
            if(configChange.getYearStartNum()!=null){
                // 更新增发周期起始块号
                chainConfig.setIssueEpochStartBlockNumber(configChange.getYearStartNum());
                summary.setYearStartNum(configChange.getYearStartNum());
            }
            if(configChange.getYearEndNum()!=null){
                // 更新增发周期结束块号
                chainConfig.setIssueEpochEndBlockNumber(configChange.getYearEndNum());
                summary.setYearEndNum(configChange.getYearEndNum());
            }

            if(configChange.getYearStartNum()!=null&&configChange.getYearEndNum()!=null){
                // 更新增发周期区块数
                chainConfig.setAddIssuePeriodBlockCount(configChange.getYearEndNum().subtract(configChange.getYearStartNum()).toBigInteger());
                // 更新每个增发周期的结算周期数
                chainConfig.setSettlePeriodCountPerIssue(chainConfig.getAddIssuePeriodBlockCount().divide(chainConfig.getSettlePeriodBlockCount()));
            }

            if(configChange.getSettleStakeReward()!=null) summary.setSettleStakeReward(configChange.getSettleStakeReward());
            if(configChange.getBlockReward()!=null) summary.setBlockReward(configChange.getBlockReward());
            if(configChange.getStakeReward()!=null) summary.setStakeReward(configChange.getStakeReward());
        }
        networkStatCache.updateByEpochChange(summary);
    }


}
