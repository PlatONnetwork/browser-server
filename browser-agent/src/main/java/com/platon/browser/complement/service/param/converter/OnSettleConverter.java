package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.param.epoch.Settle;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.StakingMapper;

@Service
public class OnSettleConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private StakingMapper stakingMapper;
	
	public void convert(CollectionEvent event,CollectionBlock block) throws Exception {
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
        List<Staking> stakingList = stakingMapper.selectByExample(stakingExample);
        stakingList.forEach(staking -> {
            staking.setStakingLocked(staking.getStakingLocked().add(staking.getStakingHes()));
            staking.setStakingHes(BigDecimal.ZERO);
            if(staking.getStatus() == 2 && staking.getStakingReductionEpoch() + settle.getStakingLockEpoch() < settle.getSettingEpoch()){
                staking.setStakingReduction(BigDecimal.ZERO);
                staking.setStatus(3);
            }

        });
        epochBusinessMapper.settle(settle);
	}

}
