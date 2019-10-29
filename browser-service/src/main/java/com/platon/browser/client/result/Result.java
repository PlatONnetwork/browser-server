package com.platon.browser.client.result;

import lombok.Data;

import java.util.List;

@Data
public class Result {
    private String gasUsed;
    private List<Logs> logs;
    private String transactionHash;
    private String transactionIndex;
}