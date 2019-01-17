package com.platon.browser.dto;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.common.dto.agent.CandidateDetailDto;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.dto.agent.TransactionDto;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.util.GeoUtil;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 16:22
 */
public class NodeRankingDto extends NodeRanking {
    public void init ( CandidateDto initData ) {
        CandidateDetailDto candidateDetailDto = null;
        try {
            candidateDetailDto = JSONObject.parseObject(initData.getExtra(), CandidateDetailDto.class);
            this.setName(candidateDetailDto.getNodeName());
            this.setIntro(candidateDetailDto.getNodeDiscription());
            this.setOrgName(candidateDetailDto.getNodeDepartment());
            this.setOrgWebsite(candidateDetailDto.getOfficialWebsite());
            this.setUrl(candidateDetailDto.getNodePortrait() != null ? candidateDetailDto.getNodePortrait() : "test");
        } catch (Exception e) {
            this.setName("");
            this.setIntro("");
            this.setOrgName("");
            this.setOrgWebsite("");
            this.setUrl("");
        }
        this.setIp(initData.getHost());
        this.setNodeId(initData.getCandidateId());
        this.setPort(Integer.valueOf(initData.getPort()));
        this.setAddress(initData.getOwner());
        this.setUpdateTime(new Date());
        this.setCreateTime(new Date());
        this.setDeposit(initData.getDeposit().toString());
        this.setRewardRatio(BigDecimal.valueOf(initData.getFee()).divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_FLOOR).doubleValue());
        this.setBlockCount(0L);
        GeoUtil.IpLocation ipLocation = GeoUtil.getIpLocation(getIp());
        BeanUtils.copyProperties(ipLocation, this);

    }
}
