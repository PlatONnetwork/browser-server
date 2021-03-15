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
    private Long seq;                   // 序号ID
    private String name;                // 合约名称
    private String symbol;
    private Integer decimal;            // 精度
    private String contract;            // 合约地址（也是交易to地址）
    private String hash;                // 交易哈希
    private String from;                // 交易发起者（也是代币扣除方）
    private String to;
    private String value;               // 交易value
    private Long bn;                    // 区块高度
    private Date bTime;                 // 区块时间
    private Integer toType;             // 接收方类型
    private Integer fromType;           // 发送方类型
    private String remark;
    private String txFee;               // 手续费
}
