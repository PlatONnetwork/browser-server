package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Block;
import lombok.Data;

@Data
public class CollectionEvent {

    /**
     * 链路ID
     */
    private String traceId;

    // 区块信息
    private Block block;


    private EpochMessage epochMessage;



    /**
     * 释放对象引用
     */
    public void releaseRef() {
        block = null;
        epochMessage = null;
    }

}
