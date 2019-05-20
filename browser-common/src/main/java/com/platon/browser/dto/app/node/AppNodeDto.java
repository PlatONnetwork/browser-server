package com.platon.browser.dto.app.node;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class AppNodeDto {
    private String nodeId;
    private Integer ranking;
    private String name;
    private String countryCode;
    private String deposit;
    private String reward;
    private String ticketCount;
    private String joinTime;
    private String nodeType;
}
