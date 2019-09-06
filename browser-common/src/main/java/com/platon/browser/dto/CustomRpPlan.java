package com.platon.browser.dto;

import com.platon.browser.dao.entity.RpPlan;

import java.util.Date;

/**
 * User: dongqile
 * Date: 2019/8/31
 * Time: 16:44
 */
public class CustomRpPlan extends RpPlan {
    public CustomRpPlan(){
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }
}
