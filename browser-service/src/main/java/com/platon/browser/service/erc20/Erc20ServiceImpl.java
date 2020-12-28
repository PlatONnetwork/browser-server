package com.platon.browser.service.erc20;

import com.alaya.crypto.Credentials;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.tx.exceptions.ContractCallException;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.utils.NetworkParms;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: browser-server
 * @description: erc查询余额接口
 * @author: Rongjin Zhang
 * @create: 2020-10-12 11:48
 */
@Slf4j
@Component
public class Erc20ServiceImpl implements Erc20Service {

    @Resource
    private PlatOnClient platOnClient;

    public Erc20Contract init(String contractAddress) {
        if (StringUtils.isBlank(contractAddress)) {
            throw new RuntimeException("contractAddress is not null");
        }
        Erc20Contract erc20Contract = new Erc20Contract(contractAddress, platOnClient.getWeb3jWrapper().getWeb3j(),
            Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
            NetworkParms.getChainId());
        return erc20Contract;
    }

    @Override
    public BigInteger getBalance(String contractAddress, String account) {
        Erc20Contract erc20Contract = this.init(contractAddress);
        BigInteger balance = BigInteger.ZERO;
        try {
            balance = erc20Contract.balanceOf(account).send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get name error", e);
        }
        return balance;
    }

    @Override
    public String getName(String contractAddress) {
        Erc20Contract erc20Contract = this.init(contractAddress);
        String name = "";
        try {
            name = erc20Contract.name().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress, e);
        } catch (Exception e) {
            log.error(" erc get name error", e);
        }
        return name;
    }

    @Override
    public String getSymbol(String contractAddress) {
        Erc20Contract erc20Contract = this.init(contractAddress);
        String symbol = "";
        try {
            symbol = erc20Contract.symbol().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get symbol error", e);
        }
        return symbol;
    }

    @Override
    public BigInteger getDecimals(String contractAddress) {
        Erc20Contract erc20Contract = this.init(contractAddress);
        BigInteger decimal = null;
        try {
            decimal = erc20Contract.decimals().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get decimal error", e);
        }
        return decimal;
    }

    @Override
    public BigInteger getTotalSupply(String contractAddress) {
        Erc20Contract erc20Contract = this.init(contractAddress);
        BigInteger totalSupply = null;
        try {
            totalSupply = erc20Contract.totalSupply().send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get totalSupply error", e);
        }
        return totalSupply;
    }

    @Override
    public ERCData getErcData(String contractAddress) {
        String name = this.getName(contractAddress);
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String symbol = this.getSymbol(contractAddress);
        BigInteger decimals = this.getDecimals(contractAddress);
        BigInteger totalSupply = this.getTotalSupply(contractAddress);
        if (StringUtils.isBlank(symbol) || decimals == null || totalSupply == null) {
            return null;
        }
        ERCData ercData = new ERCData();
        ercData.setName(name);
        ercData.setDecimal(decimals.intValue());
        ercData.setSymbol(symbol);
        ercData.setTotalSupply(totalSupply);
        return ercData;
    }

    @Override
    public List<TransferEvent> getTransferEvents(TransactionReceipt transactionReceipt) {
        Erc20Contract erc20Contract = this.init(transactionReceipt.getContractAddress());
        List<TransferEvent> transferEvents = new ArrayList<>();
        try {
            List<Erc20Contract.TransferEventResponse> transferEventResponses =
                    erc20Contract.getTransferEvents(transactionReceipt);
            transferEventResponses.forEach(transferEventRespon -> {
                TransferEvent transferEvent = new TransferEvent();
                transferEvent.setFrom(transferEventRespon.from);
                transferEvent.setTo(transferEventRespon.to);
                transferEvent.setValue(transferEventRespon.value);
                transferEvent.setLog(transferEventRespon.log);
                transferEvents.add(transferEvent);
            });
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }

    @Override
    public List<TransferEvent> getApprovalEvents(TransactionReceipt transactionReceipt) {
        Erc20Contract erc20Contract = this.init(transactionReceipt.getContractAddress());
        List<TransferEvent> transferEvents = null;
        try {
            List<Erc20Contract.ApprovalEventResponse> approvalEventResponses =
                    erc20Contract.getApprovalEvents(transactionReceipt);
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }
}
