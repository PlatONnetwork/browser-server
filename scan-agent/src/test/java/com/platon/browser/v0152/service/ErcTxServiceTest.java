package com.platon.browser.v0152.service;

import cn.hutool.core.util.RandomUtil;
import com.platon.browser.v0152.retry.ErcBackOffPolicy;
import com.platon.browser.v0152.retry.ErcRetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class ErcTxServiceTest {

    @Test
    public void retry() throws Throwable {
        AtomicLong contractIdMark = new AtomicLong(0);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(ErcRetryPolicy.factory());
        retryTemplate.setBackOffPolicy(ErcBackOffPolicy.factory());
        String res = retryTemplate.execute((RetryCallback<String, Throwable>) context -> {
                    // 需要重试的逻辑代码
                    String data = "";
                    try {
                        if (contractIdMark.intValue() <= 3) {
                            if (context.getRetryCount() > 0) {
                                log.warn("当前重试次数为{},{}", context.getRetryCount(),contractIdMark.intValue());
                            }
                            data = test();
                        } else {
                            log.warn("重试超过3次，将不再重试");
                        }
                    } catch (SocketTimeoutException e) {
                        throw e;
                    } catch (Exception e) {
                        contractIdMark.incrementAndGet();
                        throw e;
                    }
                    return data;
                }, context -> {
                    // 重试失败后执行的代码
                    log.error("第[{}]次重试失败", context.getRetryCount() + 1);
                    return "";
                }
        );
    }

    AtomicLong aaa = new AtomicLong(1);

    private String test() throws IOException {
        try {
            int j = RandomUtil.randomInt(2000);
            j = 4;
            Thread.sleep(2000);
            if (j % 2 == 0) {
                int i = 1 / 0;
            }
        } catch (Exception e) {
            throw new IOException("我超时了");
        } finally {
            aaa.incrementAndGet();
        }
        return "";
    }

}
