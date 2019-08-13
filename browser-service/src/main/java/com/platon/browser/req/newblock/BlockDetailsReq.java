package com.platon.browser.req.newblock;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

import lombok.Data;

@Data
public class BlockDetailsReq extends PageReq{
    @NotBlank(message = "{number not null}")
    private Integer number;
}