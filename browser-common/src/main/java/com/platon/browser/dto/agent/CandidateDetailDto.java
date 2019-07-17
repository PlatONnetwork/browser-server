package com.platon.browser.dto.agent;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2018/12/4
 * Time: 11:20
 */
@Data
public class CandidateDetailDto {
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点简介
     */
    private String nodeDiscription;
    /**
     * 机构名称
     */
    private String nodeDepartment;
    /**
     * 官网
     */
    private String officialWebsite;
    /**
     * 节点logo
     */
    private String nodePortrait;
    /**
     * 加入时间
     */
    private long time;
    /**
     * 节点退款地址
     */
    private String owner;
}