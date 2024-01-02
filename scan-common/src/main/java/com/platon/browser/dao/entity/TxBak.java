package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TxBak {
    private Long id;

    private String hash;

    private String bHash;

    private Long num;

    private Integer index;

    private Date time;

    private String nonce;

    private Integer status;

    private String gasPrice;

    private String gasUsed;

    private String gasLimit;

    private String from;

    private String to;

    private String value;

    private Integer type;

    private String cost;

    private Integer toType;

    private Long seq;

    private Date creTime;

    private Date updTime;

    private Integer contractType;

    private String contractAddress;


    //1.5.0
    private long chainId;         //链id
    private String accessListInfo;
    private int rawEthTxType;       // "0x2" 0 - 传统交易（旧） 1 - AccessList交易 2 - DynamicFee交易
    private String maxFeePerGas;    //10进制字符串
    private String maxPriorityFeePerGas;  //10进制字符串


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public String getbHash() {
        return bHash;
    }

    public void setbHash(String bHash) {
        this.bHash = bHash == null ? null : bHash.trim();
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce == null ? null : nonce.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice == null ? null : gasPrice.trim();
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed == null ? null : gasUsed.trim();
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit == null ? null : gasLimit.trim();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from == null ? null : from.trim();
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to == null ? null : to.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost == null ? null : cost.trim();
    }

    public Integer getToType() {
        return toType;
    }

    public void setToType(Integer toType) {
        this.toType = toType;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Date getCreTime() {
        return creTime;
    }

    public void setCreTime(Date creTime) {
        this.creTime = creTime;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress == null ? null : contractAddress.trim();
    }

    public long getChainId() {
        return chainId;
    }

    public void setChainId(long chainId) {
        this.chainId = chainId;
    }

    public String getAccessListInfo() {
        return accessListInfo;
    }

    public void setAccessListInfo(String accessListInfo) {
        this.accessListInfo = accessListInfo;
    }

    public int getRawEthTxType() {
        return rawEthTxType;
    }

    public void setRawEthTxType(int rawEthTxType) {
        this.rawEthTxType = rawEthTxType;
    }

    public String getMaxFeePerGas() {
        return maxFeePerGas;
    }

    public void setMaxFeePerGas(String maxFeePerGas) {
        this.maxFeePerGas = maxFeePerGas;
    }

    public String getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public void setMaxPriorityFeePerGas(String maxPriorityFeePerGas) {
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table tx_bak
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        id("id", "id", "BIGINT", false),
        hash("hash", "hash", "VARCHAR", false),
        bHash("b_hash", "bHash", "VARCHAR", false),
        num("num", "num", "BIGINT", false),
        index("index", "index", "INTEGER", true),
        time("time", "time", "TIMESTAMP", true),
        nonce("nonce", "nonce", "VARCHAR", false),
        status("status", "status", "INTEGER", true),
        gasPrice("gas_price", "gasPrice", "VARCHAR", false),
        gasUsed("gas_used", "gasUsed", "VARCHAR", false),
        gasLimit("gas_limit", "gasLimit", "VARCHAR", false),
        from("from", "from", "VARCHAR", true),
        to("to", "to", "VARCHAR", true),
        value("value", "value", "VARCHAR", true),
        type("type", "type", "INTEGER", true),
        cost("cost", "cost", "VARCHAR", false),
        toType("to_type", "toType", "INTEGER", false),
        seq("seq", "seq", "BIGINT", false),
        creTime("cre_time", "creTime", "TIMESTAMP", false),
        updTime("upd_time", "updTime", "TIMESTAMP", false),
        contractType("contract_type", "contractType", "INTEGER", false),
        contractAddress("contract_address", "contractAddress", "VARCHAR", false),
        input("input", "input", "LONGVARCHAR", true),
        info("info", "info", "LONGVARCHAR", false),
        erc1155TxInfo("erc1155_tx_info", "erc1155TxInfo", "LONGVARCHAR", false),
        erc721TxInfo("erc721_tx_info", "erc721TxInfo", "LONGVARCHAR", false),
        erc20TxInfo("erc20_tx_info", "erc20TxInfo", "LONGVARCHAR", false),
        transferTxInfo("transfer_tx_info", "transferTxInfo", "LONGVARCHAR", false),
        pposTxInfo("ppos_tx_info", "pposTxInfo", "LONGVARCHAR", false),
        failReason("fail_reason", "failReason", "LONGVARCHAR", false),
        method("method", "method", "LONGVARCHAR", true),

        chainId("chain_id", "chainId", "BIGINT", false),
        rawEthTxType("raw_eth_tx_type", "rawEthTxType", "INTEGER", false),
        maxFeePerGas("max_fee_per_gas", "maxFeePerGas", "VARCHAR", false),
        maxPriorityFeePerGas("max_priority_fee_per_gas", "maxPriorityFeePerGas", "VARCHAR", false),
        accessListInfo("access_list_info", "accessListInfo", "LONGVARCHAR", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public static Column[] excludes(Column ... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table tx_bak
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
            } else {
                return this.column;
            }
        }
    }
}
