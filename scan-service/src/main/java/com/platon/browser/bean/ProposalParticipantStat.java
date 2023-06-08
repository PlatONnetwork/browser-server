package com.platon.browser.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/10 12:05
 * @Description: 参与者
 */
public class ProposalParticipantStat {
    private Long voterCount; // 累积可投票人数

    private Long supportCount; // 赞成票数

    private Long opposeCount; // 反对票数

    private Long abstainCount; // 弃权票数

    public Long getVoterCount() {
        return voterCount;
    }
    @JSONField(name = "accuVerifierAccount")
    public void setVoterCount(Long voterCount) {
        this.voterCount = voterCount;
    }
    public Long getSupportCount() {
        return supportCount;
    }

    @JSONField(name = "yeas")
    public void setSupportCount(Long supportCount) {
        this.supportCount = supportCount;
    }
    public Long getOpposeCount() {
        return opposeCount;
    }

    @JSONField(name = "nays")
    public void setOpposeCount(Long opposeCount) {
        this.opposeCount = opposeCount;
    }
    public Long getAbstainCount() {
        return abstainCount;
    }
    @JSONField(name = "abstentions")
    public void setAbstainCount(Long abstainCount) {
        this.abstainCount = abstainCount;
    }
}
