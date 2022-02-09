package com.platon.browser.queue.handler;

import cn.hutool.core.collection.ListUtil;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.dao.mapper.TokenInventoryMapper;
import com.platon.browser.queue.event.TokenInventoryEvent;
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

@Slf4j
@Component
public class TokenInventoryHandler extends AbstractHandler<TokenInventoryEvent> {

    @Resource
    private TokenInventoryMapper tokenInventoryMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.token-inventory.batch-size}")
    private volatile int batchSize;

    private Set<TokenInventoryWithBLOBs> stage = new HashSet<>();

    @PostConstruct
    private void init() {
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    @Override
    public void onEvent(TokenInventoryEvent event, long sequence, boolean endOfBatch) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getTokenList());
            if (stage.size() < batchSize)
                return;
            tokenInventoryMapper.batchInsert(ListUtil.list(true, stage));
            long endTime = System.currentTimeMillis();
            printTps("token", stage.size(), startTime, endTime);
            stage.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

}
