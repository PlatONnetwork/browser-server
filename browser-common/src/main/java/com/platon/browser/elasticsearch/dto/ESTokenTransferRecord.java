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
public class ESTokenTransferRecord {
    private Long seq;                   // 序号ID
    private String hash;                // 交易哈希
    private Long bn;                    // 区块高度
    private String from;                // 交易发起者（也是代币扣除方）
    private String contract;            // 合约地址（也是交易to地址）
    private String tto;                 // 代币接收者地址
    private String tValue;              // 代币转账金额（未使用精度换算前）
    private Integer decimal;            // 精度
    private String name;                // 合约名称
    private String symbol;              // 代币符号
    private String sign;                // 方法签名
    private Integer result;             // 转账结果（1 成功，0 失败）
    private Date bTime;                 // 区块时间
    private String value;               // 交易value
    private String info;                // 回执信息
    private Date ctime;                 // 记录创建时间（以录入ES时间为主）
}
