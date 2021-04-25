package com.platon.browser.param;

import com.alibaba.fastjson.JSON;
import lombok.Builder;

public abstract class TxParam {
    public String toJSONString(){
        return JSON.toJSONString(this);
    }
}
