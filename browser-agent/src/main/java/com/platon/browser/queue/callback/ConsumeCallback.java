package com.platon.browser.queue.callback;

import java.util.concurrent.ExecutionException;

/**
 * 消息消费回调接口
 * @param <T>
 */
public interface ConsumeCallback<T> {
    void call(T param) throws ExecutionException, InterruptedException;
}
