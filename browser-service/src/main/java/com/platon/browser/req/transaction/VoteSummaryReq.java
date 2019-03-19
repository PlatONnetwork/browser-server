package com.platon.browser.req.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/3/18
 * Time: 17:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VoteSummaryReq {
    @NotNull(message = "{address is not null}")
    private List<String> addressList;
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
}