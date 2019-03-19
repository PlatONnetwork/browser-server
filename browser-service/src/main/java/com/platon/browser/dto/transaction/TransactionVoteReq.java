package com.platon.browser.dto.transaction;

import com.platon.browser.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/3/15
 * Time: 16:28
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionVoteReq extends PageReq {
    @NotNull(message = "{address is not null}")
    private List<String> walletAddrs;
    @NotBlank(message = "{chain.id.notnull}")
    private String cid;
}