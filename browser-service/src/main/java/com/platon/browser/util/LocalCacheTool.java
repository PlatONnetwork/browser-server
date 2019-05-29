package com.platon.browser.util;

import com.platon.browser.dto.NodeRespPage;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.dto.node.NodeListItem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/24 11:13
 * @Description:
 */
public class LocalCacheTool {
    public static final Map<String,String> TICKET_PRICE_MAP = new ConcurrentHashMap<>();
    public static final Map<String,String> NODEID_TICKETCOUNT_MAP = new ConcurrentHashMap<>();
    public static final Map<String, AppNodeListWrapper> APP_CHAINID_NODES_MAP = new ConcurrentHashMap<>();
    public static final Map<String, NodeRespPage> API_CHAINID_NODES_MAP = new ConcurrentHashMap<>();
}
