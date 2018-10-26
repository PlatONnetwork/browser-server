package com.platon.browser.common.enums;

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

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
