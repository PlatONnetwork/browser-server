package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum  TransactionTypeEnum {
    TRANSACTION_TRANSFER("transfer","转账",0),
    TRANSACTION_MPC_TRANSACTION("MPCtransaction","MPC交易",5),
    TRANSACTION_CONTRACT_CREATE("contractCreate","合约创建",1),
    TRANSACTION_VOTE_TICKET("voteTicket","投票交易",2003),
    TRANSACTION_TRANSACTION_EXECUTE("transactionExecute","合约执行",2),
    TRANSACTION_AUTHORIZATION("authorization","权限",1004),
    TRANSACTION_CANDIDATE_DEPOSIT("candidateDeposit","竞选质押",1000),
    TRANSACTION_CANDIDATE_APPLY_WITHDRAW("candidateApplyWithdraw","减持质押",1005),
    TRANSACTION_CANDIDATE_WITHDRAW("candidateWithdraw","提取质押",1003),
    TRANSACTION_UNKNOWN("unknown","未知",4);

    public String code;
    public String desc;
    public int num;

    TransactionTypeEnum ( String code, String desc,int num) {
        this.code = code;
        this.desc = desc;
        this.num = num;
    }

    private static Map<String, TransactionTypeEnum> map = new HashMap<>();
    private static Map<Integer, TransactionTypeEnum> numMap = new HashMap<>();
    static {
        Arrays.asList(TransactionTypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
        Arrays.asList(TransactionTypeEnum.values()).forEach(typeEnum->numMap.put(typeEnum.num,typeEnum));
    }

    public static TransactionTypeEnum getEnum(String code){
        return map.get(code);
    }
    public static TransactionTypeEnum getEnum(Integer num){
    	return numMap.get(num);
    }
}
