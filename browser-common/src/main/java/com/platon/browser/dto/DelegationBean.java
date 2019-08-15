package com.platon.browser.dto;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.UnDelegation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description:
 */
@Data
public class DelegationBean extends Delegation {

    private List<UnDelegation> unDelegations = new ArrayList<>();

    // delegation与staking的关联键
    public String getStakingMapKey(){
        return this.getNodeId()+this.getStakingBlockNum();
    }

    // delegation与un_delegation的关联键
    public String getDelegationMapKey(){
        return this.getDelegateAddr()+this.getNodeId()+this.getStakingBlockNum();
    }
}
