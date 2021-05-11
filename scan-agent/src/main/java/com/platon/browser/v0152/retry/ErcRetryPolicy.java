package com.platon.browser.v0152.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.classify.Classifier;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.net.SocketTimeoutException;

/**
 * ERC重试策略
 *
 * @date 2021/5/11
 */
@Slf4j
public class ErcRetryPolicy {

    /**
     * 重试策略工厂：超时异常--无限重试，业务异常--重试3次
     *
     * @param
     * @return org.springframework.retry.policy.ExceptionClassifierRetryPolicy
     * @date 2021/5/11
     */
    public static ExceptionClassifierRetryPolicy factory() {
        // 根据不同的异常选择不同的重试策略
        ExceptionClassifierRetryPolicy policy = new ExceptionClassifierRetryPolicy();
        policy.setExceptionClassifier((Classifier<Throwable, RetryPolicy>) classifiable -> {
            if (classifiable instanceof SocketTimeoutException) {
                // 如果是TimeoutException 异常就是AlwaysRetryPolicy（一直执行直到成功）策略
                log.warn("超时异常，即将无限重试");
                return new AlwaysRetryPolicy();
            } else {
                log.warn("调用异常，即将重试3次");
                return new SimpleRetryPolicy();
            }
        });
        return policy;
    }

}
