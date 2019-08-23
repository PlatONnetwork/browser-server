package com.platon.browser.engine.stage;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/10
 * Time: 18:07
 */
@Data
public class BlockChainResult {
    private ProposalStage proposalStage=new ProposalStage();
    private StakingStage stakingStage=new StakingStage();
    private AddressStage addressStage =new AddressStage();
    private NetworkStatStage networkStatStage =new NetworkStatStage();

    public void clear(){
        proposalStage.clear();
        stakingStage.clear();
        addressStage.clear();
        networkStatStage.clear();
    }
}
