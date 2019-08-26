package com.platon.browser.param;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:15
 * txType=2004版本声明
 */
@Data
public class DeclareVersionParam {

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
    public void init(String activeNode,Integer version,String versionSigns){
        this.setActiveNode(activeNode);
        this.setVersion(version);
        this.setVersionSigns(versionSigns);
    }
}
