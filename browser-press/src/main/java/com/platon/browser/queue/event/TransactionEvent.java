package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;

import java.util.List;
@Data
public class TransactionEvent {
    private List<Transaction> transactionList;
}
