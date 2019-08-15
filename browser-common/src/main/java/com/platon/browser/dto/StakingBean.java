package com.platon.browser.dto;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.json.CreateValidatorDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
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
        // 初始化默认值
        this.setStakingLocked("0");
        this.setStakingReduction("0");
        this.setStakingEpoch(0);
        this.setStatDelegateHas("0");
        this.setStatDelegateLocked("0");
        this.setStatDelegateReduction("0");
        this.setStatDelegateQty(0);
        this.setStatVerifierTime(0);
        this.setIsSetting(0);
        this.setIsInit(0);
        this.setIsConsensus(0);
        this.setStakingHas("0");
        this.setStatus(0);
    }

    // <质押块高-质押记录> 映射
    private Map<String, DelegationBean> delegations = new HashMap<>();

    // staking与delegation的关联键
    public String getStakingMapKey(){
        return this.getNodeId()+this.getStakingBlockNum();
    }


    public void initWithNode(org.web3j.platon.bean.Node initData){
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
        BeanUtils.copyProperties(initData,this);
        // 质押区块高度
        if(initData.getStakingBlockNum()!=null) this.setStakingBlockNum(initData.getStakingBlockNum().longValue());
        // 质押节点地址
        this.setNodeId(this.getNodeId().startsWith("0x")?this.getNodeId():"0x"+this.getNodeId());
        // 发起质押交易的索引
        if(initData.getStakingTxIndex()!=null) this.setStakingTxIndex(initData.getStakingTxIndex().intValue());
        // 发起质押的账户地址
        this.setStakingAddr(initData.getStakingAddress());
        // 第三方社交软件关联id
        this.setExternalId(initData.getExternalId());
        // 收益地址
        this.setDenefitAddr(initData.getBenifitAddress());
        // 节点状态 1：候选中 2：退出中 3：已退出
        if(initData.getStatus()!=null) this.setStatus(initData.getStatus().intValue());
        // 结算周期标识
        if(initData.getStakingEpoch()!=null) this.setStakingEpoch(initData.getStakingEpoch().intValue());
        // 节点名称(质押节点名称)
        this.setStakingName(initData.getNodeName());
        // 节点的第三方主页
        this.setWebSite(initData.getWebsite());
    }

    public void initWithCreateValidatorDto(TransactionBean initData){
        BeanUtils.copyProperties(initData,this);
        this.setStakingTxIndex(initData.getTransactionIndex());
        // 发起质押的账户地址
        this.setStakingAddr(initData.getFrom());
        // 质押金额(犹豫期金额)
        this.setStakingHas(initData.getValue());
        this.setStakingName(initData.getTxJson(CreateValidatorDto.class).getNodeName());
    }
}
