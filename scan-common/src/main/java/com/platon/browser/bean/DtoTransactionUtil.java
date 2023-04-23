package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class DtoTransactionUtil {
    private DtoTransactionUtil() {}

    public static Transaction newDtoTransaction() {
        Date date = new Date();
        Transaction transaction = new Transaction();
        transaction.setCreTime(date).setUpdTime(date).setCost(BigDecimal.ZERO.toString())
            .setGasLimit(BigDecimal.ZERO.toString()).setGasPrice(BigDecimal.ZERO.toString())
            .setGasUsed(BigDecimal.ZERO.toString()).setStatus(Transaction.StatusEnum.FAILURE.getCode())
            .setValue(BigDecimal.ZERO.toString()).setIndex(0);
        return transaction;
    }

    public static void updateWithBlock(Block block, Transaction dtoTransaction) {
        dtoTransaction.setTime(block.getTime());
     }

    public static void updateWithRawTransaction(com.platon.protocol.core.methods.response.Transaction rawTransaction, Transaction dtoTransaction) {
        dtoTransaction.setNum(rawTransaction.getBlockNumber().longValue()).setBHash(rawTransaction.getBlockHash())
            .setHash(rawTransaction.getHash()).setValue(rawTransaction.getValue().toString())
            .setIndex(rawTransaction.getTransactionIndex().intValue()).setGasPrice(rawTransaction.getGasPrice().toString())
            .setInput(rawTransaction.getInput()).setTo(rawTransaction.getTo()).setFrom(rawTransaction.getFrom())
            .setGasLimit(rawTransaction.getGas().toString()).setNonce(rawTransaction.getNonce().toString());
    }
}
