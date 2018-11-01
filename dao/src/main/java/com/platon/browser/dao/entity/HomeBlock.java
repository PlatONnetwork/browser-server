package com.platon.browser.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class HomeBlock {
    private Long number;
    private Date timestamp;
    private String miner;
    private String blockReward;
    private Long transaction;
}