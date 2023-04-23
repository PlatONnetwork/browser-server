package com.platon.browser.v0152.bean;

import com.platon.browser.elasticsearch.dto.ErcTx;
import lombok.Data;

@Data
public class ErcTxInfo {
    private String name;                // 合约名称
    private String symbol;
    private Integer decimal;            // 精度
    private String contract;            // 合约地址（也是交易to地址）
    private String from;                // 交易发起者（也是代币扣除方）
    private String to;
    private String tokenId;             // tokenId
    private String value;               // 交易value
    private Integer toType;             // 接收方类型
    private Integer fromType;           // 发送方类型

    public static ErcTxInfo convertFromErcTx(ErcTx ercTx){
        ErcTxInfo ercTxInfo = new ErcTxInfo();
        ercTxInfo.setName(ercTx.getName());
        ercTxInfo.setSymbol(ercTx.getSymbol());
        ercTxInfo.setDecimal(ercTx.getDecimal());
        ercTxInfo.setContract(ercTx.getContract());
        ercTxInfo.setFrom(ercTx.getFrom());
        ercTxInfo.setTo(ercTx.getTo());
        ercTxInfo.setTokenId(ercTx.getTokenId());
        ercTxInfo.setValue(ercTx.getValue());
        ercTxInfo.setToType(ercTx.getToType());
        ercTxInfo.setFromType(ercTx.getFromType());
        return ercTxInfo;
    }
}
