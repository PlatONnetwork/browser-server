package com.platon.browser.persistence.service;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Component
public class RedisService {
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void batchInsertOrUpdate (Set<Block> blocks, Set<Transaction> transactions) throws IOException {

    }
}
