package com.platon.browser.enums;

/**
 * token类型枚举
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/1/29
 */
public enum TokenTypeEnum {

    ERC20("erc20", "erc20合约"),
    ERC721("erc721", "erc721合约"),
    ERC1155("erc1155", "erc1155合约");

    private String type;

    private String desc;

    TokenTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
