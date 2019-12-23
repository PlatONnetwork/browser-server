package com.platon.browser;

import com.platon.browser.queue.publisher.BlockPublisher;
import com.platon.browser.queue.publisher.NodeOptPublisher;
import com.platon.browser.queue.publisher.TransactionPublisher;
import com.platon.browser.service.BlockResult;
import com.platon.browser.service.DataGenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@EnableRetry
@SpringBootApplication
public class PressApplication implements ApplicationRunner {

    @Autowired
    private BlockPublisher blockPublisher;
    @Autowired
    private TransactionPublisher transactionPublisher;
    @Autowired
    private NodeOptPublisher nodeOptPublisher;

    @Autowired
    private DataGenService dataGenService;
    public static void main ( String[] args ) {
        SpringApplication.run(PressApplication.class, args);
    }
    @Override
    public void run ( ApplicationArguments args ) {
        BigInteger blockNumber = BigInteger.ZERO;
        while (true){
            BlockResult blockResult = dataGenService.get(blockNumber);

            blockPublisher.publish(Arrays.asList(blockResult.getBlock()));
            transactionPublisher.publish(blockResult.getTransactionList());
            nodeOptPublisher.publish(blockResult.getNodeOptList());

            blockNumber=blockNumber.add(BigInteger.ONE);
        }
    }
}
