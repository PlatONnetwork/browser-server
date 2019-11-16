package com.platon.browser.collection.service.receipt;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.RpcParam;
import com.platon.browser.client.ReceiptResult;
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
public class ReceiptRetryService {
    private static final String RECEIPT_RPC_INTERFACE = "platon_getTransactionByBlock";
    @Autowired
    private PlatOnClient platOnClient;

    /**
     * 带有重试功能的根据区块号获取区块内所有交易的回执信息
     * @param blockNumber
     * @return 交易回扏信息
     * @throws
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public ReceiptResult getReceipt(Long blockNumber) throws Exception {
        long startTime = System.currentTimeMillis();

        try {
            log.debug("获取回执:{}({})",Thread.currentThread().getStackTrace()[1].getMethodName(),blockNumber);
            RpcParam param = new RpcParam();
            param.setMethod(RECEIPT_RPC_INTERFACE);
            param.getParams().add(blockNumber);
            ReceiptResult result = platOnClient.getReceiptResult(blockNumber);
            log.debug("回执结果数:{}", result.getResult().size());
            log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
            return result;
        }catch (Exception e){
            platOnClient.updateCurrentWeb3jWrapper();
            log.error("",e);
            throw e;
        }
    }

}
