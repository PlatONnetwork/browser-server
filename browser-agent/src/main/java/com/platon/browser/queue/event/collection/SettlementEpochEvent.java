package com.platon.browser.queue.event.collection;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Data
public class SettlementEpochEvent extends EpochEvent {

    private BigInteger perBlockReward;

    /**
     * 取当前共识周期的奖励总和
     * @return
     */
    public BigInteger getSettlementReward(){
        return this.getPeriodBlockReward();
    }

    /**
     * 取当前共识周期内每个区块的奖励
     * @return
     */
    public BigInteger getPerBlockReward(){
        return new BigDecimal(this.getPeriodBlockReward())
                .divide(BigDecimal.valueOf(this.getCurValidators().size()),0, RoundingMode.FLOOR)
                .toBigInteger();
    }
    @Override
    public String toString() {
        return "";
    }

    @Override
    public void init(BigInteger blockNumber, PlatOnClient platOnClient, SpecialContractApi specialContractApi) {

    }
}
