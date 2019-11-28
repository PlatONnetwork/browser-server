package com.platon.browser;

import com.platon.browser.service.SyncService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@EnableRetry
@Configuration
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {"com.platon.browser.dao.mapper", "com.platon.browser.complement.dao.mapper"})
public class SyncApplication implements ApplicationRunner {

    @Autowired
    private SyncService syncService;

    public static void main ( String[] args ) {
        SpringApplication.run(SyncApplication.class, args);
    }

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    @Override
    public void run ( ApplicationArguments args ) throws Exception {
        EXECUTOR_SERVICE.submit(()->syncService.syncBlock());
        EXECUTOR_SERVICE.submit(()->syncService.syncTransaction());
    }
}
