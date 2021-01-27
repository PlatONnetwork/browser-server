package com.platon.browser.response.token;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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

    private Long seq;
    private String txHash;
    private Long blockNumber;
    private String txFrom;
    private String contract;
    private String transferTo;
    private BigDecimal transferValue;
    private Integer decimal;
    private String symbol;
    private String name;
    private String methodSign;
    private Integer result;
    private Date blockTimestamp;
    private BigDecimal value;
    private String tokenId;
    private Long systemTimestamp;
    private String type;   // 交易类型：input 进账， out 出账
    private Integer fromType; // 地址类型：1-账户地址，2-内置地址，3-evm地址，4-wasm地址，5-evm-erc地址，
    private Integer toType; // 地址类型：1-账户地址，2-内置地址，3-evm地址，4-wasm地址，5-evm-erc地址，

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

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

}
