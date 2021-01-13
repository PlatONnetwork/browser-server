package com.platon.browser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="spring.redis.key")
public class RedisKeyConfig {
	private Long maxItem;
	private String blocks;
	private String transactions;
	private String networkStat;
	private String innerTx;
	private String erc20Tx;
	private String erc721Tx;
}
