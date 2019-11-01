package com.platon.browser.common.dto;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.List;

/**
 * 当前区块周期切换事件
 */
@Data
@Slf4j
public abstract class EpochEvent {
    // 当前区块号
    private BigInteger blockNumber;
    // 当前所处周期,从1开始
    private Long epoch;
    // 当前周期的验证人
    private List<Node> candidates;
    // 周期区块奖励
    private BigInteger periodBlockReward;
    // 周期质押奖励
    private BigInteger periodStakingReward;

    public abstract void init(BigInteger blockNumber, PlatOnClient platOnClient, SpecialContractApi specialContractApi);
}
