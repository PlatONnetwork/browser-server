package com.platon.browser.req.newtransaction;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

import lombok.Data;

@Data
public class TransactionDetailsReq extends PageReq{
    @NotBlank(message = "{txHash not null}")
    private String txHash;
}