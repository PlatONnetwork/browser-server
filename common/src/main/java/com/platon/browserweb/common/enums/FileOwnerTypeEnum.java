package com.platon.browserweb.common.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/6/27
 * \* Time: 20:34
 * \
 */
public enum FileOwnerTypeEnum {
    INVESTOR_ID_AUTH_FRONT("investorIdAuthFront","投资者身份任务正面照"),
    INVESTOR_ID_AUTH_REVERSE("investorIdAuthReverse","投资者身份任务背面照"),
    INVESTOR_ID_AUTH_HANDLE("investorIdAuthHandle","投资者身份任务背手持身份证照"),
    INVESTOR_ID_AUTH_ADDRESS("investorIdAuthAddress","投资者身份任务居住地证明"),
    INVESTOR_UNLOCK_AUTH_FRONT("investorUnlockAuthFront","投资者身份任务正面照"),
    INVESTOR_UNLOCK_AUTH_HANDLE("investorUnlockAuthHandle","投资者身份任务背手持身份证照"),
    INVESTOR_ID_AUTH_BL("investorIdAuthBL","投资者营业执照"),
    INVESTOR_ICON("investorIcon","投资者头像");

    private String code;
    private String desc;

    private FileOwnerTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static FileOwnerTypeEnum getEnumByCodeValue(String code){
        FileOwnerTypeEnum[] allEnums = values();
        for(FileOwnerTypeEnum e : allEnums){
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }

}