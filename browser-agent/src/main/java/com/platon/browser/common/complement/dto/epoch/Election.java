package com.platon.browser.common.complement.dto.epoch;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 选举周期切换参数入库
 */
@Data
@Builder
@Slf4j
@Accessors(chain = true)
public class Election  extends BusinessParam {


    /**
     * 上轮结算周期验证人
     */
    private List <String> preValidatorList;

    /**
     *  结算周期
     */
    private int settingEpoch;


    /**
     * 交易所在区块号
     */
    private BigInteger bNum;

    /**
     * 时间
     */
    private Date time;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.ELECTION_EPOCH;
    }


}