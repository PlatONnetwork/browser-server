package com.platon.browser.complement.dao.param.proposal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处罚提案数据更新
 * @author Rongjin Zhang
 *
 */
@Data
@Accessors(chain = true)
public class ProposalSlash {
    //投票hash
    private String voteHash;
    //投票选项
    private Integer voteOption;
    //提案hash
    private String hash;

}
