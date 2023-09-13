package com.platon.browser.bean;

import com.platon.browser.utils.ChainVersionUtil;
import lombok.Data;

import java.math.BigInteger;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-22 10:18:43
 **/
@Data
public class NodeVersion {
    private String nodeId;
    private Integer programVersion;

	public Integer getBigVersion() {
		return ChainVersionUtil.toBigVersion(new BigInteger(String.valueOf(programVersion))).intValue();
	}
}
