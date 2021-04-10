package com.platon.browser.service.receipt;

import com.platon.browser.bean.ReceiptResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 11:14
 * @Description: 交易服务
 */
@Slf4j
@Service
public class ReceiptService {
    @Resource
    private ReceiptRetryService retryService;

    /**
     * 异步获取区块
     */
    public CompletableFuture<ReceiptResult> getReceiptAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(()->{
            try {
                return retryService.getReceipt(blockNumber);
            } catch (Exception  e) {
                log.error("采集区块({})异常!",blockNumber,e);
            }
            return null;
        });
    }
}
