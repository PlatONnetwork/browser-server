package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Slash {

    /**
     * 主键
     */
    private Long id;

    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 交易hash
     */
    private String txHash;

    /**
     * 时间
     */
    private Date time;

    /**
     * 通过（block_number/每个结算周期出块数）向上取整
     */
    private Integer settingEpoch;

    /**
     * 质押交易所在块高
     */
    private Long stakingBlockNum;

    /**
     * 双签惩罚比例
     */
    private BigDecimal slashRate;

    /**
     * 惩罚金分配给举报人比例
     */
    private BigDecimal slashReportRate;

    /**
     * 交易发送者
     */
    private String benefitAddress;

    /**
     * 双签惩罚后剩下的质押金额，因为双签处罚后节点就被置为退出中，所有金额都会移动到待赎回字段中
     */
    private BigDecimal codeRemainRedeemAmount;

    /**
     * 奖励的金额
     */
    private BigDecimal codeRewardValue;

    /**
     * 节点状态:1候选中,2退出中,3已退出,4已锁定
     */
    private Integer codeStatus;

    /**
     * 当前退出中
     */
    private Integer codeStakingReductionEpoch;

    /**
     * 惩罚的金额
     */
    private BigDecimal codeSlashValue;

    /**
     * 解质押需要经过的结算周期数
     */
    private Integer unStakeFreezeDuration;

    /**
     * 解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者
     */
    private Long unStakeEndBlock;

    /**
     * 双签的区块
     */
    private Long blockNum;

    /**
     * 是否退出:1是,2否
     */
    private Integer isQuit;

    /**
     * 是否已处理，1-是，0-否
     */
    private Boolean isHandle;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 举报证据
     */
    private String slashData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getSettingEpoch() {
        return settingEpoch;
    }

    public void setSettingEpoch(Integer settingEpoch) {
        this.settingEpoch = settingEpoch;
    }

    public Long getStakingBlockNum() {
        return stakingBlockNum;
    }

    public void setStakingBlockNum(Long stakingBlockNum) {
        this.stakingBlockNum = stakingBlockNum;
    }

    public BigDecimal getSlashRate() {
        return slashRate;
    }

    public void setSlashRate(BigDecimal slashRate) {
        this.slashRate = slashRate;
    }

    public BigDecimal getSlashReportRate() {
        return slashReportRate;
    }

    public void setSlashReportRate(BigDecimal slashReportRate) {
        this.slashReportRate = slashReportRate;
    }

    public String getBenefitAddress() {
        return benefitAddress;
    }

    public void setBenefitAddress(String benefitAddress) {
        this.benefitAddress = benefitAddress == null ? null : benefitAddress.trim();
    }

    public BigDecimal getCodeRemainRedeemAmount() {
        return codeRemainRedeemAmount;
    }

    public void setCodeRemainRedeemAmount(BigDecimal codeRemainRedeemAmount) {
        this.codeRemainRedeemAmount = codeRemainRedeemAmount;
    }

    public BigDecimal getCodeRewardValue() {
        return codeRewardValue;
    }

    public void setCodeRewardValue(BigDecimal codeRewardValue) {
        this.codeRewardValue = codeRewardValue;
    }

    public Integer getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(Integer codeStatus) {
        this.codeStatus = codeStatus;
    }

    public Integer getCodeStakingReductionEpoch() {
        return codeStakingReductionEpoch;
    }

    public void setCodeStakingReductionEpoch(Integer codeStakingReductionEpoch) {
        this.codeStakingReductionEpoch = codeStakingReductionEpoch;
    }

    public BigDecimal getCodeSlashValue() {
        return codeSlashValue;
    }

    public void setCodeSlashValue(BigDecimal codeSlashValue) {
        this.codeSlashValue = codeSlashValue;
    }

    public Integer getUnStakeFreezeDuration() {
        return unStakeFreezeDuration;
    }

    public void setUnStakeFreezeDuration(Integer unStakeFreezeDuration) {
        this.unStakeFreezeDuration = unStakeFreezeDuration;
    }

    public Long getUnStakeEndBlock() {
        return unStakeEndBlock;
    }

    public void setUnStakeEndBlock(Long unStakeEndBlock) {
        this.unStakeEndBlock = unStakeEndBlock;
    }

    public Long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }

    public Integer getIsQuit() {
        return isQuit;
    }

    public void setIsQuit(Integer isQuit) {
        this.isQuit = isQuit;
    }

    public Boolean getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(Boolean isHandle) {
        this.isHandle = isHandle;
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

    public String getSlashData() {
        return slashData;
    }

    public void setSlashData(String slashData) {
        this.slashData = slashData == null ? null : slashData.trim();
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table slash
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        id("id", "id", "BIGINT", false),
        nodeId("node_id", "nodeId", "VARCHAR", false),
        txHash("tx_hash", "txHash", "VARCHAR", false),
        time("time", "time", "TIMESTAMP", true),
        settingEpoch("setting_epoch", "settingEpoch", "INTEGER", false),
        stakingBlockNum("staking_block_num", "stakingBlockNum", "BIGINT", false),
        slashRate("slash_rate", "slashRate", "DECIMAL", false),
        slashReportRate("slash_report_rate", "slashReportRate", "DECIMAL", false),
        benefitAddress("benefit_address", "benefitAddress", "VARCHAR", false),
        codeRemainRedeemAmount("code_remain_redeem_amount", "codeRemainRedeemAmount", "DECIMAL", false),
        codeRewardValue("code_reward_value", "codeRewardValue", "DECIMAL", false),
        codeStatus("code_status", "codeStatus", "INTEGER", false),
        codeStakingReductionEpoch("code_staking_reduction_epoch", "codeStakingReductionEpoch", "INTEGER", false),
        codeSlashValue("code_slash_value", "codeSlashValue", "DECIMAL", false),
        unStakeFreezeDuration("un_stake_freeze_duration", "unStakeFreezeDuration", "INTEGER", false),
        unStakeEndBlock("un_stake_end_block", "unStakeEndBlock", "BIGINT", false),
        blockNum("block_num", "blockNum", "BIGINT", false),
        isQuit("is_quit", "isQuit", "INTEGER", false),
        isHandle("is_handle", "isHandle", "BIT", false),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        slashData("slash_data", "slashData", "LONGVARCHAR", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
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
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public static Column[] excludes(Column... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table slash
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