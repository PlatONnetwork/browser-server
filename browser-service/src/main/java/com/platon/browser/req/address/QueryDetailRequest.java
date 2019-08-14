package com.platon.browser.req.address;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class QueryDetailRequest {
    @NotBlank(message = "{address not null}")
    private String address;
}