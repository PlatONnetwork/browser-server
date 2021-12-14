package com.platon.browser.enums;

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

    /**
     * 根据名称获取枚举
     *
     * @param name
     * @return com.platon.browser.v0151.enums.ErcTypeEnum
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/19
     */
    public static ErcTypeEnum getErcTypeEnum(String name) {
        for (ErcTypeEnum e : ErcTypeEnum.values()) {
            if (e.getDesc().equals(name)) {
                return e;
            }
        }
        return ErcTypeEnum.UNKNOWN;
    }

}