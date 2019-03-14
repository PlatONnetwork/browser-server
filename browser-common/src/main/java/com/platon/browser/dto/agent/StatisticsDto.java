package com.platon.browser.dto.agent;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2018/12/5
 * Time: 20:58
 */
@Data
public class StatisticsDto {
    private String chainId;
    private String ip;
    private Integer port;
    private String address;
    private Double rewardRatio;
    private Integer nodeStatus;
    private String id;
    private String value;
    private String type;
}