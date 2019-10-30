package com.platon.browser.queue.event.collection;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * 当前区块相关的所有事件信息(共识周期切换事件/结算周期切换事件/增发周期切换事件)
 */
@Data
@Slf4j
public class EpochMessage {
    private BigInteger blockNumber;
    private ConsensusEpochEvent consensusEpochEvent=new ConsensusEpochEvent();
    private SettlementEpochEvent settlementEpochEvent=new SettlementEpochEvent();
    public EpochMessage(BigInteger blockNumber, PlatOnClient platOnClient, SpecialContractApi specialContractApi){
        this.blockNumber = blockNumber;
        consensusEpochEvent.init(blockNumber,platOnClient,specialContractApi);
        settlementEpochEvent.init(blockNumber,platOnClient,specialContractApi);
    }
}
