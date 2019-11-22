package com.platon.browser.client;

import com.alibaba.fastjson.annotation.JSONField;
import com.platon.browser.utils.HexTool;
import lombok.Data;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-22 10:18:43
 **/
@Data
public class NodeVersion {
    @JSONField(name = "ProgramVersion")
    private Integer bigVersion;
    @JSONField(name = "NodeId")
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId = HexTool.prefix(nodeId);
    }
}
