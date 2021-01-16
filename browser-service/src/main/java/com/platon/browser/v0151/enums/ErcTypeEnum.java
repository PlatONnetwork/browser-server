package com.platon.browser.v0151.enums;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description:
 */
public enum ErcTypeEnum {
    UNKNOWN("unknown"),
    ERC20("erc20"),
    ERC721("erc721");
    private String desc;
    ErcTypeEnum(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
}