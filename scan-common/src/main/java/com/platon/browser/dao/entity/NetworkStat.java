package com.platon.browser.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class NetworkStat {

    private Integer id;

    private Long curNumber;

    private String curBlockHash;

    private String nodeId;

    private String nodeName;

    /**
     * 交易总数
     */
    private Integer txQty;

    private Integer curTps;

    private Integer maxTps;

    /**
     * 发行量
     */
    private BigDecimal issueValue;

    /**
     * 流通量
     */
    private BigDecimal turnValue;

    private BigDecimal availableStaking;

    /**
     * 实时质押委托总数
     */
    private BigDecimal stakingDelegationValue;

    /**
     * 实时质押总数
     */
    private BigDecimal stakingValue;

    private Integer doingProposalQty;

    /**
     * 提案总数
     */
    private Integer proposalQty;

    /**
     * 地址数
     */
    private Integer addressQty;

    private BigDecimal blockReward;

    private BigDecimal stakingReward;

    private BigDecimal settleStakingReward;

    private Long addIssueBegin;

    private Long addIssueEnd;

    private Long nextSettle;

    private Long nodeOptSeq;

    private Date createTime;

    private Date updateTime;

    private Long avgPackTime;

    /**
     * 整个链的erc721交易数
     */
    private Integer erc721TxQty;

    /**
     * 整个链的erc20交易数
     */
    private Integer erc20TxQty;

    /**
     * 年份
     */
    private Integer yearNum;

    /**
     * 增发比例
     */
    private String issueRates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCurNumber() {
        return curNumber;
    }

    public void setCurNumber(Long curNumber) {
        this.curNumber = curNumber;
    }

    public String getCurBlockHash() {
        return curBlockHash;
    }

    public void setCurBlockHash(String curBlockHash) {
        this.curBlockHash = curBlockHash == null ? null : curBlockHash.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public Integer getTxQty() {
        return txQty;
    }

    public void setTxQty(Integer txQty) {
        this.txQty = txQty;
    }

    public Integer getCurTps() {
        return curTps;
    }

    public void setCurTps(Integer curTps) {
        this.curTps = curTps;
    }

    public Integer getMaxTps() {
        return maxTps;
    }

    public void setMaxTps(Integer maxTps) {
        this.maxTps = maxTps;
    }

    public BigDecimal getIssueValue() {
        return issueValue;
    }

    public void setIssueValue(BigDecimal issueValue) {
        this.issueValue = issueValue;
    }

    public BigDecimal getTurnValue() {
        return turnValue;
    }

    public void setTurnValue(BigDecimal turnValue) {
        this.turnValue = turnValue;
    }

    public BigDecimal getAvailableStaking() {
        return availableStaking;
    }

    public void setAvailableStaking(BigDecimal availableStaking) {
        this.availableStaking = availableStaking;
    }

    public BigDecimal getStakingDelegationValue() {
        return stakingDelegationValue;
    }

    public void setStakingDelegationValue(BigDecimal stakingDelegationValue) {
        this.stakingDelegationValue = stakingDelegationValue;
    }

    public BigDecimal getStakingValue() {
        return stakingValue;
    }

    public void setStakingValue(BigDecimal stakingValue) {
        this.stakingValue = stakingValue;
    }

    public Integer getDoingProposalQty() {
        return doingProposalQty;
    }

    public void setDoingProposalQty(Integer doingProposalQty) {
        this.doingProposalQty = doingProposalQty;
    }

    public Integer getProposalQty() {
        return proposalQty;
    }

    public void setProposalQty(Integer proposalQty) {
        this.proposalQty = proposalQty;
    }

    public Integer getAddressQty() {
        return addressQty;
    }

    public void setAddressQty(Integer addressQty) {
        this.addressQty = addressQty;
    }

    public BigDecimal getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(BigDecimal blockReward) {
        this.blockReward = blockReward;
    }

    public BigDecimal getStakingReward() {
        return stakingReward;
    }

    public void setStakingReward(BigDecimal stakingReward) {
        this.stakingReward = stakingReward;
    }

    public BigDecimal getSettleStakingReward() {
        return settleStakingReward;
    }

    public void setSettleStakingReward(BigDecimal settleStakingReward) {
        this.settleStakingReward = settleStakingReward;
    }

    public Long getAddIssueBegin() {
        return addIssueBegin;
    }

    public void setAddIssueBegin(Long addIssueBegin) {
        this.addIssueBegin = addIssueBegin;
    }

    public Long getAddIssueEnd() {
        return addIssueEnd;
    }

    public void setAddIssueEnd(Long addIssueEnd) {
        this.addIssueEnd = addIssueEnd;
    }

    public Long getNextSettle() {
        return nextSettle;
    }

    public void setNextSettle(Long nextSettle) {
        this.nextSettle = nextSettle;
    }

    public Long getNodeOptSeq() {
        return nodeOptSeq;
    }

    public void setNodeOptSeq(Long nodeOptSeq) {
        this.nodeOptSeq = nodeOptSeq;
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

    public Long getAvgPackTime() {
        return avgPackTime;
    }

    public void setAvgPackTime(Long avgPackTime) {
        this.avgPackTime = avgPackTime;
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

    public Integer getYearNum() {
        return yearNum;
    }

    public void setYearNum(Integer yearNum) {
        this.yearNum = yearNum;
    }

    public String getIssueRates() {
        return issueRates;
    }

    public void setIssueRates(String issueRates) {
        this.issueRates = issueRates == null ? null : issueRates.trim();
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table network_stat
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        id("id", "id", "INTEGER", false),
        curNumber("cur_number", "curNumber", "BIGINT", false),
        curBlockHash("cur_block_hash", "curBlockHash", "VARCHAR", false),
        nodeId("node_id", "nodeId", "VARCHAR", false),
        nodeName("node_name", "nodeName", "VARCHAR", false),
        txQty("tx_qty", "txQty", "INTEGER", false),
        curTps("cur_tps", "curTps", "INTEGER", false),
        maxTps("max_tps", "maxTps", "INTEGER", false),
        issueValue("issue_value", "issueValue", "DECIMAL", false),
        turnValue("turn_value", "turnValue", "DECIMAL", false),
        availableStaking("available_staking", "availableStaking", "DECIMAL", false),
        stakingDelegationValue("staking_delegation_value", "stakingDelegationValue", "DECIMAL", false),
        stakingValue("staking_value", "stakingValue", "DECIMAL", false),
        doingProposalQty("doing_proposal_qty", "doingProposalQty", "INTEGER", false),
        proposalQty("proposal_qty", "proposalQty", "INTEGER", false),
        addressQty("address_qty", "addressQty", "INTEGER", false),
        blockReward("block_reward", "blockReward", "DECIMAL", false),
        stakingReward("staking_reward", "stakingReward", "DECIMAL", false),
        settleStakingReward("settle_staking_reward", "settleStakingReward", "DECIMAL", false),
        addIssueBegin("add_issue_begin", "addIssueBegin", "BIGINT", false),
        addIssueEnd("add_issue_end", "addIssueEnd", "BIGINT", false),
        nextSettle("next_settle", "nextSettle", "BIGINT", false),
        nodeOptSeq("node_opt_seq", "nodeOptSeq", "BIGINT", false),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        avgPackTime("avg_pack_time", "avgPackTime", "BIGINT", false),
        erc721TxQty("erc721_tx_qty", "erc721TxQty", "INTEGER", false),
        erc20TxQty("erc20_tx_qty", "erc20TxQty", "INTEGER", false),
        yearNum("year_num", "yearNum", "INTEGER", false),
        issueRates("issue_rates", "issueRates", "LONGVARCHAR", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
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
         * This method corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table network_stat
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
         * This method corresponds to the database table network_stat
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