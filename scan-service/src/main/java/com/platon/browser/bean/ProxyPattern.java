package com.platon.browser.bean;

import lombok.Data;

/**
 * @Author: NieXiang
 * @Date: 2023/6/7
 */
@Data
public class ProxyPattern {

    private ContractInfo proxy;

    private ContractInfo implementation;

}
