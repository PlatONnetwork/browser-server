package com.platon.browser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="spring.redis.key")
public class RedisKeyConfig {
	private Long maxItem; // 最大缓存记录数
	private String blocks; // 区块
	private String transactions; // 交易
	private String networkStat; // 网络统计
	private String transferTx; // 合约调用内部的转账交易
	private String erc20Tx; // erc20交易
	private String erc721Tx; // erc721交易
	private String erc1155Tx; // erc1155交易
}
