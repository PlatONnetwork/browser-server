package com.platon.browser.erc.client;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.converter.TransferEventConverter;
import com.platon.browser.dto.ERCData;
import com.platon.browser.dto.TransferEvent;
import com.platon.browser.erc.ERCInterface;
import com.platon.browser.utils.NetworkParms;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-23 10:36
 */
@Data
@Slf4j
@Component
public class ERCClient implements ERCInterface {

    @Autowired
    private PlatOnClient platOnClient;

    private String contractAddress;

    private ERC20Client erc20Client;

    private Lock lock = new ReentrantLock();

    private void init() {
        if (StringUtils.isBlank(this.contractAddress)) {
            throw new RuntimeException("contractAddress is not null");
        }
        if (this.erc20Client == null) {
            try {
                this.lock.lock();
                this.erc20Client = new ERC20Client(this.contractAddress, this.platOnClient.getWeb3jWrapper().getWeb3j(),
                    null, NetworkParms.getChainId());
            } finally {
                this.lock.unlock();
            }
        }
    }

    @Override
    public String getName() {
        this.init();
        String name = "";
        try {
            name = this.erc20Client.name().send();
        } catch (Exception e) {
            log.error(" erc get name error", e);
        }
        return name;
    }

    @Override
    public String getSymbol() {
        this.init();
        String symbol = "";
        try {
            symbol = this.erc20Client.symbol().send();
        } catch (Exception e) {
            log.error(" erc get symbol error", e);
        }
        return symbol;
    }

    @Override
    public BigInteger getDecimals() {
        this.init();
        BigInteger decimal = null;
        try {
            decimal = this.erc20Client.decimals().send();
        } catch (Exception e) {
            log.error(" erc get decimal error", e);
        }
        return decimal;
    }

    @Override
    public BigInteger getTotalSupply() {
        this.init();
        BigInteger totalSupply = null;
        try {
            totalSupply = this.erc20Client.totalSupply().send();
        } catch (Exception e) {
            log.error(" erc get totalSupply error", e);
        }
        return totalSupply;
    }

    @Override
    public ERCData getErcData() {
        String name = this.getName();
        String symbol = this.getSymbol();
        BigInteger decimals = this.getDecimals();
        BigInteger totalSupply = this.getTotalSupply();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(symbol) || decimals == null || totalSupply == null) {
            return null;
        }
        ERCData ercData = new ERCData();
        ercData.setName(name);
        ercData.setDecimal(decimals);
        ercData.setSymbol(symbol);
        ercData.setTotalSupply(totalSupply);
        return ercData;
    }

    @Override
    public List<TransferEvent> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<TransferEvent> transferEvents = null;
        try {
            List<ERC20Client.TransferEventResponse> transferEventResponses =
                this.erc20Client.getTransferEvents(transactionReceipt);
            transferEvents = TransferEventConverter.INSTANCE.domain2dto(transferEventResponses);
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }

    @Override
    public List<TransferEvent> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<TransferEvent> transferEvents = null;
        try {
            List<ERC20Client.ApprovalEventResponse> approvalEventResponses =
                this.erc20Client.getApprovalEvents(transactionReceipt);
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }

}
