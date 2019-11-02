package com.platon.browser.persistence.handler;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.persistence.service.DbService;
import com.platon.browser.persistence.service.EsService;
import com.platon.browser.queue.complement.event.ComplementEvent;
import com.platon.browser.queue.complement.handler.IComplementEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class ComplementEventHandler implements IComplementEventHandler {

    @Autowired
    private EsService esService;
    @Autowired
    private DbService dbService;


    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        // 此处调用 persistence模块功能

        List<Block> blocks = new ArrayList<>();
        event.getBlock().setTransactions(null);
        blocks.add(event.getBlock());
        List<Transaction> transactions = new ArrayList<>(event.getTransactions());
        List<BusinessParam> businessParams = event.getBusinessParams();
        try {
            // 入库MYSQL
            dbService.insert(businessParams);
            // 入库ES
            esService.batchInsertOrUpdate(blocks,transactions, Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}