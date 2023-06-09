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
    private Integer bigVersion;
    public void setProgramVersion(String programVersion){
        this.programVersion = Integer.valueOf(programVersion);
        this.bigVersion = ChainVersionUtil.toBigVersion(new BigInteger(programVersion)).intValue();
    }
    private String nodeId;
    public void setNodeId(String nodeId){
        this.nodeId = HexUtil.prefix(nodeId);
    }
	public Integer getBigVersion() {
		return bigVersion;
	}
	public void setProgramVersion(Integer programVersion) {
        this.programVersion = programVersion;
		this.bigVersion = ChainVersionUtil.toBigVersion(new BigInteger(String.valueOf(programVersion))).intValue();
	}
	public String getNodeId() {
		return nodeId;
	}
}
