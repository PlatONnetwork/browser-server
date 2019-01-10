package com.platon.browser.dto.node;

import com.platon.browser.dao.entity.NodeRanking;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class NodeListItem {
    private String id;
    private int ranking;
    private String logo;
    private String name;
    private int electionStatus;
    private String countryCode;
    private String location;
    private String deposit;
    private int blockCount;
    private double rewardRatio;
    private String address;
    public void init(NodeRanking initData){
        BeanUtils.copyProperties(initData,this);
    }
}
