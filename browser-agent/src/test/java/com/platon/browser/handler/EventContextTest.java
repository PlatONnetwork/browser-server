package com.platon.browser.handler;

import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.engine.stage.StakingStage;
import lombok.Data;

/**
 * @Auther: dongqile
 * @Date: 2019/9/6
 * @Description:
 */
@Data
public class EventContextTest {
    private CustomTransaction transaction;
    private StakingStage stakingStage;
    private ProposalStage proposalStage;
    private AddressStage addressStage;

    public EventContextTest ( CustomTransaction transaction, StakingStage stakingStage, ProposalStage proposalStage, AddressStage addressStage ) {
        this.transaction = transaction;
        this.stakingStage = stakingStage;
        this.proposalStage = proposalStage;
        this.addressStage = addressStage;
    }
}