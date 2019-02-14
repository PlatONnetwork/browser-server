package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum  TransactionTypeEnum {
    TRANSACTION_TRANSFER("transfer","转账"),
    TRANSACTION_MPC_TRANSACTION("MPCtransaction","MPC交易"),
    TRANSACTION_CONTRACT_CREATE("contractCreate","合约创建"),
    TRANSACTION_VOTE_TICKET("voteTicket","投票交易"),
    TRANSACTION_TRANSACTION_EXECUTE("transactionExecute","合约执行"),
    TRANSACTION_AUTHORIZATION("authorization","权限"),
    TRANSACTION_CANDIDATE_DEPOSIT("candidateDeposit","竞选质押"),
    TRANSACTION_CANDIDATE_APPLY_WITHDRAW("candidateApplyWithdraw","减持质押"),
    TRANSACTION_CANDIDATE_WITHDRAW("candidateWithdraw","提取质押"),
    TRANSACTION_UNKNOWN("unknown","未知");

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
