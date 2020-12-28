package com.platon.browser.service.erc20;

import com.alaya.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

public interface Erc20Service {
    Erc20Contract init(String contractAddress);
    BigInteger getBalance(String contractAddress, String account);
    String getName(String contractAddress);
    String getSymbol(String contractAddress);
    BigInteger getDecimals(String contractAddress);
    BigInteger getTotalSupply(String contractAddress);
    ERCData getErcData(String contractAddress);
    List<TransferEvent> getTransferEvents(TransactionReceipt transactionReceipt);
    List<TransferEvent> getApprovalEvents(TransactionReceipt transactionReceipt);
}
