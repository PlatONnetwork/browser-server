package com.platon.browser.req.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/3/18
 * Time: 17:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CandidateTicketCountReq {
    @NotNull(message = "{nodeIds is not null}")
    private List<String> nodeIds;
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
}