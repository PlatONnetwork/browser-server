package com.platon.browser.dto;

import com.platon.browser.dao.entity.Staking;
import org.springframework.beans.BeanUtils;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/13 14:36
 * @Description:
 */
public class StakingInfo extends Staking {
    public StakingInfo(org.web3j.platon.bean.Node initData){
        BeanUtils.copyProperties(initData,this);
        if(initData.getStakingTxIndex()!=null) this.setStakingTxIndex(initData.getStakingTxIndex().intValue());
        this.setStakingAddr(initData.getStakingAddress());
        // TODO:
        //this.setStakingHas(initData.get);
        // TODO:
        //this.setStakingLocked(initData.gets);
        //this.setStatDelegateHas(initData.getReleasedHes());
        //this.setStatDelegateLocked();
        //this.setStatDelegateReduction();
        //this.setStatDelegateQty();
        //this.setStatVerifierTime(initDa);
        //this.setStakingIcon(initData.getExternalId());
        this.setDenefitAddr(initData.getBenifitAddress());
        //this.setExpectedIncome(initData.ge);
        //this.setBlockRewardValue(initData.getr);
        //this.setPreSetBlockRewardValue(initData.get);
        //this.setCurConsBlockQty(initDa);
        //this.setPreConsBlockQty();
        //this.setStakingRewardValue(initData.gets);
        //this.setJoinTime(initData.get);
        //this.setLeaveTime(initData.g);
        if(initData.getStatus()!=null) this.setStatus(initData.getStatus().intValue());
        //this.setIsConsensus(initData.gets);
        if(initData.getStakingEpoch()!=null) this.setStakingEpoch(initData.getStakingEpoch().intValue());
        //this.setStakingReduction(initData.getsta);
        this.setStakingName(initData.getNodeName());
        //this.setIsInit();

    }
}
