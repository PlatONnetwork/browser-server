package com.platon.browser.common.complement.dto.delegate;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class DelegateCreate extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DELEGATE_CREATE;
    }
}
