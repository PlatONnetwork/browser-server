package com.platon.browser.req.newblock;

import lombok.Data;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

@Data
public class BlockListByNodeIdReq extends PageReq{
	@NotBlank(message="{nodeId is not null}")
    private String nodeId;
}