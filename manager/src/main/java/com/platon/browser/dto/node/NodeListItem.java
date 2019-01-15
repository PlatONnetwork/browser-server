package com.platon.browser.dto.node;

import com.platon.browser.dao.entity.NodeRanking;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class NodeListItem {
    private Long id;
    private String nodeId;
    private Integer ranking;
    private String logo;
    private String name;
    private Integer electionStatus;
    private String countryCode;
    private String location;
    private String deposit;
    private Integer blockCount;
    private Double rewardRatio;
    private String address;
    private Integer isValid;
    public void init(NodeRanking initData) {
        BeanUtils.copyProperties(initData,this);
        this.setBlockCount(initData.getBlockCount().intValue());
        this.setLogo(initData.getUrl());
    }
}
