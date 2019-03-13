package com.platon.browser.bean;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dto.agent.CandidateDetailDto;
import com.platon.browser.dto.agent.CandidateDto;
import com.platon.browser.util.GeoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.platon.browser.utils.CacheTool.NODEID_TO_AVGTIME;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 16:22
 */
public class NodeRankingBean extends NodeRanking {

    public static final Map<String,GeoUtil.IpLocation> IP_MAP = new HashMap<>();

    public void init ( CandidateDto initData ) {
        try {
            CandidateDetailDto candidateDetailDto = JSONObject.parseObject(initData.getExtra(), CandidateDetailDto.class);
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
        GeoUtil.IpLocation ipLocation =IP_MAP.get(initData.getHost());
        if(StringUtils.isEmpty(ipLocation)){
            GeoUtil.IpLocation nowIpLocation = GeoUtil.getIpLocation(getIp());
            BeanUtils.copyProperties(nowIpLocation, this);
            IP_MAP.put(initData.getHost(),nowIpLocation);
        }else {
            BeanUtils.copyProperties(ipLocation, this);
        }
        Double avgTime = NODEID_TO_AVGTIME.get(this.getNodeId());
        if(avgTime!=null) this.setAvgTime(avgTime);
    }
}
