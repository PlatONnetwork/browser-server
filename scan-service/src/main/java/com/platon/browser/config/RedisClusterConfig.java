package com.platon.browser.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Data
@Configuration
public class RedisClusterConfig {
//	private String configKey="DEFAULT";
//	private int connectionTimeout;
//	private int soTimeout;
//	private int maxRedirections;
//	private int maxTotal;
//	private int maxIdle;
//	private int minIdle;
//	private int maxWaitMillis;
//	private boolean testOnBorrow;
//	private boolean testOnReturn;
//	private boolean testWhileIdle;
//	private String masterIPs="";
//	private String password="";

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(stringRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}
}
