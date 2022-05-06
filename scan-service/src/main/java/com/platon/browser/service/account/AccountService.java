package com.platon.browser.service.account;

import com.platon.browser.bean.CommonConstant;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.protocol.core.DefaultBlockParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    private static final String REWARD_ADDR = InnerContractAddrEnum.REWARD_CONTRACT.getAddress();

    private static final String BLOCK_TIP = "]在区块号[";

    private static final String BALANCE_TIP = "]的余额失败:";

    @Resource
    private PlatOnClient platOnClient;

    /**
     * 带有重试功能的根据区块号获取激励池余额
     *
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public BigDecimal getInciteBalance(BigInteger blockNumber) {
        if (blockNumber.compareTo(BigInteger.ZERO) < 0) {
            blockNumber = BigInteger.ZERO;
        }
        try {
            BigInteger balance = platOnClient.getWeb3jWrapper().getWeb3j().platonGetBalance(INCITE_ACCOUNT_ADDR, DefaultBlockParameter.valueOf(blockNumber)).send().getBalance();
            return new BigDecimal(balance);
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            String error = "获取激励池[" + INCITE_ACCOUNT_ADDR + BLOCK_TIP + blockNumber + BALANCE_TIP + e.getMessage();
            log.error("{}", error);
            throw new BusinessException(error);
        }
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public BigDecimal recover(Exception e) {
        log.error("重试完成还是业务失败，请联系管理员处理");
        return BigDecimal.ZERO;
    }

    /**
     * 带有重试功能的根据区块号获取锁仓池余额
     *
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public BigDecimal getLockCabinBalance(BigInteger blockNumber) {
        if (blockNumber.compareTo(BigInteger.ZERO) < 0) {
            blockNumber = BigInteger.ZERO;
        }
        try {
            BigInteger balance = platOnClient.getWeb3jWrapper().getWeb3j().platonGetBalance(RESTRICTING_ADDR, DefaultBlockParameter.valueOf(blockNumber)).send().getBalance();
            return new BigDecimal(balance);
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            String error = "获取锁仓合约[" + RESTRICTING_ADDR + BLOCK_TIP + blockNumber + BALANCE_TIP + e.getMessage();
            log.error("{}", error);
            throw new BusinessException(error);
        }
    }

    /**
     * 带有重试功能的根据区块号获取质押池余额
     *
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BigDecimal getStakingBalance(BigInteger blockNumber) {
        if (blockNumber.compareTo(BigInteger.ZERO) < 0) blockNumber = BigInteger.ZERO;
        try {
            BigInteger balance = platOnClient.getWeb3jWrapper().getWeb3j().platonGetBalance(STAKING_ADDR, DefaultBlockParameter.valueOf(blockNumber)).send().getBalance();
            return new BigDecimal(balance);
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            String error = "获取质押合约[" + STAKING_ADDR + BLOCK_TIP + blockNumber + BALANCE_TIP + e.getMessage();
            log.error("{}", error);
            throw new BusinessException(error);
        }
    }

    /**
     * 带有重试功能的根据区块号获取收益余额
     *
     * @param blockNumber
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BigDecimal getRewardBalance(BigInteger blockNumber) {
        if (blockNumber.compareTo(BigInteger.ZERO) < 0) blockNumber = BigInteger.ZERO;
        try {
            BigInteger balance = platOnClient.getWeb3jWrapper().getWeb3j().platonGetBalance(REWARD_ADDR, DefaultBlockParameter.valueOf(blockNumber)).send().getBalance();
            return new BigDecimal(balance);
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            String error = "获取质押合约[" + REWARD_ADDR + BLOCK_TIP + blockNumber + BALANCE_TIP + e.getMessage();
            log.error("{}", error);
            throw new BusinessException(error);
        }
    }

}
