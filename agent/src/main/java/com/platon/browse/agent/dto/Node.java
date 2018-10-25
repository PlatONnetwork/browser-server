package com.platon.browse.agent.dto;


/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:11
 */
public class Node {

    //TODO:数据收集DTO节点相关暂时定义以下，后期补充以底层为主

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点地址
     */
    private String nodeAddress;

    /**
     * 节点ip
     */
    private String ip;

    /**
     * 节点矿工地址
     */
    private String  miner;

    public String getNodeId () {
        return nodeId;
    }

    public void setNodeId ( String nodeId ) {
        this.nodeId = nodeId;
    }

    public String getNodeName () {
        return nodeName;
    }

    public void setNodeName ( String nodeName ) {
        this.nodeName = nodeName;
    }

    public String getNodeAddress () {
        return nodeAddress;
    }

    public void setNodeAddress ( String nodeAddress ) {
        this.nodeAddress = nodeAddress;
    }

    public String getIp () {
        return ip;
    }

    public void setIp ( String ip ) {
        this.ip = ip;
    }

    public String getMiner () {
        return miner;
    }

    public void setMiner ( String miner ) {
        this.miner = miner;
    }
}