package com.platon.browser.v0152.bean;

import com.platon.browser.v0152.enums.ErcTypeEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class ErcTokenCacheItem {
    private ErcTypeEnum typeEnum;
    private String address;
    public void init(ErcToken initData){
        BeanUtils.copyProperties(initData,this);
    }
}
