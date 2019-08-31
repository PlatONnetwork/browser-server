package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.stage.AddressStage;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.engine.stage.StakingStage;
import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:12
 * @Description: 事件上下文
 */
@Data
public class EventContext {
    private CustomTransaction transaction;
    private StakingStage stakingStage;
    private ProposalStage proposalStage;
    private AddressStage addressStage;
}
