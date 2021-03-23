package com.platon.browser.queue.handler;

import cn.hutool.core.collection.ListUtil;
import com.platon.browser.dao.entity.Token;
import com.platon.browser.dao.mapper.TokenMapper;
import com.platon.browser.queue.event.TokenEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * token事件处理器
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/3/22
 */
@Slf4j
@Component
public class TokenHandler extends AbstractHandler<TokenEvent> {

    @Resource
    private TokenMapper tokenMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.token.batch-size}")
    private volatile int batchSize;

    private Set<Token> stage = new HashSet<>();

    @PostConstruct
    private void init() {
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    @Override
    public void onEvent(TokenEvent event, long sequence, boolean endOfBatch) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getTokenList());
            if (stage.size() < batchSize)
                return;
            tokenMapper.batchInsert(ListUtil.list(true, stage));
            long endTime = System.currentTimeMillis();
            printTps("token", stage.size(), startTime, endTime);
            stage.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

}
