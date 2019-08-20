package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.engine.result.StakingExecuteResult;
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
    private StakingExecuteResult executeResult;
    private ProposalExecuteResult proposalExecuteResult;

    public EventContext(CustomTransaction transaction, BlockChain blockChain, NodeCache nodeCache, StakingExecuteResult executeResult,
                        ProposalExecuteResult proposalExecuteResult) {
        this.transaction = transaction;
        this.blockChain = blockChain;
        this.nodeCache = nodeCache;
        this.executeResult = executeResult;
        this.proposalExecuteResult = proposalExecuteResult;
    }

}
