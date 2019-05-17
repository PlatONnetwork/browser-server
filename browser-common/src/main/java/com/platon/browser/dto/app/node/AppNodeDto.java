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
    private int ranking;
    private String name;
    private String countryCode;
    private String deposit;
    private double rewardRatio;
}
