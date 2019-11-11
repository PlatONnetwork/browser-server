/**
  * Copyright 2019 bejson.com
  */
package com.platon.browser.dto.keybase;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
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

}
