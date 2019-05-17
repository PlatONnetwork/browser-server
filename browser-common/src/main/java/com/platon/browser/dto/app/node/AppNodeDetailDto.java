package com.platon.browser.dto.app.node;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class AppNodeDetailDto {
    private String nodeId;
    private String ranking;
    private String name;
    private String deposit;
    private String rewardRatio;
    private String orgName;
    private String orgWebsite;
    private String intro;
    private String nodeUrl;
    private String ticketCount;
    private String joinTime;
    private String nodeType;

}
