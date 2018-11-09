package com.platon.browser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Value("${platon.chains}")
    private String configStr;

    @Bean
    public ChainsConfig chainsConfig(){
        return new ChainsConfig(configStr);
    }
}
