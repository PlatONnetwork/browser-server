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
@ConfigurationProperties(prefix="spring.elasticsearch.index")
public class EsIndexConfig {
    private String blockIndexName; // 区块索引名称
    private String transactionIndexName; // 交易索引名称
    private String delegationIndexName; // 委托索引名称
    private String nodeOptIndexName; // 节点操作日志索引名称
    private String delegationRewardIndexName; // 委托奖励索引名称
    private String transferTxIndexName; // 主交易内部转账交易索引名称
    private String erc20TxIndexName; // 主交易内部erc20交易索引名称(new)
    private String erc721TxIndexName; // 主交易内部erc721交易索引名称
    private String erc1155TxIndexName; // 主交易内部erc1155交易索引名称
}