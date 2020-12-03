package com.platon.browser.res.token;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Erc20 合约内部转账交易数据
 *
 * @author AgentRJ
 * @create 2020-09-23 14:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryTokenTransferRecordListResp {

    @ApiModelProperty(value = "交易哈希")
    private String txHash;
    @ApiModelProperty(value = "区块高度")
    private Long blockNumber;
    @ApiModelProperty(value = "交易发起者")
    private String txFrom;
    @ApiModelProperty(value = "Token合约（交易to）")
    private String contract;
    @ApiModelProperty(value = "代币转账接收者")
    private String transferTo;
    @ApiModelProperty(value = "代币转账金额（单位：1）")
    private BigDecimal transferValue;
    @ApiModelProperty(value = "代币精度")
    private Integer decimal;
    @ApiModelProperty(value = "代币符号")
    private String symbol;
    @ApiModelProperty(value = "代币名称")
    private String name;
    @ApiModelProperty(value = "交易函数签名")
    private String methodSign;
    @ApiModelProperty(value = "交易结果 0 失败，1 成功")
    private Integer result;
    @ApiModelProperty(value = "区块时间")
    private Date blockTimestamp;
    @ApiModelProperty(value = "交易value金额（单位：LAT）")
    private BigDecimal value;
    @ApiModelProperty(value = "服务器时间")
    private Long systemTimestamp;
    @ApiModelProperty(value = "交易方向：INPUT 入账，OUT 出账")
    private String type;   // 交易类型：input 进账， out 出账
    @ApiModelProperty(value = "from 类型")
    private Integer fromType; // 地址类型：1-账户地址，2-内置地址，3-evm地址，4-wasm地址，5-evm-erc地址，
    @ApiModelProperty(value = "to类型")
    private Integer toType; // 地址类型：1-账户地址，2-内置地址，3-evm地址，4-wasm地址，5-evm-erc地址，

    private Long seq;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxHash() {
        return this.txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    public Long getBlockNumber() {
        return this.blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTxFrom() {
        return this.txFrom;
    }

    public void setTxFrom(String txFrom) {
        this.txFrom = txFrom == null ? null : txFrom.trim();
    }

    public String getContract() {
        return this.contract;
    }

    public void setContract(String contract) {
        this.contract = contract == null ? null : contract.trim();
    }

    public String getTransferTo() {
        return this.transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo == null ? null : transferTo.trim();
    }

    public BigDecimal getTransferValue() {
        return this.transferValue;
    }

    public void setTransferValue(BigDecimal transferValue) {
        this.transferValue = transferValue;
    }

    public Integer getDecimal() {
        return this.decimal;
    }

    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public String getMethodSign() {
        return this.methodSign;
    }

    public void setMethodSign(String methodSign) {
        this.methodSign = methodSign == null ? null : methodSign.trim();
    }

    public Integer getResult() {
        return this.result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getBlockTimestamp() {
        return this.blockTimestamp;
    }

    public void setBlockTimestamp(Date blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getSystemTimestamp() {
        return this.systemTimestamp;
    }

    public void setSystemTimestamp(Long systemTimestamp) {
        this.systemTimestamp = systemTimestamp;
    }

    public static enum TransferType {

        INPUT("INPUT"),
        OUT("OUT"),
        NONE("NONE"),
        ;

        String type;

        TransferType(String type) {
            this.type = type;
        }

        public String val() {
            return this.type;
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}
