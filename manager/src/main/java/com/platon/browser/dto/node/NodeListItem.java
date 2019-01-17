package com.platon.browser.dto.node;

import com.platon.browser.dao.entity.NodeRanking;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        BigDecimal ratio = BigDecimal.ONE.subtract(BigDecimal.valueOf(initData.getRewardRatio())).setScale(2,RoundingMode.DOWN);
        this.setRewardRatio(ratio.doubleValue());
        BigDecimal deposit = Convert.fromWei(initData.getDeposit(), Convert.Unit.ETHER);
        this.setDeposit(deposit.toString());
    }
}
