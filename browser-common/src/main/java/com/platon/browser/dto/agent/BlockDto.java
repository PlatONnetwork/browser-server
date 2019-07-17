package com.platon.browser.dto.agent;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 16:22
 */
@Data
public class BlockDto {
    /**
     * 区块高度
     */
    private Integer number;
    /**
     * 区块中交易列表
     */
    private List<TransactionDto> transaction;
    /**
     * 区块大小
     */
    private float size;
    /**
     * 区块hash
     */
    private String hash;
    /**
     * 能量消耗
     */
    private BigInteger energonUsed;
    /**
     * 平均能量消耗
     */
    private BigInteger energonAverage;
    /**
     * 能量限制
     */
    private BigInteger energonLimit;
    /**
     * 区块奖励
     */
    private String blockReward;
    /**
     * 父区块hash
     */
    private String parentHash;
    /**
     * 出块节点地址
     */
    private String miner;
    /**
     * 交易附加数据
     */
    private String extraData;
    /**
     *  Nonce值
     */
    private String nonce;
    private long timestamp;
    private Integer transactionNumber;
}
