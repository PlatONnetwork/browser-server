package com.platon.browser.bean;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/10 12:05
 * @Description: 参与者
 */
@Data
public class ProposalParticipantStat {
    private Long accuVerifierAccount; // 累积可投票人数
    private Long yeas; // 赞成票数
    private Long nays; // 反对票数
    private Long abstentions; // 弃权票数
	public Long getAccuVerifierAccount() {
		return accuVerifierAccount;
	}
	public void setAccuVerifierAccount(Long accuVerifierAccount) {
		this.accuVerifierAccount = accuVerifierAccount;
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
}
