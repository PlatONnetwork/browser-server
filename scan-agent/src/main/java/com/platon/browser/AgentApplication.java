package com.platon.browser;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableRetry
@Configuration
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {"com.platon.browser", "com.platon.browser.dao.mapper", "com.platon.browser.dao.custommapper", "com.platon.browser.v0150.dao", "com.platon.browser.v0151.dao"})
public class AgentApplication implements ApplicationRunner {


    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

}
