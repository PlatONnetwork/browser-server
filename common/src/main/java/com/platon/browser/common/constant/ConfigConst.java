package com.platon.browser.common.constant;

import com.platon.browser.common.constant.ConfigLoader;

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
        return NODE_IP;
    }

    public static String getNodeTypeKey () {
        return NODE_TYPE_KEY;
    }

    public static String getChainId () {
        return CHAIN_ID;
    }
}