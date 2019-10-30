package com.platon.browser.collection.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.old.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 10:14
 * @Description: 区块服务
 */
@Slf4j
@Service
public class BlockRetryService {

    @Autowired
    private PlatOnClient client;

    /**
     * 根据区块号获取区块信息
     * @param blockNumber
     * @return 带有交易信息的区块信息
     * @throws
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public PlatonBlock getBlock(Long blockNumber) throws IOException, BusinessException {
        log.debug("{}({})",Thread.currentThread().getStackTrace()[1].getMethodName(),blockNumber);
        DefaultBlockParameter dp = DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber));
        return client.getWeb3j().platonGetBlockByNumber(dp,true).send();
    }
}
