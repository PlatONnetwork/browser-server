package com.platon.browser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix="disruptor.queue")
public class DisruptorConfig {
    private int blockBufferSize; // 区块事件环形缓冲区大小
    private int collectionBufferSize; // 采集事件环形缓冲区大小
    private int complementBufferSize; // 数据补充环形缓冲区大小
    private int gasEstimateBufferSize; // gas price估算消息环形缓冲区大小
    private int persistenceBufferSize; // 数据持久化环形缓冲区大小
    private int persistenceBatchSize; // 数据持久化批处理大小
}