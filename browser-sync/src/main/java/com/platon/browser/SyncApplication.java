package com.platon.browser;

import com.platon.browser.service.BlockSyncService;
import com.platon.browser.service.TransactionSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@EnableRetry
@SpringBootApplication
public class SyncApplication implements ApplicationRunner {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
    @Autowired
    private BlockSyncService blockSyncService;
    @Autowired
    private TransactionSyncService transactionSyncService;

    public static void main ( String[] args ) {
        SpringApplication.run(SyncApplication.class, args);
    }
    @Override
    public void run ( ApplicationArguments args ) {
//        EXECUTOR_SERVICE.submit(()->blockSyncService.sync());
//        EXECUTOR_SERVICE.submit(()->transactionSyncService.sync());
//
//        while (!Erc20TransactionSyncService.isDone()){
//            SleepUtil.sleep(1L);
//        }
//        log.info("数据同步完成!");
//        System.exit(0);
    }
}
