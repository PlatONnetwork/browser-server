package com.platon.browser.engine;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/10
 * Time: 18:07
 */
@Data
public class BlockChainResult {
    private ProposalExecuteResult proposalExecuteResult;
    private StakingExecuteResult stakingExecuteResult;
}