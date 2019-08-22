package com.platon.browser.engine.result;

import com.platon.browser.dto.CustomNetworkStat;
import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/10
 * Time: 18:07
 */
@Data
public class BlockChainResult {
    private ProposalExecuteResult proposalExecuteResult=new ProposalExecuteResult();
    private StakingExecuteResult stakingExecuteResult=new StakingExecuteResult();
    private AddressExecuteResult addressExecuteResult =new AddressExecuteResult();
    private NetworkStatResult networkStatResult =new NetworkStatResult();

    public void clear(){
        proposalExecuteResult.clear();
        stakingExecuteResult.clear();
        addressExecuteResult.clear();
        networkStatResult.clear();
    }
}
