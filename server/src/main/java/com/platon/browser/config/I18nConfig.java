package com.platon.browser.config;

import com.platon.browser.util.I18nUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class I18nConfig {
    @Bean
    public I18nUtil i18n(MessageSource messageSource) {
        return new I18nUtil(messageSource);
    }
}