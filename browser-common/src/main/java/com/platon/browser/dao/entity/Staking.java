package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Staking extends StakingKey {
    private Integer stakingTxIndex;

    private String stakingAddr;

    private String stakingHas;

    private String stakingLocked;

    private String statDelegateHas;

    private String statDelegateLocked;

    private String statDelegateReduction;

    private Integer statDelegateQty;

    private Integer statVerifierTime;

    private String stakingIcon;

    private String externalId;

    private String denefitAddr;

    private String expectedIncome;

    private String blockRewardValue;

    private String preSetBlockRewardValue;

    private Long curConsBlockQty;

    private Long preConsBlockQty;

    private String stakingRewardValue;

    private String webSite;

    private String details;

    private Date joinTime;

    private Date leaveTime;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer isConsensus;

    private Integer stakingEpoch;

    private String stakingReduction;

    private String stakingName;

    private Integer isSetting;

    private Integer isInit;

    public Integer getStakingTxIndex() {
        return stakingTxIndex;
    }

    public void setStakingTxIndex(Integer stakingTxIndex) {
        this.stakingTxIndex = stakingTxIndex;
    }

    public String getStakingAddr() {
        return stakingAddr;
    }

    public void setStakingAddr(String stakingAddr) {
        this.stakingAddr = stakingAddr == null ? null : stakingAddr.trim();
    }

    public String getStakingHas() {
        return stakingHas;
    }

    public void setStakingHas(String stakingHas) {
        this.stakingHas = stakingHas == null ? null : stakingHas.trim();
    }

    public String getStakingLocked() {
        return stakingLocked;
    }

    public void setStakingLocked(String stakingLocked) {
        this.stakingLocked = stakingLocked == null ? null : stakingLocked.trim();
    }

    public String getStatDelegateHas() {
        return statDelegateHas;
    }

    public void setStatDelegateHas(String statDelegateHas) {
        this.statDelegateHas = statDelegateHas == null ? null : statDelegateHas.trim();
    }

    public String getStatDelegateLocked() {
        return statDelegateLocked;
    }

    public void setStatDelegateLocked(String statDelegateLocked) {
        this.statDelegateLocked = statDelegateLocked == null ? null : statDelegateLocked.trim();
    }

    public String getStatDelegateReduction() {
        return statDelegateReduction;
    }

    public void setStatDelegateReduction(String statDelegateReduction) {
        this.statDelegateReduction = statDelegateReduction == null ? null : statDelegateReduction.trim();
    }

    public Integer getStatDelegateQty() {
        return statDelegateQty;
    }

    public void setStatDelegateQty(Integer statDelegateQty) {
        this.statDelegateQty = statDelegateQty;
    }

    public Integer getStatVerifierTime() {
        return statVerifierTime;
    }

    public void setStatVerifierTime(Integer statVerifierTime) {
        this.statVerifierTime = statVerifierTime;
    }

    public String getStakingIcon() {
        return stakingIcon;
    }

    public void setStakingIcon(String stakingIcon) {
        this.stakingIcon = stakingIcon == null ? null : stakingIcon.trim();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId == null ? null : externalId.trim();
    }

    public String getDenefitAddr() {
        return denefitAddr;
    }

    public void setDenefitAddr(String denefitAddr) {
        this.denefitAddr = denefitAddr == null ? null : denefitAddr.trim();
    }

    public String getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(String expectedIncome) {
        this.expectedIncome = expectedIncome == null ? null : expectedIncome.trim();
    }

    public String getBlockRewardValue() {
        return blockRewardValue;
    }

    public void setBlockRewardValue(String blockRewardValue) {
        this.blockRewardValue = blockRewardValue == null ? null : blockRewardValue.trim();
    }

    public String getPreSetBlockRewardValue() {
        return preSetBlockRewardValue;
    }

    public void setPreSetBlockRewardValue(String preSetBlockRewardValue) {
        this.preSetBlockRewardValue = preSetBlockRewardValue == null ? null : preSetBlockRewardValue.trim();
    }

    public Long getCurConsBlockQty() {
        return curConsBlockQty;
    }

    public void setCurConsBlockQty(Long curConsBlockQty) {
        this.curConsBlockQty = curConsBlockQty;
    }

    public Long getPreConsBlockQty() {
        return preConsBlockQty;
    }

    public void setPreConsBlockQty(Long preConsBlockQty) {
        this.preConsBlockQty = preConsBlockQty;
    }

    public String getStakingRewardValue() {
        return stakingRewardValue;
    }

    public void setStakingRewardValue(String stakingRewardValue) {
        this.stakingRewardValue = stakingRewardValue == null ? null : stakingRewardValue.trim();
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite == null ? null : webSite.trim();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getIsConsensus() {
        return isConsensus;
    }

    public void setIsConsensus(Integer isConsensus) {
        this.isConsensus = isConsensus;
    }

    public Integer getStakingEpoch() {
        return stakingEpoch;
    }

    public void setStakingEpoch(Integer stakingEpoch) {
        this.stakingEpoch = stakingEpoch;
    }

    public String getStakingReduction() {
        return stakingReduction;
    }

    public void setStakingReduction(String stakingReduction) {
        this.stakingReduction = stakingReduction == null ? null : stakingReduction.trim();
    }

    public String getStakingName() {
        return stakingName;
    }

    public void setStakingName(String stakingName) {
        this.stakingName = stakingName == null ? null : stakingName.trim();
    }

    public Integer getIsSetting() {
        return isSetting;
    }

    public void setIsSetting(Integer isSetting) {
        this.isSetting = isSetting;
    }

    public Integer getIsInit() {
        return isInit;
    }

    public void setIsInit(Integer isInit) {
        this.isInit = isInit;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table staking
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        stakingBlockNum("staking_block_num", "stakingBlockNum", "BIGINT", false),
        nodeId("node_id", "nodeId", "VARCHAR", false),
        stakingTxIndex("staking_tx_index", "stakingTxIndex", "INTEGER", false),
        stakingAddr("staking_addr", "stakingAddr", "VARCHAR", false),
        stakingHas("staking_has", "stakingHas", "VARCHAR", false),
        stakingLocked("staking_locked", "stakingLocked", "VARCHAR", false),
        statDelegateHas("stat_delegate_has", "statDelegateHas", "VARCHAR", false),
        statDelegateLocked("stat_delegate_locked", "statDelegateLocked", "VARCHAR", false),
        statDelegateReduction("stat_delegate_reduction", "statDelegateReduction", "VARCHAR", false),
        statDelegateQty("stat_delegate_qty", "statDelegateQty", "INTEGER", false),
        statVerifierTime("stat_verifier_time", "statVerifierTime", "INTEGER", false),
        stakingIcon("staking_icon", "stakingIcon", "VARCHAR", false),
        externalId("external_id", "externalId", "VARCHAR", false),
        denefitAddr("denefit_addr", "denefitAddr", "VARCHAR", false),
        expectedIncome("expected_income", "expectedIncome", "VARCHAR", false),
        blockRewardValue("block_reward_value", "blockRewardValue", "VARCHAR", false),
        preSetBlockRewardValue("pre_set_block_reward_value", "preSetBlockRewardValue", "VARCHAR", false),
        curConsBlockQty("cur_cons_block_qty", "curConsBlockQty", "BIGINT", false),
        preConsBlockQty("pre_cons_block_qty", "preConsBlockQty", "BIGINT", false),
        stakingRewardValue("staking_reward_value", "stakingRewardValue", "VARCHAR", false),
        webSite("web_site", "webSite", "VARCHAR", false),
        details("details", "details", "VARCHAR", false),
        joinTime("join_time", "joinTime", "TIMESTAMP", false),
        leaveTime("leave_time", "leaveTime", "TIMESTAMP", false),
        status("status", "status", "INTEGER", true),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        isConsensus("is_consensus", "isConsensus", "INTEGER", false),
        stakingEpoch("staking_epoch", "stakingEpoch", "INTEGER", false),
        stakingReduction("staking_reduction", "stakingReduction", "VARCHAR", false),
        stakingName("staking_name", "stakingName", "VARCHAR", false),
        isSetting("is_setting", "isSetting", "INTEGER", false),
        isInit("is_init", "isInit", "INTEGER", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
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
         * This method corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table staking
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
         * This method corresponds to the database table staking
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