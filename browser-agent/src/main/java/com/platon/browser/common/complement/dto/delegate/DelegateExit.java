package com.platon.browser.common.complement.dto.delegate;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
@Accessors(chain = true)
public class DelegateExit extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DELEGATE_EXIT;
    }
}
