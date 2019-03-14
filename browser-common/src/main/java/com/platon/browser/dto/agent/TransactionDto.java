package com.platon.browser.dto.agent;

import lombok.Data;

import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 16:11
 */
@Data
public class TransactionDto {
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 交易时间（单位：秒）
     */
    private long timestamp;
    /**
     * 交易发起方地址
     */
    private String from;
    /**
     * 交易接收方地址
     */
    private String to;
    /**
     * 交易金额
     */
    private String value;
    /**
     * 区块高度
     */
    private BigInteger blockNumber;
    /**
     * 区块hash
     */
    private String blockHash;
    /**
     * 能量限制
     */
    private BigInteger energonLimit;
    /**
     * 能量消耗
     */
    private BigInteger energonUsed;
    /**
     * 能量价格
     */
    private BigInteger energonPrice;
    /**
     * 交易实际花费值(手续费)
     */
    private BigInteger actualTxCoast;
    /**
     * 交易类型
     transfer ：转账
     MPCtransaction ： MPC交易
     contractCreate ： 合约创建
     vote ： 投票
     transactionExecute ： 合约执行
     authorization ： 权限
     */
    private String txType;
    /**
     * Nonce值
     */
    private String nonce;
    /**
     * 交易输入数据
     */
    private String input;
    /**
     * 交易在区块中位置
     */
    private BigInteger transactionIndex;
    /**
     *  交易接收者类型（to是合约还是账户）contract合约、 account账户
     */
    private String receiveType;
    /**
     *  交易状态
     */
    private String txReceiptStatus;
}
