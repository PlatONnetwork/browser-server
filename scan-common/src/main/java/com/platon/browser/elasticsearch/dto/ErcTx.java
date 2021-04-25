package com.platon.browser.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author AgentRJ
 * @create 2020-09-23 17:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ErcTx {

    /**
     * 序号ID
     */
    private Long seq;

    /**
     * 合约名称
     */
    private String name;

    /**
     * 单位
     */
    private String symbol;

    /**
     * 精度
     */
    private Integer decimal;

    /**
     * 合约地址（也是交易to地址）
     */
    private String contract;

    /**
     * 交易哈希
     */
    private String hash;

    /**
     * 交易发起者（也是代币扣除方）
     */
    private String from;

    private String to;

    /**
     * 交易value
     */
    private String value;

    /**
     * 区块高度
     */
    private Long bn;

    /**
     * 区块时间
     */
    private Date bTime;

    /**
     * 接收方类型
     */
    private Integer toType;

    /**
     * 发送方类型
     */
    private Integer fromType;

    private String remark;

    /**
     * 手续费
     */
    private String txFee;

}
