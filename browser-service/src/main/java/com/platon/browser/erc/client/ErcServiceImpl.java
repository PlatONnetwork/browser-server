package com.platon.browser.erc.client;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.tx.exceptions.ContractCallException;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.erc.ErcService;
import com.platon.browser.utils.NetworkParms;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: browser-server
 * @description: erc查询余额接口
 * @author: Rongjin Zhang
 * @create: 2020-10-12 11:48
 */
@Slf4j
@Component
public class ErcServiceImpl implements ErcService {

    @Autowired
    private PlatOnClient platOnClient;

    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    private ERC20Client init(String contractAddress) {
        if (StringUtils.isBlank(contractAddress)) {
            throw new RuntimeException("contractAddress is not null");
        }
        ERC20Client erc20Client = new ERC20Client(contractAddress, this.platOnClient.getWeb3jWrapper().getWeb3j(),
            Credentials.create("4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7"),
            NetworkParms.getChainId());
        return erc20Client;
    }

    @Override
    public BigInteger getBalance(String contractAddress, String account) {
        ERC20Client erc20Client = this.init(contractAddress);
        BigInteger balance = BigInteger.ZERO;
        try {
            balance = erc20Client.balanceOf(account).send();
        } catch (ContractCallException e) {
            log.debug(" not erc contract,{}", contractAddress);
        } catch (Exception e) {
            log.error(" erc get name error", e);
        }
        return balance;
    }
}
