package com.platon.browser.common.complement.dto;

import com.platon.browser.common.enums.BusinessType;
import lombok.Data;

/**
 * 持久化参数基类
 */
@Data
public abstract class BusinessParam {
    public abstract BusinessType getBusinessType();
}
