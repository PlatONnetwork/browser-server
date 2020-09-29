package com.platon.browser.erc.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.dto.ERCData;
import com.platon.browser.dto.TransferEvent;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.erc.ERCInterface;
import com.platon.browser.param.Erc20Param;
import com.platon.browser.utils.NetworkParms;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-23 10:36
 */
@Slf4j
@Component
public class ERCClient implements ERCInterface {

    @Autowired
    private PlatOnClient platOnClient;

    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    private ERC20Client erc20Client;

    private Lock lock = new ReentrantLock();

    private void init(String contractAddress) {
        if (StringUtils.isBlank(contractAddress)) {
            throw new RuntimeException("contractAddress is not null");
        }
        if (this.erc20Client == null) {
            try {
                this.lock.lock();
                this.erc20Client = new ERC20Client(contractAddress, this.platOnClient.getWeb3jWrapper().getWeb3j(),
                    Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
                    NetworkParms.getChainId());
            } finally {
                this.lock.unlock();
            }
        }
    }

    @Override
    public String getName(String contractAddress) {
        this.init(contractAddress);
        String name = "";
        try {
            name = this.erc20Client.name().send();
        } catch (Exception e) {
            log.error(" erc get name error", e);
        }
        return name;
    }

    @Override
    public String getSymbol(String contractAddress) {
        this.init(contractAddress);
        String symbol = "";
        try {
            symbol = this.erc20Client.symbol().send();
        } catch (Exception e) {
            log.error(" erc get symbol error", e);
        }
        return symbol;
    }

    @Override
    public BigInteger getDecimals(String contractAddress) {
        this.init(contractAddress);
        BigInteger decimal = null;
        try {
            decimal = this.erc20Client.decimals().send();
        } catch (Exception e) {
            log.error(" erc get decimal error", e);
        }
        return decimal;
    }

    @Override
    public BigInteger getTotalSupply(String contractAddress) {
        this.init(contractAddress);
        BigInteger totalSupply = null;
        try {
            totalSupply = this.erc20Client.totalSupply().send();
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
        this.init(transactionReceipt.getContractAddress());
        List<TransferEvent> transferEvents = new ArrayList<>();
        try {
            List<ERC20Client.TransferEventResponse> transferEventResponses =
                this.erc20Client.getTransferEvents(transactionReceipt);
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
        List<TransferEvent> transferEvents = null;
        try {
            List<ERC20Client.ApprovalEventResponse> approvalEventResponses =
                this.erc20Client.getApprovalEvents(transactionReceipt);
        } catch (Exception e) {
            log.error(" erc get transferEvents error", e);
        }
        return transferEvents;
    }

    @Override
    public void initContractData(List<Transaction> transactions, AddressCache addressCache) {
        Map<String, Erc20Token> erc20Tokens = new ConcurrentHashMap<>();
        transactions.stream().filter(transaction -> transaction.getEsTokenTransferRecords().size() > 0)
            .forEach(transaction -> {
                List<Erc20Param> erc20Params = new ArrayList<>();
                transaction.getEsTokenTransferRecords().stream().forEach(esTokenTransferRecord -> {
                    // 存量的erc20参数，提高访问速度
                    Erc20Token erc20Token = erc20Tokens.get(esTokenTransferRecord.getContract());
                    // 先缓存中获取
                    if (erc20Token == null) {
                        erc20Token = addressCache.getAllErc20TokenMap().get(esTokenTransferRecord.getContract());
                        // 防止参数erc数据为null
                        if (StringUtils.isBlank(erc20Token.getName())) {
                            erc20Token = null;
                        } else {
                            erc20Tokens.put(esTokenTransferRecord.getContract(), erc20Token);
                        }
                    }
                    // 如果数据不存在再从从数据库中获取数据
                    if (erc20Token == null) {
                        erc20Token = this.erc20TokenMapper.selectByAddress(esTokenTransferRecord.getContract());
                        if (null != erc20Token) {
                            erc20Tokens.put(esTokenTransferRecord.getContract(), erc20Token);
                        }
                    }
                    if (erc20Token != null) {
                        esTokenTransferRecord.setDecimal(erc20Token.getDecimal());
                        esTokenTransferRecord.setName(erc20Token.getName());
                        esTokenTransferRecord.setSymbol(erc20Token.getSymbol());
                    }
                    // 更新erc交易数
                    addressCache.updateErcTx(esTokenTransferRecord.getContract());
                    // 存储erc20参数到交易的info中
                    Erc20Param erc20Param = Erc20Param.builder().innerContractAddr(esTokenTransferRecord.getContract())
                        .innerContractName(esTokenTransferRecord.getName()).innerFrom(esTokenTransferRecord.getFrom())
                        .innerSymbol(esTokenTransferRecord.getSymbol()).innerTo(esTokenTransferRecord.getTto())
                        .innerDecimal(String.valueOf(esTokenTransferRecord.getDecimal())).innerValue(esTokenTransferRecord.getTValue()).build();
                    erc20Params.add(erc20Param);
                });
                transaction.setInfo(JSONObject.toJSONString(erc20Params));
            });
    }

}
