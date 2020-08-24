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
public class RewardEvent {
    private List <DelegationReward> delegationRewards;
}