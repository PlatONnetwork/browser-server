package com.platon.browser.complement.service.param.converter;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.complement.dto.AnnualizedRateInfo;
import com.platon.browser.complement.dao.param.epoch.Settle;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.Block;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class OnSettleConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private StakingMapper stakingMapper;

	public void convert(CollectionEvent event, Block block) throws Exception {
        List<String> curVerifierList = new ArrayList<>();
        event.getEpochMessage().getCurVerifierList().forEach(v->curVerifierList.add(v.getNodeId()));
        List<String> preVerifierList = new ArrayList<>();
        event.getEpochMessage().getPreVerifierList().forEach(v->preVerifierList.add(v.getNodeId()));

        Settle settle = Settle.builder()
                .preVerifierList(preVerifierList)
                .curVerifierList(curVerifierList)
                .stakingReward(new BigDecimal(event.getEpochMessage().getStakeReward()))
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
        //todo:stakinglist为空
        stakingList.forEach(staking -> {
            //犹豫期金额
            staking.setStakingLocked(staking.getStakingLocked().add(staking.getStakingHes()));
            staking.setStakingHes(BigDecimal.ZERO);
            //退出中记录状态设置
            if(staking.getStatus() == 2 && staking.getStakingReductionEpoch() + settle.getStakingLockEpoch() < settle.getSettingEpoch()){
                staking.setStakingReduction(BigDecimal.ZERO);
                staking.setStatus(3);
            }
            //当前质押是上轮结算周期验证人,发放质押奖励
            if(preVerifierList.contains(staking.getNodeId())){
                staking.setFeeRewardValue(settle.getStakingReward());
            }else {
                staking.setFeeRewardValue(BigDecimal.ZERO);
            }
            //当前质押是下轮结算周期验证人
            if(curVerifierList.contains(staking.getNodeId())){
                staking.setIsSettle(1);
            }else {
                staking.setIsSettle(2);
            }
            //计算年化率
            AnnualizedRateInfo ari = AnnualizedRateInfo.builder()
                    .cost(new ArrayList<>())
                    .profit(new ArrayList<>())
                    .slash(new ArrayList<>())
                    .build();
            String ariString = staking.getAnnualizedRateInfo();
            if(StringUtils.isNotBlank(ariString)){
                ari = JSON.parseObject(ariString,AnnualizedRateInfo.class);
                if(ari.getProfit()==null) ari.setProfit(new ArrayList<>());
                if(ari.getSlash()==null) ari.setSlash(new ArrayList<>());
            }
            // 对超出数量的收益轮换
            CalculateUtils.rotateProfit(staking,BigInteger.valueOf(settle.getSettingEpoch()),ari,chainConfig);
            BigDecimal annualizedRate = CalculateUtils.calculateAnnualizedRate(ari,chainConfig.getSettlePeriodCountPerIssue());
            staking.setAnnualizedRate(annualizedRate.doubleValue());

            // 如果当前节点在下一轮结算周期还是验证人,则记录下下一轮结算周期的成本
            if(curVerifierList.contains(staking.getNodeId())){
                CalculateUtils.rotateCost(staking,BigInteger.valueOf(settle.getSettingEpoch()),ari,chainConfig);
            }

            staking.setAnnualizedRateInfo(ari.toJSONString());
        });
        settle.setStakingList(stakingList);
        epochBusinessMapper.settle(settle);
	}

}
