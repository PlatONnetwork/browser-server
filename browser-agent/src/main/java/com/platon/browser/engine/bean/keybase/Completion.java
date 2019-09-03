
package com.platon.browser.engine.bean.keybase;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Completion {
    @JSONField(name="total_score")
    private double totalScore;
    private Components components;
    private String uid;
    private String thumbnail;
    @JSONField(name="is_followee")
    private boolean isFollowee;
}
