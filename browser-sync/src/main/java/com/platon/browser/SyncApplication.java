package com.platon.browser;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NetworkStatExample;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.queue.publisher.BlockEventPublisher;
import com.platon.browser.queue.publisher.TransactionEventPublisher;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Slf4j
@EnableRetry
@Configuration
@EnableScheduling
@SpringBootApplication
@EnableEncryptableProperties
@MapperScan(basePackages = {"com.platon.browser.dao.mapper", "com.platon.browser.complement.dao.mapper"})
public class SyncApplication implements ApplicationRunner {

    @Autowired
    private BlockEventPublisher blockEventPublisher;

    @Autowired
    private TransactionEventPublisher transactionEventPublisher;

    @Autowired
    private BlockESRepository blockESRepository;

    @Autowired
    private TransactionESRepository transactionESRepository;

    @Autowired
    private NetworkStatMapper networkStatMapper;

    @Value("${esyncnfo.searchBlockPageSize}")
    private int blockPageSize;

    @Value("${esyncnfo.searchTxPageSize}")
    private int txPageSize;

    public static void main ( String[] args ) {
        SpringApplication.run(SyncApplication.class, args);
    }

    @Override
    public void run ( ApplicationArguments args ) throws Exception {
        ESResult <Block> blocks = new ESResult <>();
        List <NetworkStat> list = networkStatMapper.selectByExample(new NetworkStatExample());
        ESQueryBuilderConstructor blockConstructor = new ESQueryBuilderConstructor();
        blockConstructor.setDesc("num");
        for (int pageNo = 0; pageNo <= 1000; pageNo++) {
            blocks = blockESRepository.search(blockConstructor, Block.class, pageNo, blockPageSize);
            blockEventPublisher.publish(blocks.getRsData());
        }

        ESResult<Transaction> transactions = new ESResult <>();
        ESQueryBuilderConstructor transactionConstructor = new ESQueryBuilderConstructor();
        transactionConstructor.setDesc("seq");
        for (int pageNo = 0; pageNo <= 100; pageNo++) {
            transactions = transactionESRepository.search(transactionConstructor,Transaction.class,pageNo,txPageSize);
            transactionEventPublisher.publish(transactions.getRsData());
        }
    }



}
