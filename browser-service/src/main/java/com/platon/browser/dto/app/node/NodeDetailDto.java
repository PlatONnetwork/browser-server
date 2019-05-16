package com.platon.browser.dto.app.node;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class NodeDetailDto {
    private String nodeId;
    private int ranking;
    private String name;
    private String deposit;
    private double rewardRatio;
    private String orgName;
    private String orgWebsite;
    private String intro;
    private String nodeUrl;
    private int ticketCount;
    private long joinTime;
    private String nodeType;

}
