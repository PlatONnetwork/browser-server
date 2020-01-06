package com.platon.browser.complement.converter.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.complement.dto.AnnualizedRateInfo;
import com.platon.browser.common.complement.dto.PeriodValueElement;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.param.epoch.Settle;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.sdk.contracts.ppos.dto.resp.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OnSettleConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private StakingMapper stakingMapper;

	public void convert(CollectionEvent event, Block block) {
        long startTime = System.currentTimeMillis();
	    if(block.getNum()==1) return;

        log.debug("Block Number:{}",block.getNum());

        Map<String,Node> curVerifierMap = new HashMap<>();
        event.getEpochMessage().getCurVerifierList().forEach(v->curVerifierMap.put(v.getNodeId(),v));
        Map<String,Node> preVerifierMap = new HashMap<>();
        event.getEpochMessage().getPreVerifierList().forEach(v->preVerifierMap.put(v.getNodeId(),v));

        Settle settle = Settle.builder()
                .preVerifierSet(preVerifierMap.keySet())
                .curVerifierSet(curVerifierMap.keySet())
                .stakingReward(event.getEpochMessage().getPreStakeReward())
                .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .stakingLockEpoch(chainConfig.getUnStakeRefundSettlePeriodCount().intValue())
                .build();

        List<Integer> statusList = new ArrayList <>();
        statusList.add(1);
        statusList.add(2);
        StakingExample stakingExample = new StakingExample();
        stakingExample.createCriteria()
                .andStatusIn(statusList);
        List<Staking> stakingList = stakingMapper.selectByExampleWithBLOBs(stakingExample);
        stakingList.forEach(staking -> {
            //犹豫期金额变成锁定金额
            staking.setStakingLocked(staking.getStakingLocked().add(staking.getStakingHes()));
            staking.setStakingHes(BigDecimal.ZERO);
            //退出中记录状态设置（状态为退出中且已经经过指定的结算周期数，则把状态置为已退出）
            if(staking.getStatus() == 2 && staking.getStakingReductionEpoch() + settle.getStakingLockEpoch() < settle.getSettingEpoch()){
                staking.setStakingReduction(BigDecimal.ZERO);
                staking.setStatus(3);
            }
            //当前质押是上轮结算周期验证人,发放本结算周期的质押奖励, 奖励金额暂存至stakeReward变量
            BigDecimal curSettleStakeReward = BigDecimal.ZERO;
            if(settle.getPreVerifierSet().contains(staking.getNodeId())){
                curSettleStakeReward = settle.getStakingReward();
            }

            //当前质押是下轮结算周期验证人
            if(settle.getCurVerifierSet().contains(staking.getNodeId())){
                staking.setIsSettle(1);
            }else {
                staking.setIsSettle(2);
            }

            // 设置当前质押的总委托奖励，从节点上取出来的委托总奖励就是当前质押获取的总委托奖励
            Node node = preVerifierMap.get(staking.getNodeId());
            staking.setTotalDeleReward(new BigDecimal(node.getDelegateRewardTotal()));
            // 当前结算周期奖励分成比例
            staking.setRewardPer(node.getRewardPer().intValue());

            // 计算节点质押年化率
            calcStakeAnnualizedRate(staking,curSettleStakeReward,settle);
            // 计算委托年化率
            calcDelegateAnnualizedRate(staking,settle);
        });
        settle.setStakingList(stakingList);
        epochBusinessMapper.settle(settle);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
	}

    /**
     * 计算节点质押年化率
     * @param staking
     * @param curSettleStakeReward
     * @param settle
     */
	private void calcStakeAnnualizedRate(Staking staking,BigDecimal curSettleStakeReward,Settle settle){
        // 设置发放质押奖励后的金额，用于年化率计算
        staking.setStakingRewardValue(staking.getStakingRewardValue().add(curSettleStakeReward));
        // 解析年化率信息对象
        String ariString = staking.getAnnualizedRateInfo();
        AnnualizedRateInfo ari = StringUtils.isNotBlank(ariString)?JSON.parseObject(ariString,AnnualizedRateInfo.class):new AnnualizedRateInfo();
        if(ari.getStakeProfit()==null) ari.setStakeProfit(new ArrayList<>());
        if(ari.getStakeCost()==null) ari.setStakeCost(new ArrayList<>());
        if(ari.getSlash()==null) ari.setSlash(new ArrayList<>());

        // 如果当前节点在下一轮结算周期还是验证人,则记录下一轮结算周期的质押成本
        if(settle.getCurVerifierSet().contains(staking.getNodeId())){
            // 计算当前质押成本
            BigDecimal curSettleCost = staking.getStakingLocked() // 锁定的质押金
                    .add(staking.getStakingHes()) // 犹豫期的质押金
                    .add(staking.getStatDelegateHes()) // 犹豫期的委托金
                    .add(staking.getStatDelegateLocked()); // 锁定的委托金
            CalculateUtils.rotateCost(ari.getStakeCost(),curSettleCost,BigInteger.valueOf(settle.getSettingEpoch()),chainConfig);
        }
        // 如果当前节点在前一轮结算周期，则更新利润并计算年化率
        if(settle.getPreVerifierSet().contains(staking.getNodeId())){
            if(ari.getStakeProfit().isEmpty()) {
                // 设置0收益作为计算周期间收益的减数
                PeriodValueElement pv = new PeriodValueElement();
                // 例如当前是第6个周期，要得出5和6两个周期的利润和，则需要记录第4个周期末的利润，这样才可以用【第6个周期末的利润】-【第4个周期末的利润】计算出5和6两个周期的利润和
                pv.setPeriod(settle.getSettingEpoch()-2L);
                pv.setValue(BigDecimal.ZERO);
                List<PeriodValueElement> profits = new ArrayList<>();
                profits.add(pv);
                ari.setStakeProfit(profits);
            }
            if(ari.getSlash()==null) ari.setSlash(new ArrayList<>());
            // 对超出数量的收益轮换
            BigDecimal curSettleStakeProfit = staking.getStakingRewardValue().add(staking.getBlockRewardValue()).add(staking.getFeeRewardValue());
            CalculateUtils.rotateProfit(ari.getStakeProfit(),curSettleStakeProfit,BigInteger.valueOf(settle.getSettingEpoch()-1L),chainConfig);
            BigDecimal annualizedRate = CalculateUtils.calculateAnnualizedRate(ari.getStakeProfit(),ari.getStakeCost(),chainConfig);
            // 设置年化率
            staking.setAnnualizedRate(annualizedRate.doubleValue());
        }
        // 设置年化率计算原始数据
        staking.setAnnualizedRateInfo(ari.toJSONString());

        // 把当前staking的stakingRewardValue的值置为当前结算周期的质押奖励值，累加操作由mapper xmm中的SQL语句完成
        // staking表：【`staking_reward_value` =  `staking_reward_value` + #{staking.stakingRewardValue}】
        // node表：【`stat_staking_reward_value` = `stat_staking_reward_value` + #{staking.stakingRewardValue}】
        staking.setStakingRewardValue(curSettleStakeReward);
    }

    /**
     * 计算委托年化率
     * @param staking 当前质押记录
     * @param settle 周期切换业务参数
     */
    private void calcDelegateAnnualizedRate(Staking staking,Settle settle){
        //计算委托年化率
        // 解析年化率信息对象
        String ariString = staking.getAnnualizedRateInfo();
        AnnualizedRateInfo ari = StringUtils.isNotBlank(ariString)?JSON.parseObject(ariString,AnnualizedRateInfo.class):new AnnualizedRateInfo();
        if(ari.getDelegateProfit()==null) ari.setDelegateProfit(new ArrayList<>());
        if(ari.getDelegateCost()==null) ari.setDelegateCost(new ArrayList<>());

        // 如果当前节点在下一轮结算周期还是验证人,则记录下一轮结算周期的委托成本
        if(settle.getCurVerifierSet().contains(staking.getNodeId())){
            // 计算当前委托成本
            BigDecimal curSettleCost = staking.getStatDelegateLocked() // 锁定的委托
                    .add(staking.getStatDelegateHes()); // 犹豫期的委托金
            CalculateUtils.rotateCost(ari.getDelegateCost(),curSettleCost,BigInteger.valueOf(settle.getSettingEpoch()),chainConfig);
        }
        // 如果当前节点在前一轮结算周期，则更新利润并计算年化率
        if(settle.getPreVerifierSet().contains(staking.getNodeId())){
            if(ari.getDelegateProfit().isEmpty()) {
                // 设置0收益作为计算周期间收益的减数
                PeriodValueElement pv = new PeriodValueElement();
                // 例如当前是第6个周期，要得出5和6两个周期的利润和，则需要记录第4个周期末的利润，这样才可以用【第6个周期末的利润】-【第4个周期末的利润】计算出5和6两个周期的利润和
                pv.setPeriod(settle.getSettingEpoch()-2L);
                pv.setValue(BigDecimal.ZERO);
                List<PeriodValueElement> profits = new ArrayList<>();
                profits.add(pv);
                ari.setDelegateProfit(profits);
            }
            // 对超出数量的收益轮换
            BigDecimal curSettleDelegateProfit = staking.getTotalDeleReward();
            CalculateUtils.rotateProfit(ari.getDelegateProfit(),curSettleDelegateProfit,BigInteger.valueOf(settle.getSettingEpoch()-1L),chainConfig);
            BigDecimal annualizedRate = CalculateUtils.calculateAnnualizedRate(ari.getDelegateProfit(),ari.getDelegateCost(),chainConfig);
            // 设置年化率
            staking.setDeleAnnualizedRate(annualizedRate.doubleValue());
        }
        // 设置年化率计算原始数据
        staking.setAnnualizedRateInfo(ari.toJSONString());
    }
}
