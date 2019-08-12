package com.platon.browser.dto.json;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:21
 * txType=3000多签举报证据模板
 */
@Data
public class EvidencesDto {
    /**
     * 举报的节点id
     */
    private String verify;

    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String nodeName;

    /**
     * 质押交易快高
     */
    private String stakingBlockNum;
}