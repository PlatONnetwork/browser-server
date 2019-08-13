package com.platon.browser.dto.json;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 15:15
 * txType=2004版本声明
 */
@Data
public class DeclareVersionDto {

    /**
     * 声明的节点，只能是验证人/候选人
     */
    private String activeNode;

    /**
     * 声明的版本
     */
    private Integer version;

    public void init(String activeNode,Integer version){
        this.setActiveNode(activeNode);
        this.setVersion(version);
    }
}