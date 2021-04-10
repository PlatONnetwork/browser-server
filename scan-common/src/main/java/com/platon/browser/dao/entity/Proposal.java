package com.platon.browser.dao.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Proposal {
    private String hash;

    private Integer type;

    private String nodeId;

    private String nodeName;

    private String url;

    private String newVersion;

    private Long endVotingBlock;

    private Long activeBlock;

    private Date timestamp;

    private Long yeas;

    private Long nays;

    private Long abstentions;

    private Long accuVerifiers;

    private Integer status;

    private String pipNum;

    private String pipId;

    private String topic;

    private String description;

    private String canceledPipId;

    private String canceledTopic;

    private Long blockNumber;

    private Date createTime;

    private Date updateTime;

    private Integer completionFlag;

    private String module;

    private String name;

    private String staleValue;

    private String newValue;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion == null ? null : newVersion.trim();
    }

    public Long getEndVotingBlock() {
        return endVotingBlock;
    }

    public void setEndVotingBlock(Long endVotingBlock) {
        this.endVotingBlock = endVotingBlock;
    }

    public Long getActiveBlock() {
        return activeBlock;
    }

    public void setActiveBlock(Long activeBlock) {
        this.activeBlock = activeBlock;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getYeas() {
        return yeas;
    }

    public void setYeas(Long yeas) {
        this.yeas = yeas;
    }

    public Long getNays() {
        return nays;
    }

    public void setNays(Long nays) {
        this.nays = nays;
    }

    public Long getAbstentions() {
        return abstentions;
    }

    public void setAbstentions(Long abstentions) {
        this.abstentions = abstentions;
    }

    public Long getAccuVerifiers() {
        return accuVerifiers;
    }

    public void setAccuVerifiers(Long accuVerifiers) {
        this.accuVerifiers = accuVerifiers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPipNum() {
        return pipNum;
    }

    public void setPipNum(String pipNum) {
        this.pipNum = pipNum == null ? null : pipNum.trim();
    }

    public String getPipId() {
        return pipId;
    }

    public void setPipId(String pipId) {
        this.pipId = pipId == null ? null : pipId.trim();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCanceledPipId() {
        return canceledPipId;
    }

    public void setCanceledPipId(String canceledPipId) {
        this.canceledPipId = canceledPipId == null ? null : canceledPipId.trim();
    }

    public String getCanceledTopic() {
        return canceledTopic;
    }

    public void setCanceledTopic(String canceledTopic) {
        this.canceledTopic = canceledTopic == null ? null : canceledTopic.trim();
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
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

    public Integer getCompletionFlag() {
        return completionFlag;
    }

    public void setCompletionFlag(Integer completionFlag) {
        this.completionFlag = completionFlag;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? null : module.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStaleValue() {
        return staleValue;
    }

    public void setStaleValue(String staleValue) {
        this.staleValue = staleValue == null ? null : staleValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table proposal
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    public enum Column {
        hash("hash", "hash", "VARCHAR", false),
        type("type", "type", "INTEGER", true),
        nodeId("node_id", "nodeId", "VARCHAR", false),
        nodeName("node_name", "nodeName", "VARCHAR", false),
        url("url", "url", "VARCHAR", false),
        newVersion("new_version", "newVersion", "VARCHAR", false),
        endVotingBlock("end_voting_block", "endVotingBlock", "BIGINT", false),
        activeBlock("active_block", "activeBlock", "BIGINT", false),
        timestamp("timestamp", "timestamp", "TIMESTAMP", true),
        yeas("yeas", "yeas", "BIGINT", false),
        nays("nays", "nays", "BIGINT", false),
        abstentions("abstentions", "abstentions", "BIGINT", false),
        accuVerifiers("accu_verifiers", "accuVerifiers", "BIGINT", false),
        status("status", "status", "INTEGER", true),
        pipNum("pip_num", "pipNum", "VARCHAR", false),
        pipId("pip_id", "pipId", "VARCHAR", false),
        topic("topic", "topic", "VARCHAR", false),
        description("description", "description", "VARCHAR", false),
        canceledPipId("canceled_pip_id", "canceledPipId", "VARCHAR", false),
        canceledTopic("canceled_topic", "canceledTopic", "VARCHAR", false),
        blockNumber("block_number", "blockNumber", "BIGINT", false),
        createTime("create_time", "createTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        completionFlag("completion_flag", "completionFlag", "INTEGER", false),
        module("module", "module", "VARCHAR", true),
        name("name", "name", "VARCHAR", true),
        staleValue("stale_value", "staleValue", "VARCHAR", false),
        newValue("new_value", "newValue", "VARCHAR", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
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
         * This method corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table proposal
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
         * This method corresponds to the database table proposal
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