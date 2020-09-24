package com.platon.browser.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author AgentRJ
 * @create 2020-09-23 17:46
 */
@Data
@Builder
@Accessors(chain = true)
public class ESTokenTransferRecord {
    private Long id;                    // 序号ID
    private String txHash;              // 交易哈希
    private Long blockNumber;           // 区块高度
    private String txFrom;              // 交易发起者（也是代币扣除方）
    private String contract;            // 合约地址（也是交易to地址）
    private String transferTo;          // 代币接收者地址
    private BigDecimal transferValue;   // 代币转账金额（未使用精度换算前）
    private Integer decimal;            // 精度
    private String symbol;              // 代币符号
    private String methodSign;          // 方法签名
    private Integer result;             // 转账结果（1 成功，0 失败）
    private Date blockTimestamp;        // 区块时间
    private BigDecimal value;           // 交易value
    private Date createTime;            // 记录创建时间（以录入ES时间为主）
}
