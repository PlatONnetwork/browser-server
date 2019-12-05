package com.platon.browser.config.govern;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @description: 可修改的惩罚参数配置
 * @author: chendongming@juzix.net
 * @create: 2019-11-25 18:31:37
 **/
@Builder
public class Slashing {
    private BigDecimal slashFractionDuplicateSign;
    private BigDecimal duplicateSignReportReward;
    private BigDecimal maxEvidenceAge;
    private BigDecimal slashBlocksReward;
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
}
