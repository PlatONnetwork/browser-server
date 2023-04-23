package com.platon.browser.dao.entity;

import com.platon.browser.utils.ChainVersionUtil;
import com.platon.browser.utils.HexUtil;
import com.platon.contracts.ppos.dto.resp.Node;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class Staking extends StakingKey {
    private Integer stakingTxIndex;

    private BigDecimal stakingHes;

    private BigDecimal stakingLocked;

    private BigDecimal stakingReduction;

    private Integer stakingReductionEpoch;

    private String nodeName;

    private String nodeIcon;

    private String externalId;

    private String externalName;

    private String stakingAddr;

    private String benefitAddr;

    private Double annualizedRate;

    private String programVersion;

    private String bigVersion;

    private String webSite;

    private String details;

    private Date joinTime;

    private Date leaveTime;

    private Integer status;

    private Integer isConsensus;

    private Integer isSettle;

    private Integer isInit;

    private BigDecimal statDelegateHes;

    private BigDecimal statDelegateLocked;

    private BigDecimal statDelegateReleased;

    private BigDecimal blockRewardValue;

    private BigDecimal predictStakingReward;

    private BigDecimal stakingRewardValue;

    private BigDecimal feeRewardValue;

    private Long curConsBlockQty;

    private Long preConsBlockQty;

    private Date createTime;

    private Date updateTime;

    private Integer rewardPer;

    private Integer nextRewardPer;

    private Integer nextRewardPerModEpoch;

    private BigDecimal haveDeleReward;

    private Double preDeleAnnualizedRate;

    private Double deleAnnualizedRate;

    private BigDecimal totalDeleReward;

    private Integer exceptionStatus;

    private Integer unStakeFreezeDuration;

    private Long unStakeEndBlock;

    private Integer zeroProduceFreezeDuration;

    private Integer zeroProduceFreezeEpoch;

    private Integer lowRateSlashCount;

    private String annualizedRateInfo;

    private BigDecimal slashAmount;

    public Integer getStakingTxIndex() {
        return stakingTxIndex;
    }

    public void setStakingTxIndex(Integer stakingTxIndex) {
        this.stakingTxIndex = stakingTxIndex;
    }

    public BigDecimal getStakingHes() {
        return stakingHes;
    }

    public void setStakingHes(BigDecimal stakingHes) {
        this.stakingHes = stakingHes;
    }

    public BigDecimal getStakingLocked() {
        return stakingLocked;
    }

    public void setStakingLocked(BigDecimal stakingLocked) {
        this.stakingLocked = stakingLocked;
    }

    public BigDecimal getStakingReduction() {
        return stakingReduction;
    }

    public void setStakingReduction(BigDecimal stakingReduction) {
        this.stakingReduction = stakingReduction;
    }

    public Integer getStakingReductionEpoch() {
        return stakingReductionEpoch;
    }

    public void setStakingReductionEpoch(Integer stakingReductionEpoch) {
        this.stakingReductionEpoch = stakingReductionEpoch;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public String getNodeIcon() {
        return nodeIcon;
    }

    public void setNodeIcon(String nodeIcon) {
        this.nodeIcon = nodeIcon == null ? null : nodeIcon.trim();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId == null ? null : externalId.trim();
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName == null ? null : externalName.trim();
    }

    public String getStakingAddr() {
        return stakingAddr;
    }

    public void setStakingAddr(String stakingAddr) {
        this.stakingAddr = stakingAddr == null ? null : stakingAddr.trim();
    }

    public String getBenefitAddr() {
        return benefitAddr;
    }

    public void setBenefitAddr(String benefitAddr) {
        this.benefitAddr = benefitAddr == null ? null : benefitAddr.trim();
    }

    public Double getAnnualizedRate() {
        return annualizedRate;
    }

    public void setAnnualizedRate(Double annualizedRate) {
        this.annualizedRate = annualizedRate;
    }

    public String getProgramVersion() {
        return programVersion;
    }

    public void setProgramVersion(String programVersion) {
        this.programVersion = programVersion == null ? null : programVersion.trim();
    }

    public String getBigVersion() {
        return bigVersion;
    }

    public void setBigVersion(String bigVersion) {
        this.bigVersion = bigVersion == null ? null : bigVersion.trim();
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

    public Integer getIsConsensus() {
        return isConsensus;
    }

    public void setIsConsensus(Integer isConsensus) {
        this.isConsensus = isConsensus;
    }

    public Integer getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(Integer isSettle) {
        this.isSettle = isSettle;
    }

    public Integer getIsInit() {
        return isInit;
    }

    public void setIsInit(Integer isInit) {
        this.isInit = isInit;
    }

    public BigDecimal getStatDelegateHes() {
        return statDelegateHes;
    }

    public void setStatDelegateHes(BigDecimal statDelegateHes) {
        this.statDelegateHes = statDelegateHes;
    }

    public BigDecimal getStatDelegateLocked() {
        return statDelegateLocked;
    }

    public void setStatDelegateLocked(BigDecimal statDelegateLocked) {
        this.statDelegateLocked = statDelegateLocked;
    }

    public BigDecimal getStatDelegateReleased() {
        return statDelegateReleased;
    }

    public void setStatDelegateReleased(BigDecimal statDelegateReleased) {
        this.statDelegateReleased = statDelegateReleased;
    }

    public BigDecimal getBlockRewardValue() {
        return blockRewardValue;
    }

    public void setBlockRewardValue(BigDecimal blockRewardValue) {
        this.blockRewardValue = blockRewardValue;
    }

    public BigDecimal getPredictStakingReward() {
        return predictStakingReward;
    }

    public void setPredictStakingReward(BigDecimal predictStakingReward) {
        this.predictStakingReward = predictStakingReward;
    }

    public BigDecimal getStakingRewardValue() {
        return stakingRewardValue;
    }

    public void setStakingRewardValue(BigDecimal stakingRewardValue) {
        this.stakingRewardValue = stakingRewardValue;
    }

    public BigDecimal getFeeRewardValue() {
        return feeRewardValue;
    }

    public void setFeeRewardValue(BigDecimal feeRewardValue) {
        this.feeRewardValue = feeRewardValue;
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

    public Integer getRewardPer() {
        return rewardPer;
    }

    public void setRewardPer(Integer rewardPer) {
        this.rewardPer = rewardPer;
    }

    public Integer getNextRewardPer() {
        return nextRewardPer;
    }

    public void setNextRewardPer(Integer nextRewardPer) {
        this.nextRewardPer = nextRewardPer;
    }

    public Integer getNextRewardPerModEpoch() {
        return nextRewardPerModEpoch;
    }

    public void setNextRewardPerModEpoch(Integer nextRewardPerModEpoch) {
        this.nextRewardPerModEpoch = nextRewardPerModEpoch;
    }

    public BigDecimal getHaveDeleReward() {
        return haveDeleReward;
    }

    public void setHaveDeleReward(BigDecimal haveDeleReward) {
        this.haveDeleReward = haveDeleReward;
    }

    public Double getPreDeleAnnualizedRate() {
        return preDeleAnnualizedRate;
    }

    public void setPreDeleAnnualizedRate(Double preDeleAnnualizedRate) {
        this.preDeleAnnualizedRate = preDeleAnnualizedRate;
    }

    public Double getDeleAnnualizedRate() {
        return deleAnnualizedRate;
    }

    public void setDeleAnnualizedRate(Double deleAnnualizedRate) {
        this.deleAnnualizedRate = deleAnnualizedRate;
    }

    public BigDecimal getTotalDeleReward() {
        return totalDeleReward;
    }

    public void setTotalDeleReward(BigDecimal totalDeleReward) {
        this.totalDeleReward = totalDeleReward;
    }

    public Integer getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(Integer exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
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

    public Integer getZeroProduceFreezeDuration() {
        return zeroProduceFreezeDuration;
    }

    public void setZeroProduceFreezeDuration(Integer zeroProduceFreezeDuration) {
        this.zeroProduceFreezeDuration = zeroProduceFreezeDuration;
    }

    public Integer getZeroProduceFreezeEpoch() {
        return zeroProduceFreezeEpoch;
    }

    public void setZeroProduceFreezeEpoch(Integer zeroProduceFreezeEpoch) {
        this.zeroProduceFreezeEpoch = zeroProduceFreezeEpoch;
    }

    public Integer getLowRateSlashCount() {
        return lowRateSlashCount;
    }

    public void setLowRateSlashCount(Integer lowRateSlashCount) {
        this.lowRateSlashCount = lowRateSlashCount;
    }

    public String getAnnualizedRateInfo() {
        return annualizedRateInfo;
    }

    public void setAnnualizedRateInfo(String annualizedRateInfo) {
        this.annualizedRateInfo = annualizedRateInfo == null ? null : annualizedRateInfo.trim();
    }

    public BigDecimal getSlashAmount() {
        return slashAmount;
    }

    public void setSlashAmount(BigDecimal slashAmount) {
        this.slashAmount = slashAmount;
    }


    /**
     * 使用节点信息更新质押信息
     * @param verifier
     */
    public void updateWithVerifier(com.platon.contracts.ppos.dto.resp.Node verifier){
        // 质押区块高度
        if(verifier.getStakingBlockNum()!=null) this.setStakingBlockNum(verifier.getStakingBlockNum().longValue());
        // 质押节点地址
        this.setNodeId(HexUtil.prefix(verifier.getNodeId()));
        // 发起质押交易的索引
        if(verifier.getStakingTxIndex()!=null) this.setStakingTxIndex(verifier.getStakingTxIndex().intValue());
        // 发起质押的账户地址
        this.setStakingAddr(verifier.getStakingAddress());
        // 第三方社交软件关联id
        this.setExternalId(verifier.getExternalId());
        // 收益地址
        this.setBenefitAddr(verifier.getBenifitAddress());
//        // 节点状态 1：候选中 2：退出中 3：已退出
        if(verifier.getStatus()!=null) this.setStatus(verifier.getStatus().intValue());
//        // 节点名称(质押节点名称)
        this.setNodeName(StringUtils.isBlank(verifier.getNodeName())?this.getNodeName():verifier.getNodeName());
        // 节点的第三方主页
        this.setWebSite(verifier.getWebsite());
        this.setDetails(verifier.getDetails());

        // 程序版本号
        BigInteger programVersion=verifier.getProgramVersion();
        BigInteger bigVersion = ChainVersionUtil.toBigVersion(programVersion);
        this.setProgramVersion(programVersion.toString());
        this.setBigVersion(bigVersion.toString());
    }

    /**
     * 使用节点信息更新质押信息
     * @param candidate
     */
    public void updateWithCandidate(Node candidate){
        // 设置节点名称
        String nodeName = candidate.getNodeName();
        if(StringUtils.isNotBlank(nodeName)) setNodeName(nodeName);
        // 设置程序版本号
        String programVersion=candidate.getProgramVersion().toString();
        if(StringUtils.isNotBlank(programVersion)){
            setProgramVersion(programVersion);
            BigInteger bigVersion = ChainVersionUtil.toBigVersion(candidate.getProgramVersion());
            setBigVersion(bigVersion.toString());
        }
        // 设置外部ID
        String externalId = candidate.getExternalId();
        if(StringUtils.isNotBlank(externalId)) setExternalId(externalId);
        // 设置收益地址
        String benefitAddr = candidate.getBenifitAddress();
        if(StringUtils.isNotBlank(benefitAddr)) setBenefitAddr(benefitAddr);
        // 设置详情
        String details = candidate.getDetails();
        if(StringUtils.isNotBlank(details)) setDetails(details);
        // 设置官网
        String website = candidate.getWebsite();
        if(StringUtils.isNotBlank(website)) setWebSite(website);
        // 设置质押金额
        if(candidate.getShares()!=null&&candidate.getShares().compareTo(BigInteger.ZERO)>0){
            setStakingLocked(new BigDecimal(candidate.getShares()));
        }
    }

    /**
     * 质押状态类型枚举类：
     *  1.候选中
     *  2.退出中
     *  3.已退出
     */
    public enum StatusEnum{
        CANDIDATE(1, "候选中"),
        EXITING(2, "退出中"),
        EXITED(3, "已退出"),
        LOCKED(4, "已锁定"),
        ;
        private int code;
        private String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, Staking.StatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(Staking.StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static Staking.StatusEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(Staking.StatusEnum en){return ENUMS.containsValue(en);}
    }
    /**
     * 质押节点——是否共识周期验证人类型/是否结算周期验证人类型枚举类：
     *  1.是
     *  2.否
     */
    public enum YesNoEnum{
        YES(1, "是"),
        NO(2, "否")
        ;
        private int code;
        private String desc;
        YesNoEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, Staking.YesNoEnum> ENUMS = new HashMap<>();
        static {Arrays.asList(Staking.YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static Staking.YesNoEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(Staking.YesNoEnum en){return ENUMS.containsValue(en);}
    }

    public enum ExceptionStatusEnum {
        NORMAL(1, "正常"),
        LOW_RATE(2, "低出块异常"),
        MULTI_SIGN(3, "双签异常"),
        LOW_RATE_SLASHED(4, "因低出块率被惩罚"),
        MULTI_SIGN_SLASHED(5, "因双签被处罚");

        private int code;
        private String desc;

        ExceptionStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StakingKey that = (StakingKey) o;
        return Objects.equals(this.getStakingBlockNum(), that.getStakingBlockNum()) &&
                Objects.equals(this.getNodeId(), that.getNodeId());
    }

    @Override
    public int hashCode () {
        return Objects.hash(this.getStakingBlockNum(), this.getNodeId());
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table staking
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        nodeId("node_id", "nodeId", "VARCHAR", false),
        stakingBlockNum("staking_block_num", "stakingBlockNum", "BIGINT", false),
        stakingTxIndex("staking_tx_index", "stakingTxIndex", "INTEGER", false),
        stakingHes("staking_hes", "stakingHes", "DECIMAL", false),
        stakingLocked("staking_locked", "stakingLocked", "DECIMAL", false),
        stakingReduction("staking_reduction", "stakingReduction", "DECIMAL", false),
        stakingReductionEpoch("staking_reduction_epoch", "stakingReductionEpoch", "INTEGER", false),
        nodeName("node_name", "nodeName", "VARCHAR", false),
        nodeIcon("node_icon", "nodeIcon", "VARCHAR", false),
        externalId("external_id", "externalId", "VARCHAR", false),
        externalName("external_name", "externalName", "VARCHAR", false),
        stakingAddr("staking_addr", "stakingAddr", "VARCHAR", false),
        benefitAddr("benefit_addr", "benefitAddr", "VARCHAR", false),
        annualizedRate("annualized_rate", "annualizedRate", "DOUBLE", false),
        programVersion("program_version", "programVersion", "VARCHAR", false),
        bigVersion("big_version", "bigVersion", "VARCHAR", false),
        webSite("web_site", "webSite", "VARCHAR", false),
        details("details", "details", "VARCHAR", false),
        joinTime("join_time", "joinTime", "TIMESTAMP", false),
        leaveTime("leave_time", "leaveTime", "TIMESTAMP", false),
        status("status", "status", "INTEGER", true),
        isConsensus("is_consensus", "isConsensus", "INTEGER", false),
        isSettle("is_settle", "isSettle", "INTEGER", false),
        isInit("is_init", "isInit", "INTEGER", false),
        statDelegateHes("stat_delegate_hes", "statDelegateHes", "DECIMAL", false),
        statDelegateLocked("stat_delegate_locked", "statDelegateLocked", "DECIMAL", false),
        statDelegateReleased("stat_delegate_released", "statDelegateReleased", "DECIMAL", false),
        blockRewardValue("block_reward_value", "blockRewardValue", "DECIMAL", false),
        predictStakingReward("predict_staking_reward", "predictStakingReward", "DECIMAL", false),
        stakingRewardValue("staking_reward_value", "stakingRewardValue", "DECIMAL", false),
        feeRewardValue("fee_reward_value", "feeRewardValue", "DECIMAL", false),
        curConsBlockQty("cur_cons_block_qty", "curConsBlockQty", "BIGINT", false),
        preConsBlockQty("pre_cons_block_qty", "preConsBlockQty", "BIGINT", false),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        rewardPer("reward_per", "rewardPer", "INTEGER", false),
        nextRewardPer("next_reward_per", "nextRewardPer", "INTEGER", false),
        nextRewardPerModEpoch("next_reward_per_mod_epoch", "nextRewardPerModEpoch", "INTEGER", false),
        haveDeleReward("have_dele_reward", "haveDeleReward", "DECIMAL", false),
        preDeleAnnualizedRate("pre_dele_annualized_rate", "preDeleAnnualizedRate", "DOUBLE", false),
        deleAnnualizedRate("dele_annualized_rate", "deleAnnualizedRate", "DOUBLE", false),
        totalDeleReward("total_dele_reward", "totalDeleReward", "DECIMAL", false),
        exceptionStatus("exception_status", "exceptionStatus", "INTEGER", false),
        unStakeFreezeDuration("un_stake_freeze_duration", "unStakeFreezeDuration", "INTEGER", false),
        unStakeEndBlock("un_stake_end_block", "unStakeEndBlock", "BIGINT", false),
        zeroProduceFreezeDuration("zero_produce_freeze_duration", "zeroProduceFreezeDuration", "INTEGER", false),
        zeroProduceFreezeEpoch("zero_produce_freeze_epoch", "zeroProduceFreezeEpoch", "INTEGER", false),
        lowRateSlashCount("low_rate_slash_count", "lowRateSlashCount", "INTEGER", false),
        annualizedRateInfo("annualized_rate_info", "annualizedRateInfo", "LONGVARCHAR", false);

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
