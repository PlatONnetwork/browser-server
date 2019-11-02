package com.platon.browser.common.complement.dto.stake;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
public class StakeExit extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_EXIT;
    }
}
