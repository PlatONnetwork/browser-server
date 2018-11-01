package com.platon.browser.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TpsCountParam {
    private String chainId;
    private int minute;
}