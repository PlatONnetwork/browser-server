package com.platon.browser.client;

import com.alibaba.fastjson.annotation.JSONField;
import com.platon.browser.utils.HexTool;
import com.platon.browser.utils.VerUtil;

import java.math.BigInteger;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-22 10:18:43
 **/
public class NodeVersion {
    @JSONField(name = "ProgramVersion")
    private Integer bigVersion;
    public void setBigVersion(String bigVersion){
        this.bigVersion = VerUtil.transferBigVersion(new BigInteger(bigVersion)).intValue();
    }
    @JSONField(name = "NodeId")
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId = HexTool.prefix(nodeId);
    }
	public Integer getBigVersion() {
		return bigVersion;
	}
	public void setBigVersion(Integer bigVersion) {
		this.bigVersion = VerUtil.transferBigVersion(new BigInteger(String.valueOf(bigVersion))).intValue();
	}
	public String getNodeId() {
		return nodeId;
	}
}
