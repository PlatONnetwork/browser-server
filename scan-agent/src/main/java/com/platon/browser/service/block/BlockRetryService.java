package com.platon.browser.service.block;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.exception.CollectionBlockException;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 10:14
 * @Description: 带重试功能的区块服务
 */
@Slf4j
@Service
public class BlockRetryService {

    @Resource
    private PlatOnClient platOnClient;

    private BigInteger latestBlockNumber;

    /**
     * 根据区块号获取区块信息
     *
     * @param blockNumber
     * @return 带有交易信息的区块信息
     * @throws
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    PlatonBlock getBlock(Long blockNumber) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            log.debug("获取区块:{}({})", Thread.currentThread().getStackTrace()[1].getMethodName(), blockNumber);
            DefaultBlockParameter dp = DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber));
            PlatonBlock block = platOnClient.getWeb3jWrapper().getWeb3j().platonGetBlockByNumber(dp, true).send();
            log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
            return block;
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            log.error(StrUtil.format("获取区块[{}]异常", blockNumber), e);
            throw e;
        }
    }

    /**
     * 检查当前区块号是否合法
     *
     * @param currentBlockNumber
     * @throws
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    void checkBlockNumber(Long currentBlockNumber) throws IOException, CollectionBlockException {
        try {
            if (latestBlockNumber == null || currentBlockNumber > latestBlockNumber.longValue()) {
                // 如果记录的链上最新区块号为空,或当前区块号大于记录的链上最新区块号,则更新链上最新区块号
                latestBlockNumber = platOnClient.getWeb3jWrapper().getWeb3j().platonBlockNumber().send().getBlockNumber();
            }
            if (currentBlockNumber > latestBlockNumber.longValue()) {
                log.warn("准备采集区块[{}],链上最高区块[{}],即将等待重试...", currentBlockNumber, latestBlockNumber);
                // 如果当前区块号仍然大于更新后的链上最新区块号
                throw new CollectionBlockException("currentBlockNumber(" + currentBlockNumber + ")>latestBlockNumber(" + latestBlockNumber + "), wait for chain");
            }
        } catch (Exception e) {
            log.warn("检查当前区块号合法异常{}", e.getMessage());
            throw e;
        }
    }

}
