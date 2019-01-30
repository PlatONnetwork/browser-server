package com.platon.browser.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheTool {
    // 节点ID与节点名称的映射缓存
    public final static Map<String,String> NODE_ID_TO_NAME = new ConcurrentHashMap<>();
    // 交易接收者地址与接收者类型的映射缓存
    public final static Map<String,String> RECEIVER_TO_TYPE = new HashMap<>();
}
