package com.platon.browser.request.staking;

import com.platon.browser.utils.HexUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 验证人详情请求对象
 *
 * @author zhangrj
 * @file StakingDetailsReq.java
 * @description
 * @data 2019年8月31日
 */
public class StakingDetailsReq {

    @NotBlank(message = "{nodeId not null}")
    @Size(min = 128, max = 130)
    private String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        if (StringUtils.isBlank(nodeId)) return;
        this.nodeId = HexUtil.prefix(nodeId.toLowerCase());
    }

}