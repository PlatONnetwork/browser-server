package com.platon.browser.bean;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.utils.FilterTool;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.Date;

public class BlockBean extends Block {
    public void init(EthBlock initData){
        this.setNumber(initData.getBlock().getNumber().longValue());
        if (String.valueOf(initData.getBlock().getTimestamp().longValue()).length() == 10) {
            this.setTimestamp(new Date(initData.getBlock().getTimestamp().longValue() * 1000L));
        } else {
            this.setTimestamp(new Date(initData.getBlock().getTimestamp().longValue()));
        }
        this.setSize(initData.getBlock().getSize().intValue());
        if (initData.getBlock().getTransactions().size() <= 0) {
            this.setEnergonAverage(BigInteger.ZERO.toString());
        } else {
            this.setEnergonAverage(initData.getBlock().getGasUsed().divide(new BigInteger(String.valueOf(initData.getBlock().getTransactions().size()))).toString());
        }
        this.setHash(initData.getBlock().getHash());
        this.setEnergonLimit(initData.getBlock().getGasLimit().toString());
        this.setEnergonUsed(initData.getBlock().getGasUsed().toString());
        this.setTransactionNumber(initData.getBlock().getTransactions().size() > 0 ? initData.getBlock().getTransactions().size() : new Integer(0));
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setMiner(initData.getBlock().getMiner());
        this.setExtraData(initData.getBlock().getExtraData());
        this.setParentHash(initData.getBlock().getParentHash());
        String ticketId = FilterTool.ticketIdAnalysis(this.getExtraData());
        this.setTicketId(ticketId);
        this.setNonce(initData.getBlock().getNonce().toString());
        String rewardWei = FilterTool.getBlockReward(initData.getBlock().getNumber().toString());
        this.setBlockReward(rewardWei);
    }
}
