package com.platon.browser.bean.govern;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @description: 可修改的惩罚参数配置
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-25 18:31:37
 **/
@Builder
public class Slashing {
    private BigDecimal slashFractionDuplicateSign;
    private BigDecimal duplicateSignReportReward;
    private BigDecimal maxEvidenceAge;
    private BigDecimal slashBlocksReward;
	// 零出块次数阈值，在指定时间范围内达到该次数则处罚
	private Integer zeroProduceNumberThreshold;
	// 说明：用N代表下面字段所设置的值，阐述如下：
	// 上一次零出块后，在往后的N个共识周期内如若再出现零出块，则在这N个共识周期完成时记录零出块信息
	private Integer zeroProduceCumulativeTime;
	// 零出块锁定结算周期数
	private Integer zeroProduceFreezeDuration;
	public BigDecimal getSlashFractionDuplicateSign() {
		return slashFractionDuplicateSign;
	}
	public void setSlashFractionDuplicateSign(BigDecimal slashFractionDuplicateSign) {
		this.slashFractionDuplicateSign = slashFractionDuplicateSign;
	}
	public BigDecimal getDuplicateSignReportReward() {
		return duplicateSignReportReward;
	}
	public void setDuplicateSignReportReward(BigDecimal duplicateSignReportReward) {
		this.duplicateSignReportReward = duplicateSignReportReward;
	}
	public BigDecimal getMaxEvidenceAge() {
		return maxEvidenceAge;
	}
	public void setMaxEvidenceAge(BigDecimal maxEvidenceAge) {
		this.maxEvidenceAge = maxEvidenceAge;
	}
	public BigDecimal getSlashBlocksReward() {
		return slashBlocksReward;
	}
	public void setSlashBlocksReward(BigDecimal slashBlocksReward) {
		this.slashBlocksReward = slashBlocksReward;
	}

	public Integer getZeroProduceNumberThreshold() {
		return zeroProduceNumberThreshold;
	}

	public void setZeroProduceNumberThreshold(Integer zeroProduceNumberThreshold) {
		this.zeroProduceNumberThreshold = zeroProduceNumberThreshold;
	}

	public Integer getZeroProduceCumulativeTime() {
		return zeroProduceCumulativeTime;
	}

	public void setZeroProduceCumulativeTime(Integer zeroProduceCumulativeTime) {
		this.zeroProduceCumulativeTime = zeroProduceCumulativeTime;
	}

	public Integer getZeroProduceFreezeDuration() {
		return zeroProduceFreezeDuration;
	}

	public void setZeroProduceFreezeDuration(Integer zeroProduceFreezeDuration) {
		this.zeroProduceFreezeDuration = zeroProduceFreezeDuration;
	}
}
