package com.platon.browser.bootstrap.bean;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SyncData {

    private Set<Block> blockSet = new HashSet<>();

    private Set<Transaction> txBakSet = new HashSet<>();

    private Set<ErcTx> erc20BakSet = new HashSet<>();

    private Set<ErcTx> erc721BakSet = new HashSet<>();

    private Set<DelegationReward> delegationRewardBakSet = new HashSet<>();

}
