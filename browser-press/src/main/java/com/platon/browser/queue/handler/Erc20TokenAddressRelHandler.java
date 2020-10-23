package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.dao.mapper.Erc20TokenAddressRelMapper;
import com.platon.browser.queue.event.Erc20TokenAddressRelEvent;
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
public class Erc20TokenAddressRelHandler extends AbstractHandler<Erc20TokenAddressRelEvent> {

    @Autowired
    private Erc20TokenAddressRelMapper erc20TokenAddressRelMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.token-address.batch-size}")
    private volatile int batchSize;

    private List<Erc20TokenAddressRel> stage = new ArrayList<>();

    @PostConstruct
    private void init() {
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(Erc20TokenAddressRelEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getErc20TokenAddressRelList());
            if (stage.size() < batchSize) return;
            int round = stage.size() / batchSize;
            for (int i = 0; i < round; i++) {
                List<Erc20TokenAddressRel> saveEle = stage.subList(i * batchSize, i * batchSize + batchSize);
                erc20TokenAddressRelMapper.batchInsert(saveEle);
            }
            if (stage.size() > round * batchSize) {
                erc20TokenAddressRelMapper.batchInsert(stage.subList(round * batchSize, stage.size()));
            }
            long endTime = System.currentTimeMillis();
            printTps("Erc20代币与地址关系", stage.size(), startTime, endTime);
            stage.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}