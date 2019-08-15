package com.platon.browser.dto;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.json.CreateValidatorDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/13 14:36
 * @Description:
 */
@Data
public class StakingBean extends Staking {

    public StakingBean() {
    }

    // <质押块高-质押记录> 映射
    private Map<String, DelegationBean> delegations = new HashMap<>();

    public String getMapKey(){
        return this.getNodeId()+this.getStakingBlockNum();
    }

    public void initWithNode(org.web3j.platon.bean.Node initData){
        BeanUtils.copyProperties(initData,this);
        if(initData.getStakingTxIndex()!=null) this.setStakingTxIndex(initData.getStakingTxIndex().intValue());
        this.setNodeId(this.getNodeId().startsWith("0x")?this.getNodeId():"0x"+this.getNodeId());
        this.setStakingAddr(initData.getStakingAddress());
        this.setDenefitAddr(initData.getBenifitAddress());

        if(initData.getStatus()!=null) this.setStatus(initData.getStatus().intValue());
        //this.setIsConsensus(initData.gets);
        if(initData.getStakingEpoch()!=null) this.setStakingEpoch(initData.getStakingEpoch().intValue());
        //this.setStakingReduction(initData.getsta);
        this.setStakingName(initData.getNodeName());

        this.setStakingBlockNum(1l);

        this.setStakingLocked("0");
        this.setStakingReduction("0");
        this.setStakingEpoch(0);
        this.setStatDelegateHas("0");
        this.setStatDelegateLocked("0");
        this.setStatDelegateReduction("0");
        this.setStatDelegateQty(0);
        this.setStatVerifierTime(0);

    }

    public void initWithCreateValidatorDto(TransactionBean initData){
        BeanUtils.copyProperties(initData,this);
        this.setStakingTxIndex(initData.getTransactionIndex());
        // 发起质押的账户地址
        this.setStakingAddr(initData.getFrom());
        // 质押金额(犹豫期金额)
        this.setStakingHas(initData.getValue());
        this.setStakingLocked("0");
        this.setStakingReduction("0");
        this.setStakingEpoch(0);
        this.setStatDelegateHas("0");
        this.setStatDelegateLocked("0");
        this.setStatDelegateReduction("0");
        this.setStatDelegateQty(0);
        this.setStatVerifierTime(0);
        this.setStakingName(initData.getTxJson(CreateValidatorDto.class).getNodeName());


        // TODO:


        //this.setStatDelegateLocked();
        //this.setStatDelegateReduction();
        //this.setStatDelegateQty();
        //this.setStatVerifierTime(initDa);
        //this.setStakingIcon(initData.getExternalId());
        //this.setDenefitAddr(initData.getBenifitAddress());
        //this.setExpectedIncome(initData.ge);
        //this.setBlockRewardValue(initData.getr);
        //this.setPreSetBlockRewardValue(initData.get);
        //this.setCurConsBlockQty(initDa);
        //this.setPreConsBlockQty();
        //this.setStakingRewardValue(initData.gets);
        //this.setJoinTime(initData.get);
        //this.setLeaveTime(initData.g);
        //if(initData.getStatus()!=null) this.setStatus(initData.getStatus().intValue());
        //this.setIsConsensus(initData.gets);

        //this.setStakingName(initData.getNodeName());
        //this.setIsInit();

    }
}
