package com.platon.browser.complement.dao.param.epoch;

import java.util.Date;
import java.util.List;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 选举周期切换参数入库
 */
@Data
@Builder
@Accessors(chain = true)
public class Election implements BusinessParam {

    /**
     * 需要惩罚的列表
     */
    private List <Staking> slashNodeList;

    /**
     *  结算周期
     */
    private int settingEpoch;
    
    /**
     * 时间
     */
    private Date time;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.ELECTION_EPOCH;
    }
}