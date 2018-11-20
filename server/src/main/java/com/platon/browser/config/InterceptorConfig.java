//package com.platon.browser.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurerAdapter {
//    @Bean
//    public PlatonInterceptor platonInterceptor() {
//        return new PlatonInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(platonInterceptor()).addPathPatterns("/browser-api/**");
//        super.addInterceptors(registry);
//    }
//}
