package com.platon.browser.common.dto.agent;

/**
 * User: dongqile
 * Date: 2018/12/4
 * Time: 11:20
 */
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
     * URL
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

    /**
     * 节点logo
     */
    private String url;

    public String getNodeName () {
        return nodeName;
    }

    public void setNodeName ( String nodeName ) {
        this.nodeName = nodeName;
    }

    public String getNodeDiscription () {
        return nodeDiscription;
    }

    public void setNodeDiscription ( String nodeDiscription ) {
        this.nodeDiscription = nodeDiscription;
    }

    public String getNodeDepartment () {
        return nodeDepartment;
    }

    public void setNodeDepartment ( String nodeDepartment ) {
        this.nodeDepartment = nodeDepartment;
    }

    public String getOfficialWebsite () {
        return officialWebsite;
    }

    public void setOfficialWebsite ( String officialWebsite ) {
        this.officialWebsite = officialWebsite;
    }

    public String getNodePortrait () {
        return nodePortrait;
    }

    public void setNodePortrait ( String nodePortrait ) {
        this.nodePortrait = nodePortrait;
    }

    public long getTime () {
        return time;
    }

    public void setTime ( long time ) {
        this.time = time;
    }

    public String getOwner () {
        return owner;
    }

    public void setOwner ( String owner ) {
        this.owner = owner;
    }

    public String getUrl () {
        return url;
    }

    public void setUrl ( String url ) {
        this.url = url;
    }
}