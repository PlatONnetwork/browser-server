package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.browser.exception.ConfigLoadingException;
import com.platon.protocol.core.methods.response.bean.EconomicConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Slf4j
@Component("jobPlatOnClient")
public class JobPlatOnClient {

    // 交易输入参数并行解码线程池
    @Getter
    @Setter
    private ExecutorService logDecodeExecutor;

    @Resource
    private RetryableClient retryableClient;

    @PostConstruct
    private void init() throws ConfigLoadingException {
        retryableClient.init();
    }

    public void updateCurrentWeb3jWrapper() {
        retryableClient.updateCurrentWeb3jWrapper();
    }

    public Web3jWrapper getWeb3jWrapper() {
        return retryableClient.getWeb3jWrapper();
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(value = 3000L))
    public EconomicConfig getEconomicConfig() throws ConfigLoadingException {
        try {
            EconomicConfig ec = retryableClient.getWeb3jWrapper().getWeb3j().getEconomicConfig().send().getEconomicConfig();
            String msg = JSON.toJSONString(ec, true);
            log.info("链上配置:{}", msg);
            return ec;
        } catch (Exception e) {
            retryableClient.updateCurrentWeb3jWrapper();
            log.error("获取链上配置出错({}),将重试!", e.getMessage());
            throw new ConfigLoadingException(e.getMessage());
        }
    }

}
