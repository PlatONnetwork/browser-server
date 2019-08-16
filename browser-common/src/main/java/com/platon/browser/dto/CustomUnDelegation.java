package com.platon.browser.dto;

import com.platon.browser.dao.entity.UnDelegation;
import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description:
 */
@Data
public class CustomUnDelegation extends UnDelegation {

    public String getDelegationMapKey(){
        return this.getDelegateAddr()+this.getNodeId()+this.getStakingBlockNum();
    }
}
