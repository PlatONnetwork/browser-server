package com.platon.browser.erc.client;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dto.ERCData;
import com.platon.browser.erc.ERCInterface;
import com.platon.browser.utils.NetworkParms;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        if (this.erc20Client == null) {
            try {
                this.lock.lock();
                this.erc20Client = new ERC20Client(this.contractAddress, this.platOnClient.getWeb3jWrapper().getWeb3j(), null, NetworkParms.getChainId());
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
        } catch (final Exception e) {
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
        } catch (final Exception e) {
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
        } catch (final Exception e) {
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
        } catch (final Exception e) {
            log.error(" erc get totalSupply error", e);
        }
        return totalSupply;
    }

    @Override
    public ERCData getErcData() {
        final String name = this.getName();
        final String symbol = this.getSymbol();
        final BigInteger decimals = this.getDecimals();
        final BigInteger totalSupply = this.getTotalSupply();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(symbol)
                || decimals == null || totalSupply == null) {
            return null;
        }
        final ERCData ercData = new ERCData();
        ercData.setName(name);
        ercData.setDecimal(decimals);
        ercData.setSymbol(symbol);
        ercData.setTotalSupply(totalSupply);
        return ercData;
    }

}
