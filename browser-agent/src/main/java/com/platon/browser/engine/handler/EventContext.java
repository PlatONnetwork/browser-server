package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.engine.stage.StakingStage;
import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:12
 * @Description:
 */
@Data
public class EventContext {
    private CustomTransaction transaction;
    private BlockChain blockChain;
    private NodeCache nodeCache;
    private StakingStage stakingStage;
    private ProposalStage proposalStage;

    public EventContext(CustomTransaction transaction, BlockChain blockChain, NodeCache nodeCache, StakingStage stakingStage,
                        ProposalStage proposalStage) {
        this.transaction = transaction;
        this.blockChain = blockChain;
        this.nodeCache = nodeCache;
        this.stakingStage = stakingStage;
        this.proposalStage = proposalStage;
    }

}
