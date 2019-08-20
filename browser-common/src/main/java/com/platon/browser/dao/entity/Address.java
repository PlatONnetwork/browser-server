package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Address {
    private String address;

    private Integer type;

    private String balance;

    private String restrictingBalance;

    private String stakingValue;

    private String delegateValue;

    private String redeemedValue;

    private Integer txQty;

    private Integer transferQty;

    private Integer stakingQty;

    private Integer proposalQty;

    private Integer delegateQty;

    private Integer candidateCount;

    private String delegateHes;

    private String delegateLocked;

    private String delegateUnlock;

    private String delegateReduction;

    private String contractName;

    private String contractCreate;

    private String contractCreatehash;

    private Date createTime;

    private Date updateTime;

    private String rpPlan;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance == null ? null : balance.trim();
    }

    public String getRestrictingBalance() {
        return restrictingBalance;
    }

    public void setRestrictingBalance(String restrictingBalance) {
        this.restrictingBalance = restrictingBalance == null ? null : restrictingBalance.trim();
    }

    public String getStakingValue() {
        return stakingValue;
    }

    public void setStakingValue(String stakingValue) {
        this.stakingValue = stakingValue == null ? null : stakingValue.trim();
    }

    public String getDelegateValue() {
        return delegateValue;
    }

    public void setDelegateValue(String delegateValue) {
        this.delegateValue = delegateValue == null ? null : delegateValue.trim();
    }

    public String getRedeemedValue() {
        return redeemedValue;
    }

    public void setRedeemedValue(String redeemedValue) {
        this.redeemedValue = redeemedValue == null ? null : redeemedValue.trim();
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

    public Integer getDelegateQty() {
        return delegateQty;
    }

    public void setDelegateQty(Integer delegateQty) {
        this.delegateQty = delegateQty;
    }

    public Integer getCandidateCount() {
        return candidateCount;
    }

    public void setCandidateCount(Integer candidateCount) {
        this.candidateCount = candidateCount;
    }

    public String getDelegateHes() {
        return delegateHes;
    }

    public void setDelegateHes(String delegateHes) {
        this.delegateHes = delegateHes == null ? null : delegateHes.trim();
    }

    public String getDelegateLocked() {
        return delegateLocked;
    }

    public void setDelegateLocked(String delegateLocked) {
        this.delegateLocked = delegateLocked == null ? null : delegateLocked.trim();
    }

    public String getDelegateUnlock() {
        return delegateUnlock;
    }

    public void setDelegateUnlock(String delegateUnlock) {
        this.delegateUnlock = delegateUnlock == null ? null : delegateUnlock.trim();
    }

    public String getDelegateReduction() {
        return delegateReduction;
    }

    public void setDelegateReduction(String delegateReduction) {
        this.delegateReduction = delegateReduction == null ? null : delegateReduction.trim();
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

    public String getRpPlan() {
        return rpPlan;
    }

    public void setRpPlan(String rpPlan) {
        this.rpPlan = rpPlan == null ? null : rpPlan.trim();
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
        balance("balance", "balance", "VARCHAR", false),
        restrictingBalance("restricting_balance", "restrictingBalance", "VARCHAR", false),
        stakingValue("staking_value", "stakingValue", "VARCHAR", false),
        delegateValue("delegate_value", "delegateValue", "VARCHAR", false),
        redeemedValue("redeemed_value", "redeemedValue", "VARCHAR", false),
        txQty("tx_qty", "txQty", "INTEGER", false),
        transferQty("transfer_qty", "transferQty", "INTEGER", false),
        stakingQty("staking_qty", "stakingQty", "INTEGER", false),
        proposalQty("proposal_qty", "proposalQty", "INTEGER", false),
        delegateQty("delegate_qty", "delegateQty", "INTEGER", false),
        candidateCount("candidate_count", "candidateCount", "INTEGER", false),
        delegateHes("delegate_hes", "delegateHes", "VARCHAR", false),
        delegateLocked("delegate_locked", "delegateLocked", "VARCHAR", false),
        delegateUnlock("delegate_unlock", "delegateUnlock", "VARCHAR", false),
        delegateReduction("delegate_reduction", "delegateReduction", "VARCHAR", false),
        contractName("contract_name", "contractName", "VARCHAR", false),
        contractCreate("contract_create", "contractCreate", "VARCHAR", false),
        contractCreatehash("contract_createHash", "contractCreatehash", "VARCHAR", false),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        rpPlan("rp_plan", "rpPlan", "VARCHAR", false);

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