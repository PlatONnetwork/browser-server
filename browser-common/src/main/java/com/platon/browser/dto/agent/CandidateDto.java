package com.platon.browser.dto.agent;


import lombok.Data;

import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:11
 */
@Data
public class CandidateDto {

    //TODO:数据收集DTO节点相关暂时定义以下，后期补充以底层为主

    /**
     * 质押金额 (单位：ADP)
     */
    private BigInteger Deposit;
    /**
     * 质押金更新的最新块高
     */
    private BigInteger BlockNumber;
    /**
     * 质押金退款地址
     */
    private String Owner;
    /**
     * 所在区块交易索引
     */
    private Integer TxIndex;
    /**
     * 节点Id(公钥)
     */
    private String CandidateId;
    /**
     * 最新质押交易的发送方
     */
    private String From;
    /**
     * 出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
     */
    private Integer Fee;
    /**
     *  节点IP
     */
    private String Host;
    /**
     *  节点PORT
     */
    private String Port;
    /**
     * 附加数据(有长度限制，限制值待定)
     */
    private String Extra;
}