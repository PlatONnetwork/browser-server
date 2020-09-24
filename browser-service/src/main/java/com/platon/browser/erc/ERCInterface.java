package com.platon.browser.erc;

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.platon.browser.dto.ERCData;
import com.platon.browser.dto.TransferEvent;

public interface ERCInterface {

    String getName();

    String getSymbol();

    BigInteger getDecimals();

    BigInteger getTotalSupply();

    ERCData getErcData();

    List<TransferEvent> getTransferEvents(TransactionReceipt transactionReceipt);

    List<TransferEvent> getApprovalEvents(TransactionReceipt transactionReceipt);

}
