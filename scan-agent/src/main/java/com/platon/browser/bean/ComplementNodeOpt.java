package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.NodeOpt;

import java.util.Date;

public class ComplementNodeOpt extends NodeOpt {
    private ComplementNodeOpt(){}
    public static ComplementNodeOpt newInstance(){
        ComplementNodeOpt bean = new ComplementNodeOpt();
        Date date = new Date();
        bean.setCreTime(date)
            .setUpdTime(date)
           .setBNum(0L);
        return bean;
    }
}
