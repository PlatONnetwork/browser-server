package com.platon.browser.dto.node;

import lombok.Data;

@Data
public class NodeDetail {
    private String id;
    private String address;
    private String name;
    private String logo;
    private Integer electionStatus;
    private String location;
    private long joinTime;
    private String deposit;
    private double rewardRatio;
    private int ranking;
    private String profitAmount;
    private int verifyCount;
    private long blockCount;
    private double avgBlockTime;
    private String rewardAmount;
    private String nodeUrl;
    private String publicKey;
    private String wallet;
    private String intro;
    private String orgName;
    private String orgWebsite;
}