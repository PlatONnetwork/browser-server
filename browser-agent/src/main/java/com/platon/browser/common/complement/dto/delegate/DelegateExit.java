package com.platon.browser.common.complement.dto.delegate;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DelegateExit extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DELEGATE_EXIT;
    }
}
