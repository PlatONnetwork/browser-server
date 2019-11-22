package com.platon.browser.client;

import com.alibaba.fastjson.annotation.JSONField;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.VerUtil;
import lombok.Data;

import java.math.BigInteger;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-22 10:18:43
 **/
@Data
public class NodeVersion {
    private Integer bigVersion;
    @JSONField(name = "NodeId")
    private String nodeId;
    @JSONField(name = "ProgramVersion")
    private int programVersion;
    public void setNodeId(String nodeId){
        this.nodeId = HexTool.prefix(nodeId);
        this.bigVersion= VerUtil.transferBigVersion(BigInteger.valueOf(this.programVersion)).intValue();
    }
}
