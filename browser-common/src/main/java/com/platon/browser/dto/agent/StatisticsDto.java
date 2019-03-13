package com.platon.browser.dto.agent;

/**
 * User: dongqile
 * Date: 2018/12/5
 * Time: 20:58
 */
public class StatisticsDto {

    private String chainId;

    private String ip;

    private Integer port;

    private String address;

    private Double rewardRatio;

    private Integer nodeStatus;

    private String id;

    private String value;

    private String type;


    public String getValue () {
        return value;
    }

    public void setValue ( String value ) {
        this.value = value;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getId () {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public String getChainId () {
        return chainId;
    }

    public void setChainId ( String chainId ) {
        this.chainId = chainId;
    }

    public String getIp () {
        return ip;
    }

    public void setIp ( String ip ) {
        this.ip = ip;
    }

    public Integer getPort () {
        return port;
    }

    public void setPort ( Integer port ) {
        this.port = port;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress ( String address ) {
        this.address = address;
    }

    public Double getRewardRatio () {
        return rewardRatio;
    }

    public void setRewardRatio ( Double rewardRatio ) {
        this.rewardRatio = rewardRatio;
    }

    public Integer getNodeStatus () {
        return nodeStatus;
    }

    public void setNodeStatus ( Integer nodeStatus ) {
        this.nodeStatus = nodeStatus;
    }
}