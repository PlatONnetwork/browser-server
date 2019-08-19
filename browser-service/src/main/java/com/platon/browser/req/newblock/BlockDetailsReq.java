package com.platon.browser.req.newblock;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BlockDetailsReq {
    @NotNull(message = "{number not null}")
    private Integer number;
}