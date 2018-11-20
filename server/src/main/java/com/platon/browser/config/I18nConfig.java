package com.platon.browser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class I18NConfig extends WebMvcConfigurerAdapter {

    /**
     * 配置自己的国际化语言解析器
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new PlatonLocaleResolver();
    }

    /**
     * 配置自己的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //super.addInterceptors(registry);
    }


}
