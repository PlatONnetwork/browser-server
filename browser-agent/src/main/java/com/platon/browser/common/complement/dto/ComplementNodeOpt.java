package com.platon.browser.common.complement.dto;

import com.platon.browser.elasticsearch.dto.NodeOpt;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
@Accessors(chain = true)
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
