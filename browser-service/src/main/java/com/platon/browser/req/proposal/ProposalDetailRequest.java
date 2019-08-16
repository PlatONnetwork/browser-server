package com.platon.browser.req.proposal;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProposalDetailRequest {
    @NotBlank(message = "{proposalHash not null}")
    private String proposalHash;
}