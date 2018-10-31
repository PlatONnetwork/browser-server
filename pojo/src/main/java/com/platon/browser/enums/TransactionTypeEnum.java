package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum  TransactionTypeEnum {
    TRANSFER("transfer","转账"),
    MPC_TRANSACTION("MPCtransaction","MPC交易"),
    CONTRACT_CREATE("contractCreate","合约创建"),
    VOTE("vote","投票"),
    TRANSACTION_EXECUTE("transactionExecute","合约执行"),
    AUTHORIZATION("authorization","权限");

    public String code;
    public String desc;

    TransactionTypeEnum ( String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<String, TransactionTypeEnum> map = new HashMap<>();
    static {
        Arrays.asList(TransactionTypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
    }

    public static TransactionTypeEnum getEnum(String code){
        return map.get(code);
    }
}
