package com.platon.browser.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddressCountParam {
    private String chainId;
    private List<String> txTypes;
}