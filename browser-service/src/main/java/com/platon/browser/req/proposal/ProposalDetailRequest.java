package com.platon.browser.req.proposal;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 提案详情请求对象
 *  @file ProposalDetailRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class ProposalDetailRequest {
    @NotBlank(message = "{proposalHash not null}")
    private String proposalHash;
}