package com.platon.browser.dto.node;

import lombok.Data;

@Data
public class NodeItem {
    private String id;
    private int ranking;
    private String name;
    private int electionStatus;
    private String countryCode;
    private String location;
    private String deposit;
    private int blockCount;
    private double rewardRatio;
    private String address;
}
