package com.platon.browser.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.utils.ChainVersionUtil;

import java.math.BigInteger;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-22 10:18:43
 **/
public class NodeVersion {
    private Integer programVersion;
    public Integer getProgramVersion(){
        return programVersion;
    }
    @JSONField(name = "ProgramVersion")
    private Integer bigVersion;
    public void setBigVersion(String bigVersion){
        this.programVersion = Integer.valueOf(bigVersion);
        this.bigVersion = ChainVersionUtil.toBigVersion(new BigInteger(bigVersion)).intValue();
    }
    @JSONField(name = "NodeId")
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId = HexUtil.prefix(nodeId);
    }
	public Integer getBigVersion() {
		return bigVersion;
	}
	public void setBigVersion(Integer bigVersion) {
        this.programVersion = bigVersion;
		this.bigVersion = ChainVersionUtil.toBigVersion(new BigInteger(String.valueOf(bigVersion))).intValue();
	}
	public String getNodeId() {
		return nodeId;
	}
}
