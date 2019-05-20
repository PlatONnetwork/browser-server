package com.platon.browser.dto.app.node;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class AppUserNodeDto {
    private String nodeId;
    private String name;
    private String countryCode;
    private String validNum;
    private String totalTicketNum;
    private String locked;
    private String earnings;
    private String transactionTime;
}
