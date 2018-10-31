package com.platon.browser.common.constant;


/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:48
 */
public class ConfigConst extends ConfigLoader{

    private static final String NODE_IP = "node.ip";

    private static final String NODE_TYPE_KEY = "node.type";

    private static final String CHAIN_ID ="chain.id";

    public static String getNodeIp () {
        return getProperties().getProperty(NODE_IP);
    }

    public static String getNodeTypeKey () {
        return  getProperties().getProperty(NODE_TYPE_KEY);
    }

    public static String getChainId () {
        return getProperties().getProperty(CHAIN_ID);
    }
}