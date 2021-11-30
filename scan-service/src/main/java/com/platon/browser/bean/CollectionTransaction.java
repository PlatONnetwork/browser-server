package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class CollectionTransaction extends Transaction {
    private CollectionTransaction() {}

    public static CollectionTransaction newInstance() {
        Date date = new Date();
        CollectionTransaction transaction = new CollectionTransaction();
        transaction.setCreTime(date).setUpdTime(date).setCost(BigDecimal.ZERO.toString())
            .setGasLimit(BigDecimal.ZERO.toString()).setGasPrice(BigDecimal.ZERO.toString())
            .setGasUsed(BigDecimal.ZERO.toString()).setStatus(StatusEnum.FAILURE.getCode())
            .setValue(BigDecimal.ZERO.toString()).setIndex(0);
        return transaction;
    }

    public CollectionTransaction updateWithBlock(Block block) {
        this.setTime(block.getTime());
        return this;
    }

    public CollectionTransaction updateWithRawTransaction(com.platon.protocol.core.methods.response.Transaction transaction) {
        this.setNum(transaction.getBlockNumber().longValue()).setBHash(transaction.getBlockHash())
            .setHash(transaction.getHash()).setValue(transaction.getValue().toString())
            .setIndex(transaction.getTransactionIndex().intValue()).setGasPrice(transaction.getGasPrice().toString())
            .setInput(transaction.getInput()).setTo(transaction.getTo()).setFrom(transaction.getFrom())
            .setGasLimit(transaction.getGas().toString()).setNonce(transaction.getNonce().toString());
        return this;
    }
}
