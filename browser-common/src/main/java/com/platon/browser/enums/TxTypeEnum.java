package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2019/8/6
 * \* Time: 10:17
 * \
 */
public enum TxTypeEnum {

    TRANSFER(0, "转账"),
    CONTRACT_CREATION(1,"合约发布(合约创建)"),
    CONTRACT_EXECUTION(2,"合约调用(合约执行)"),
    OTHERS(4,"其他"),
    MPC(5,"MPC交易"),
    CREATE_VALIDATOR(1000,"发起质押(创建验证人)"),
    EDIT_VALIDATOR(1001,"修改质押信息(编辑验证人)"),
    INCREASE_STAKING(1002,"增持质押(增加自有质押)"),
    EXIT_VALIDATOR(1003,"撤销质押(退出验证人)"),
    DELEGATE(1004,"发起委托(委托)"),
    UN_DELEGATE(1005,"减持/撤销委托(赎回委托)"),
    CREATE_PROPOSAL_TEXT(2000,"提交文本提案(创建提案)"),
    CREATE_PROPOSAL_UPGRADE(2001,"提交升级提案(创建提案)"),
    CREATE_PROPOSAL_PARAMETER(2002,"提交参数提案(创建提案)"),
    CANCEL_PROPOSAL(2005,"提交取消提案"),
    VOTING_PROPOSAL(2003,"给提案投票(提案投票)"),
    DECLARE_VERSION(2004,"版本声明"),
    REPORT_VALIDATOR(3000,"举报多签(举报验证人)"),
    CREATE_RESTRICTING(4000,"创建锁仓计划(创建锁仓)"),
    DUPLICATE_SIGN(11,"区块双签"),
    ;



    private static Map<Integer, TxTypeEnum> map = new HashMap<>();
    static {
        Arrays.asList(TxTypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
    }

    public static TxTypeEnum getEnum(Integer code){
        return map.get(code);
    }

    public int code;
    public String desc;

    TxTypeEnum ( int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
