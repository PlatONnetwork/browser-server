package com.platon.browser.collection.service.transaction;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.RpcParam;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 11:14
 * @Description: 带重试功能的交易服务
 */
@Slf4j
@Service
public class TransactionRetryService {
    private static final String RECEIPT_RPC_INTERFACE = "platon_getTransactionByBlock";
    @Autowired
    private PlatOnClient client;

    /**
     * 根据区块号获取区块内所有交易的回执信息
     * @param blockNumber
     * @return 带有交易回扏信息
     * @throws
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    ReceiptResult getReceipt(Long blockNumber) throws HttpRequestException {
        log.debug("获取回执:{}({})",Thread.currentThread().getStackTrace()[1].getMethodName(),blockNumber);
        RpcParam param = new RpcParam();
        param.setMethod(RECEIPT_RPC_INTERFACE);
        param.getParams().add(blockNumber);
        ReceiptResult result = HttpUtil.post(client.getWeb3jAddress(),param.toJsonString(),ReceiptResult.class);
        log.debug("回执结果:{}", result);
        return result;
    }
}
