package com.platon.browser.common.complement.dto;

import com.platon.browser.common.complement.param.slash.Report;
import com.platon.browser.common.complement.param.stake.StakeCreate;
import com.platon.browser.common.complement.param.stake.StakeExit;
import com.platon.browser.common.complement.param.stake.StakeIncrease;
import com.platon.browser.common.complement.param.stake.StakeModify;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.exception.BeanCreateOrUpdateException;
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

    public ComplementNodeOpt updateWithStakeCreate(StakeCreate param) throws BeanCreateOrUpdateException {

        return this;
    }

    public ComplementNodeOpt updateWithStakeIncrease(StakeIncrease param) throws BeanCreateOrUpdateException {

        return this;
    }

    public ComplementNodeOpt updateWithStakeModify(StakeModify param) throws BeanCreateOrUpdateException {

        return this;
    }

    public ComplementNodeOpt updateWithStakeExit(StakeExit param) throws BeanCreateOrUpdateException {

        return this;
    }

    public ComplementNodeOpt updateWithReport(Report param) throws BeanCreateOrUpdateException {

        return this;
    }

}
