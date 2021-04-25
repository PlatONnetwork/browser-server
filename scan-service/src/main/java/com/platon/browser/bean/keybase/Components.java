/**
  * Copyright 2019 bejson.com
  */
package com.platon.browser.bean.keybase;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Components {
    private ValueScore username;
    @JSONField(name="key_fingerprint")
    private KeyFingerprint keyFingerprint;
    private ValueScore stellar;
    @JSONField(name = "full_name")
    private ValueScore fullName;
    private ValueScore twitter;
    private ValueScore github;
    private ValueScore reddit;
    private ValueScore hackernews;
    @JSONField(name="mastodon.social")
    private ValueScore mastodonSocial;
    private List<Websites> websites;
	public ValueScore getUsername() {
		return username;
	}
	public void setUsername(ValueScore username) {
		this.username = username;
	}
	public KeyFingerprint getKeyFingerprint() {
		return keyFingerprint;
	}
	public void setKeyFingerprint(KeyFingerprint keyFingerprint) {
		this.keyFingerprint = keyFingerprint;
	}
	public ValueScore getStellar() {
		return stellar;
	}
	public void setStellar(ValueScore stellar) {
		this.stellar = stellar;
	}
	public ValueScore getFullName() {
		return fullName;
	}
	public void setFullName(ValueScore fullName) {
		this.fullName = fullName;
	}
	public ValueScore getTwitter() {
		return twitter;
	}
	public void setTwitter(ValueScore twitter) {
		this.twitter = twitter;
	}
	public ValueScore getGithub() {
		return github;
	}
	public void setGithub(ValueScore github) {
		this.github = github;
	}
	public ValueScore getReddit() {
		return reddit;
	}
	public void setReddit(ValueScore reddit) {
		this.reddit = reddit;
	}
	public ValueScore getHackernews() {
		return hackernews;
	}
	public void setHackernews(ValueScore hackernews) {
		this.hackernews = hackernews;
	}
	public ValueScore getMastodonSocial() {
		return mastodonSocial;
	}
	public void setMastodonSocial(ValueScore mastodonSocial) {
		this.mastodonSocial = mastodonSocial;
	}
	public List<Websites> getWebsites() {
		return websites;
	}
	public void setWebsites(List<Websites> websites) {
		this.websites = websites;
	}

}
