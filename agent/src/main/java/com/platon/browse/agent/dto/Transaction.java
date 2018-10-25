package com.platon.browse.agent.dto;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 16:11
 */
public class Transaction {

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
    private Integer blockNumber;

    /**
     * 区块hash
     */
    private String blockHash;

    /**
     * 能量限制
     */
    private Integer energonLimit;


    /**
     * 能量消耗
     */
    private Integer energonUsed;

    /**
     * 能量价格
     */
    private float energonPrice;

    /**
     * 交易实际花费值(手续费)
     */
    private float actualTxCoast;

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
    private Integer transactionIndex;

    public String getHash () {
        return hash;
    }

    public void setHash ( String hash ) {
        this.hash = hash;
    }

    public long getTimestamp () {
        return timestamp;
    }

    public void setTimestamp ( long timestamp ) {
        this.timestamp = timestamp;
    }

    public String getFrom () {
        return from;
    }

    public void setFrom ( String from ) {
        this.from = from;
    }

    public String getTo () {
        return to;
    }

    public void setTo ( String to ) {
        this.to = to;
    }

    public String getValue () {
        return value;
    }

    public void setValue ( String value ) {
        this.value = value;
    }

    public Integer getBlockNumber () {
        return blockNumber;
    }

    public void setBlockNumber ( Integer blockNumber ) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash () {
        return blockHash;
    }

    public void setBlockHash ( String blockHash ) {
        this.blockHash = blockHash;
    }

    public Integer getEnergonLimit () {
        return energonLimit;
    }

    public void setEnergonLimit ( Integer energonLimit ) {
        this.energonLimit = energonLimit;
    }

    public Integer getEnergonUsed () {
        return energonUsed;
    }

    public void setEnergonUsed ( Integer energonUsed ) {
        this.energonUsed = energonUsed;
    }

    public float getEnergonPrice () {
        return energonPrice;
    }

    public void setEnergonPrice ( float energonPrice ) {
        this.energonPrice = energonPrice;
    }

    public float getActualTxCoast () {
        return actualTxCoast;
    }

    public void setActualTxCoast ( float actualTxCoast ) {
        this.actualTxCoast = actualTxCoast;
    }

    public String getTxType () {
        return txType;
    }

    public void setTxType ( String txType ) {
        this.txType = txType;
    }

    public String getNonce () {
        return nonce;
    }

    public void setNonce ( String nonce ) {
        this.nonce = nonce;
    }

    public String getInput () {
        return input;
    }

    public void setInput ( String input ) {
        this.input = input;
    }

    public Integer getTransactionIndex () {
        return transactionIndex;
    }

    public void setTransactionIndex ( Integer transactionIndex ) {
        this.transactionIndex = transactionIndex;
    }
}
