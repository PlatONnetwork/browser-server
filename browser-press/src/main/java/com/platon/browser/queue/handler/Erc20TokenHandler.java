package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Erc20Token;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import com.platon.browser.queue.event.Erc20TokenEvent;
import com.platon.browser.service.redis.RedisErc20TokenService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Erc20TokenHandler extends AbstractHandler<Erc20TokenEvent> {

    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    @Autowired
    private RedisErc20TokenService dbHelperCache;

    @Setter
    @Getter
    @Value("${disruptor.queue.erc20-token.batch-size}")
    private volatile int batchSize;

    private List<Erc20Token> stage = new ArrayList<>();

    @PostConstruct
    private void init() {
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(Erc20TokenEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getErc20TokenList());
            if (stage.size() < batchSize) return;

            erc20TokenMapper.batchInsert(stage);
            dbHelperCache.addTokenCount(stage.size());
            long endTime = System.currentTimeMillis();
            printTps("Erc20代币", stage.size(), startTime, endTime);
            stage.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}