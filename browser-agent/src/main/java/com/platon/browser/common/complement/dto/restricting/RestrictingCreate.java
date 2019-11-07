package com.platon.browser.common.complement.dto.restricting;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class RestrictingCreate extends BusinessParam {


    @Override
    public BusinessType getBusinessType() {
        return BusinessType.RESTRICTING_CREATE;
    }
}
