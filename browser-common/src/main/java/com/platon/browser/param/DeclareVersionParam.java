package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:15
 * txType=2004版本声明
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeclareVersionParam extends TxParam{

    /**
     * 声明的节点，只能是验证人/候选人
     */
    private String activeNode;

    /**
     * 声明的版本
     */
    private Integer version;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 声明的版本签名
     */
    private String versionSigns;
}
