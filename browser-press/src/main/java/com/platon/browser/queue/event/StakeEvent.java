package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Staking;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
public class StakeEvent {
    private List <Staking> stakingList;

    public List<Staking> getStakingList() {
        return stakingList;
    }

    public void setStakingList(List<Staking> stakingList) {
        this.stakingList = stakingList;
    }
}