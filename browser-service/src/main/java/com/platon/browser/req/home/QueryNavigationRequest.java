package com.platon.browser.req.home;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class QueryNavigationRequest {
    @NotBlank(message = "{parameter not null}")
    private String parameter;
}