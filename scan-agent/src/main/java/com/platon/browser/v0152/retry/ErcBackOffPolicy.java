package com.platon.browser.v0152.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

/**
 * Erc退避策略
 *
 * @date 2021/5/11
 */
@Slf4j
public class ErcBackOffPolicy {

    /**
     * 退避策略
     *
     * @param
     * @return org.springframework.retry.backoff.ExponentialBackOffPolicy
     * @date 2021/5/11
     */
    public static ExponentialBackOffPolicy factory() {
        // 指数退避策略 等待时间= 等待时间*倍数 ，即每一次的等待时间是上一次等待时间的n倍，
        // 到达最大的等待时间之后就不在增加了，一直都是以最大的等待时间在等待。默认执行3次
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        // 初始等待时间
        exponentialBackOffPolicy.setInitialInterval(1000);
        // 时间等待倍数
        exponentialBackOffPolicy.setMultiplier(2);
        // 最大等待时间
        exponentialBackOffPolicy.setMaxInterval(6000);
        return exponentialBackOffPolicy;
    }

}
