package com.platon.browser.service.receipt;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.bean.RpcParam;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 11:14
 * @Description: 带重试功能的交易服务
 */
@Slf4j
@Service
public class ReceiptRetryService {

    private static final String RECEIPT_RPC_INTERFACE = "platon_getTransactionByBlock";

    @Resource
    private PlatOnClient platOnClient;

    /**
     * 带有重试功能的根据区块号获取区块内所有交易的回执信息
     *
     * @param blockNumber
     * @return 交易回扏信息
     * @throws
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public ReceiptResult getReceipt(Long blockNumber) {
        long startTime = System.currentTimeMillis();

        try {
            log.debug("获取回执:{}({})", Thread.currentThread().getStackTrace()[1].getMethodName(), blockNumber);
            RpcParam param = new RpcParam();
            param.setMethod(RECEIPT_RPC_INTERFACE);
            param.getParams().add(blockNumber);
            ReceiptResult result = platOnClient.getReceiptResult(blockNumber);
            log.debug("回执结果数:{}", result.getResult().size());
            log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
            return result;
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            log.error(StrUtil.format("区块[{}]获取回执异常", blockNumber), e);
            throw new BusinessException(e.getMessage());
        }
    }

}
