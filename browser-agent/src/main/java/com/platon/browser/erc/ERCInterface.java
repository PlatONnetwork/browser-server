package com.platon.browser.erc;

import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.dto.ERCData;
import com.platon.browser.dto.TransferEvent;
import com.platon.browser.elasticsearch.dto.Transaction;

import java.math.BigInteger;
import java.util.List;

public interface ERCInterface {

    String getName(String contractAddress);

    String getSymbol(String contractAddress);

    BigInteger getDecimals(String contractAddress);

    BigInteger getTotalSupply(String contractAddress);

    ERCData getErcData(String contractAddress);

    List<TransferEvent> getTransferEvents(TransactionReceipt transactionReceipt);

    List<TransferEvent> getApprovalEvents(TransactionReceipt transactionReceipt);

    /**
     * 回填对应的erc20合约参数信息
     * 
     * @param transactions
     * @param addressCache
     */
    void initContractData(List<Transaction> transactions, AddressCache addressCache);

}
