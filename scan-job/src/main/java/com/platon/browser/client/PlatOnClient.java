package com.platon.browser.client;

import com.platon.browser.exception.ConfigLoadingException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Component
public class PlatOnClient {
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

    public void updateCurrentWeb3jWrapper(){
        retryableClient.updateCurrentWeb3jWrapper();
    }

    public Web3jWrapper getWeb3jWrapper(){
        return retryableClient.getWeb3jWrapper();
    }
}
