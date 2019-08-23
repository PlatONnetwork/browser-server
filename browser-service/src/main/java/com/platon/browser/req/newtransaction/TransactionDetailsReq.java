package com.platon.browser.req.newtransaction;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TransactionDetailsReq{
    @NotBlank(message = "{txHash not null}")
    private String txHash;
}