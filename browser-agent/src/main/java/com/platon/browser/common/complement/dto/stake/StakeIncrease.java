package com.platon.browser.common.complement.dto.stake;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class StakeIncrease extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_INCREASE;
    }
}
