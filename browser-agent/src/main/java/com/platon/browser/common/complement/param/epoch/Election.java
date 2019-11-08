package com.platon.browser.common.complement.param.epoch;

import java.util.Date;
import java.util.List;

import com.platon.browser.common.complement.param.BusinessParam;
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
public class Election  extends BusinessParam {

    /**
     * 需要惩罚的列表
     */
    private List <String> slashNodeList;

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