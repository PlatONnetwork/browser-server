package com.platon.browser.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CandidateTxInfo {
    @Data
    public static class Extra{
        String nodeName;
        String nodePortrait;
        String nodeDiscription;
        String nodeDepartment;
        String officialWebsite;
        long time;
    }
    @Data
    public static class Parameter{
        String owner;
        Extra Extra;
        String port;
        BigDecimal fee;
        String host;
        String nodeId;
    }
    String functionName,type;
    Parameter parameters;
}