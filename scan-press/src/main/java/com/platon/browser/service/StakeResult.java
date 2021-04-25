package com.platon.browser.service;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import lombok.Data;

import java.util.List;

@Data
public class StakeResult {

    private Node node;

    private Staking staking;

    private List<Staking> stakingList;

}
