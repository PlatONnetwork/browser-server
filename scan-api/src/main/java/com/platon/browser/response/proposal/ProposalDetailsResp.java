package com.platon.browser.response.proposal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomRateSerializer;
import com.platon.browser.config.json.CustomVersionSerializer;

/**
 *提案详情返回对象
 *  @file ProposalDetailsResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class ProposalDetailsResp {
    private String pipNum;
    private String proposalHash;        //提案内部标识
	private String topic;  //提案标题
	private String description;  //提案描述
	private String url;                 //github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md PIP编号   eip-100
	private Integer type;                //提案类型   1：文本提案； 2：升级提案；  3参数提案。
	private Integer status;              //状态  1：投票中  2：通过  3：失败   4：预升级  5：升级完成    已通过=2 或4 或 5
	private String curBlock;       //当前块高
	private String endVotingBlock;      //投票结算的快高
	private Long timestamp;    //提案时间
	private String activeBlock;         //（如果投票通过）生效块高
	private String newVersion;          //升级提案升级的版本
	private String paramName;           //参数名
    private String currentValue;        //参数当前值
    private String newValue;            //参数的新值
    private String nodeName;            //发起提案的节点名称
    private String nodeId;              //发起提案的节点id
    private Integer yeas;                //同意的人
    private Integer nays;              //反对的人
    private Integer abstentions;         //弃权的人
    private String accuVerifiers;        //总人数
    private Long activeBlockTime;      //生效块高预计时间  (activeBlock-curBlock)*period
    private Long endVotingBlockTime;    //投票块高的时间   (endVotingBlock-curBlock)*period
    private String supportRateThreshold;   //通过条件率
    private String yesRateThreshold;   //通过率
    private String opposeRateThreshold;   //反对率
    private String abstainRateThreshold;   //弃权率
    private Long inBlock;   //所在块高
    private String canceledPipId;//取消提案對應的提案id
    private String canceledTopic;//取消提案對應的提案標題
    private String participationRate;//通过条件率
	public String getPipNum() {
		return pipNum;
	}
	public void setPipNum(String pipNum) {
		this.pipNum = pipNum;
	}
	public String getProposalHash() {
		return proposalHash;
	}
	public void setProposalHash(String proposalHash) {
		this.proposalHash = proposalHash;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCurBlock() {
		return curBlock;
	}
	public void setCurBlock(String curBlock) {
		this.curBlock = curBlock;
	}
	public String getEndVotingBlock() {
		return endVotingBlock;
	}
	public void setEndVotingBlock(String endVotingBlock) {
		this.endVotingBlock = endVotingBlock;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getActiveBlock() {
		return activeBlock;
	}
	public void setActiveBlock(String activeBlock) {
		this.activeBlock = activeBlock;
	}
	@JsonSerialize(using = CustomVersionSerializer.class)
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public Integer getYeas() {
		return yeas;
	}
	public void setYeas(Integer yeas) {
		this.yeas = yeas;
	}
	public Integer getNays() {
		return nays;
	}
	public void setNays(Integer nays) {
		this.nays = nays;
	}
	public Integer getAbstentions() {
		return abstentions;
	}
	public void setAbstentions(Integer abstentions) {
		this.abstentions = abstentions;
	}
	public String getAccuVerifiers() {
		return accuVerifiers;
	}
	public void setAccuVerifiers(String accuVerifiers) {
		this.accuVerifiers = accuVerifiers;
	}
	public Long getActiveBlockTime() {
		return activeBlockTime;
	}
	public void setActiveBlockTime(Long activeBlockTime) {
		this.activeBlockTime = activeBlockTime;
	}
	public Long getEndVotingBlockTime() {
		return endVotingBlockTime;
	}
	public void setEndVotingBlockTime(Long endVotingBlockTime) {
		this.endVotingBlockTime = endVotingBlockTime;
	}
	@JsonSerialize(using = CustomRateSerializer.class)
	public String getSupportRateThreshold() {
		return supportRateThreshold;
	}
	public void setSupportRateThreshold(String supportRateThreshold) {
		this.supportRateThreshold = supportRateThreshold;
	}
	public String getOpposeRateThreshold() {
		return opposeRateThreshold;
	}
	public void setOpposeRateThreshold(String opposeRateThreshold) {
		this.opposeRateThreshold = opposeRateThreshold;
	}
	public String getAbstainRateThreshold() {
		return abstainRateThreshold;
	}
	public void setAbstainRateThreshold(String abstainRateThreshold) {
		this.abstainRateThreshold = abstainRateThreshold;
	}
	public Long getInBlock() {
		return inBlock;
	}
	public void setInBlock(Long inBlock) {
		this.inBlock = inBlock;
	}
	public String getCanceledPipId() {
		return canceledPipId;
	}
	public void setCanceledPipId(String canceledPipId) {
		this.canceledPipId = canceledPipId;
	}
	public String getCanceledTopic() {
		return canceledTopic;
	}
	public void setCanceledTopic(String canceledTopic) {
		this.canceledTopic = canceledTopic;
	}
	@JsonSerialize(using = CustomRateSerializer.class)
	public String getParticipationRate() {
		return participationRate;
	}
	public void setParticipationRate(String participationRate) {
		this.participationRate = participationRate;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getYesRateThreshold() {
		return yesRateThreshold;
	}
	public void setYesRateThreshold(String yesRateThreshold) {
		this.yesRateThreshold = yesRateThreshold;
	}
    
}
