package com.platon.browser.common.service.account;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 账户服务:
 * 1、查询激励池余额
 */
@Slf4j
@Service
public class AccountService {
    private static final String INCITE_ACCOUNT_ADDR = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress();
    private static final String RESTRICTING_ADDR = InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress();
    private static final String STAKING_ADDR = InnerContractAddrEnum.STAKING_CONTRACT.getAddress();

    @Autowired
    private PlatOnClient platOnClient;

    /**
     * 带有重试功能的根据区块号获取激励池余额
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BigDecimal getInciteBalance(BigInteger blockNumber) {
        try {
            BigInteger balance = platOnClient.getWeb3j()
                    .platonGetBalance(INCITE_ACCOUNT_ADDR, DefaultBlockParameter.valueOf(blockNumber))
                    .send().getBalance();
            return new BigDecimal(balance);
        }catch (Exception e){
            String error = "获取激励池["+INCITE_ACCOUNT_ADDR+"]在区块号["+blockNumber+"]的余额失败:"+e.getMessage();
            log.error("{}",error);
            throw new BusinessException(error);
        }
    }

    /**
     * 带有重试功能的根据区块号获取锁仓池余额
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BigDecimal getLockCabinBalance(BigInteger blockNumber){
        try {
            BigInteger balance = platOnClient.getWeb3j().platonGetBalance(RESTRICTING_ADDR,DefaultBlockParameter.valueOf(blockNumber))
                   .send().getBalance();
            return new BigDecimal(balance);
        }catch (Exception e){
            String error = "获取锁仓合约["+RESTRICTING_ADDR+"]在区块号["+blockNumber+"]的余额失败:"+e.getMessage();
            log.error("{}",error);
            throw new BusinessException(error);
        }
    }

    /**
     * 带有重试功能的根据区块号获取质押池余额
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BigDecimal getStakingBalance(BigInteger blockNumber){
        try {
            BigInteger balance = platOnClient.getWeb3j().platonGetBalance(STAKING_ADDR,DefaultBlockParameter.valueOf(blockNumber))
                    .send().getBalance();
            return new BigDecimal(balance);
        }catch (Exception e){
            String error = "获取质押合约["+STAKING_ADDR+"]在区块号["+blockNumber+"]的余额失败:"+e.getMessage();
            log.error("{}",error);
            throw new BusinessException(error);
        }
    }
}
