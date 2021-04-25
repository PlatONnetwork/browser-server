package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.DelegationReward;

import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class RewardEvent implements Event{
    private List <DelegationReward> delegationRewards;
}