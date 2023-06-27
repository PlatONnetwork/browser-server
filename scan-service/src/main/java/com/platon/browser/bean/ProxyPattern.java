package com.platon.browser.bean;

import lombok.Data;

@Data
public class ProxyPattern {
    private ContractInfo proxy;
    private ContractInfo implementation;
}
