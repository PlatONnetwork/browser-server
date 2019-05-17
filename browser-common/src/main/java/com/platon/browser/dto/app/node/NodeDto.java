package com.platon.browser.dto.app.node;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class NodeDto {
    private String nodeId;
    private int ranking;
    private String name;
    private String countryCode;
    private String countryEnName;
    private String countryCnName;
    private String countrySpellName;
    private String deposit;
    private double rewardRatio;
}
