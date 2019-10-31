package com.platon.browser.common.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 账户服务:
 * 1、查询激励池余额
 */
@Slf4j
@Service
public class AccountService {
    private static final String INCITE_ACCOUNT_ADDR = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress();

    @Autowired
    private PlatOnClient platOnClient;

    /**
     * 带有重试功能的根据区块号获取激励池余额
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BigInteger getInciteBalance(BigInteger blockNumber) {
        BigInteger inciteBalance = null;
        try {
            inciteBalance = platOnClient.getWeb3j()
                            .platonGetBalance(INCITE_ACCOUNT_ADDR, DefaultBlockParameter.valueOf(blockNumber))
                            .send().getBalance();
        } catch (IOException e) {
            throw new BusinessException("获取激励池["+INCITE_ACCOUNT_ADDR+"]在区块号["+blockNumber+"]的余额失败:"+e.getMessage());
        }
        return inciteBalance;
    }

}
