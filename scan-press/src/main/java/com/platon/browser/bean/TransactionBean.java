package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionBean {

    private BigInteger blockNum = new BigInteger("0");

    private List<Transaction> transactionList = new ArrayList<>();

}
