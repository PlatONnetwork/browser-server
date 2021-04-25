package com.platon.browser.bean;

import com.platon.browser.dao.entity.RpPlan;

import java.util.Date;

/**
 * @Auther: dongqile
 * @Date: 2019/8/31 16:44
 * @Description: 锁仓计划实体扩展类
 */
public class CustomRpPlan extends RpPlan {
    public CustomRpPlan(){
        super();
        Date date = new Date();
        this.setCreateTime(date);
        this.setUpdateTime(date);
    }
}
