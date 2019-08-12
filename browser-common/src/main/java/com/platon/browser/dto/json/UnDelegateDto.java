package com.platon.browser.dto.json;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:00
 * txType=1005减持/撤销委托(赎回委托)
 */
@Data
public class UnDelegateDto {

    /**
     * 代表着某个node的某次质押的唯一标示
     */
    private String stakingBlockNum;

    /**
     * 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;

    /**
     * 减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     */
    private String amount;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

}