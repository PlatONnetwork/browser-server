
package com.platon.browser.bean.keybase;

import com.alibaba.fastjson.annotation.JSONField;

public class Completion {
    @JSONField(name="total_score")
    private Double totalScore;
    private Components components;
    private String uid;
    private String thumbnail;
    @JSONField(name="is_followee")
    private Boolean isFollowee;
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public Components getComponents() {
		return components;
	}
	public void setComponents(Components components) {
		this.components = components;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Boolean getIsFollowee() {
		return isFollowee;
	}
	public void setIsFollowee(Boolean isFollowee) {
		this.isFollowee = isFollowee;
	}
    
}
