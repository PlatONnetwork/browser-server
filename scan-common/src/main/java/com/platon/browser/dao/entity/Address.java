package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Address {
    private String address;

    private Integer type;

    private BigDecimal balance;

    private BigDecimal restrictingBalance;

    private BigDecimal stakingValue;

    private BigDecimal delegateValue;

    private BigDecimal redeemedValue;

    private Integer txQty;

    private Integer transferQty;

    private Integer delegateQty;

    private Integer stakingQty;

    private Integer proposalQty;

    private Integer candidateCount;

    private BigDecimal delegateHes;

    private BigDecimal delegateLocked;

    private BigDecimal delegateReleased;

    private String contractName;

    private String contractCreate;

    private String contractCreatehash;

    private String contractDestroyHash;

    private Date createTime;

    private Date updateTime;

    private BigDecimal haveReward;

    private Integer erc1155TxQty;

    private Integer erc721TxQty;

    private Integer erc20TxQty;

    private String contractBin;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getRestrictingBalance() {
        return restrictingBalance;
    }

    public void setRestrictingBalance(BigDecimal restrictingBalance) {
        this.restrictingBalance = restrictingBalance;
    }

    public BigDecimal getStakingValue() {
        return stakingValue;
    }

    public void setStakingValue(BigDecimal stakingValue) {
        this.stakingValue = stakingValue;
    }

    public BigDecimal getDelegateValue() {
        return delegateValue;
    }

    public void setDelegateValue(BigDecimal delegateValue) {
        this.delegateValue = delegateValue;
    }

    public BigDecimal getRedeemedValue() {
        return redeemedValue;
    }

    public void setRedeemedValue(BigDecimal redeemedValue) {
        this.redeemedValue = redeemedValue;
    }

    public Integer getTxQty() {
        return txQty;
    }

    public void setTxQty(Integer txQty) {
        this.txQty = txQty;
    }

    public Integer getTransferQty() {
        return transferQty;
    }

    public void setTransferQty(Integer transferQty) {
        this.transferQty = transferQty;
    }

    public Integer getDelegateQty() {
        return delegateQty;
    }

    public void setDelegateQty(Integer delegateQty) {
        this.delegateQty = delegateQty;
    }

    public Integer getStakingQty() {
        return stakingQty;
    }

    public void setStakingQty(Integer stakingQty) {
        this.stakingQty = stakingQty;
    }

    public Integer getProposalQty() {
        return proposalQty;
    }

    public void setProposalQty(Integer proposalQty) {
        this.proposalQty = proposalQty;
    }

    public Integer getCandidateCount() {
        return candidateCount;
    }

    public void setCandidateCount(Integer candidateCount) {
        this.candidateCount = candidateCount;
    }

    public BigDecimal getDelegateHes() {
        return delegateHes;
    }

    public void setDelegateHes(BigDecimal delegateHes) {
        this.delegateHes = delegateHes;
    }

    public BigDecimal getDelegateLocked() {
        return delegateLocked;
    }

    public void setDelegateLocked(BigDecimal delegateLocked) {
        this.delegateLocked = delegateLocked;
    }

    public BigDecimal getDelegateReleased() {
        return delegateReleased;
    }

    public void setDelegateReleased(BigDecimal delegateReleased) {
        this.delegateReleased = delegateReleased;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getContractCreate() {
        return contractCreate;
    }

    public void setContractCreate(String contractCreate) {
        this.contractCreate = contractCreate == null ? null : contractCreate.trim();
    }

    public String getContractCreatehash() {
        return contractCreatehash;
    }

    public void setContractCreatehash(String contractCreatehash) {
        this.contractCreatehash = contractCreatehash == null ? null : contractCreatehash.trim();
    }

    public String getContractDestroyHash() {
        return contractDestroyHash;
    }

    public void setContractDestroyHash(String contractDestroyHash) {
        this.contractDestroyHash = contractDestroyHash == null ? null : contractDestroyHash.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getHaveReward() {
        return haveReward;
    }

    public void setHaveReward(BigDecimal haveReward) {
        this.haveReward = haveReward;
    }

    public Integer getErc1155TxQty() {
        return erc1155TxQty;
    }

    public void setErc1155TxQty(Integer erc1155TxQty) {
        this.erc1155TxQty = erc1155TxQty;
    }

    public Integer getErc721TxQty() {
        return erc721TxQty;
    }

    public void setErc721TxQty(Integer erc721TxQty) {
        this.erc721TxQty = erc721TxQty;
    }

    public Integer getErc20TxQty() {
        return erc20TxQty;
    }

    public void setErc20TxQty(Integer erc20TxQty) {
        this.erc20TxQty = erc20TxQty;
    }

    public String getContractBin() {
        return contractBin;
    }

    public void setContractBin(String contractBin) {
        this.contractBin = contractBin == null ? null : contractBin.trim();
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table address
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        address("address", "address", "VARCHAR", false),
        type("type", "type", "INTEGER", true),
        balance("balance", "balance", "DECIMAL", false),
        restrictingBalance("restricting_balance", "restrictingBalance", "DECIMAL", false),
        stakingValue("staking_value", "stakingValue", "DECIMAL", false),
        delegateValue("delegate_value", "delegateValue", "DECIMAL", false),
        redeemedValue("redeemed_value", "redeemedValue", "DECIMAL", false),
        txQty("tx_qty", "txQty", "INTEGER", false),
        transferQty("transfer_qty", "transferQty", "INTEGER", false),
        delegateQty("delegate_qty", "delegateQty", "INTEGER", false),
        stakingQty("staking_qty", "stakingQty", "INTEGER", false),
        proposalQty("proposal_qty", "proposalQty", "INTEGER", false),
        candidateCount("candidate_count", "candidateCount", "INTEGER", false),
        delegateHes("delegate_hes", "delegateHes", "DECIMAL", false),
        delegateLocked("delegate_locked", "delegateLocked", "DECIMAL", false),
        delegateReleased("delegate_released", "delegateReleased", "DECIMAL", false),
        contractName("contract_name", "contractName", "VARCHAR", false),
        contractCreate("contract_create", "contractCreate", "VARCHAR", false),
        contractCreatehash("contract_createHash", "contractCreatehash", "VARCHAR", false),
        contractDestroyHash("contract_destroy_hash", "contractDestroyHash", "VARCHAR", false),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        haveReward("have_reward", "haveReward", "DECIMAL", false),
        erc1155TxQty("erc1155_tx_qty", "erc1155TxQty", "INTEGER", false),
        erc721TxQty("erc721_tx_qty", "erc721TxQty", "INTEGER", false),
        erc20TxQty("erc20_tx_qty", "erc20TxQty", "INTEGER", false),
        contractBin("contract_bin", "contractBin", "LONGVARCHAR", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
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
         * This method corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table address
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
         * This method corresponds to the database table address
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