package com.platon.browser.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 内置合约
 *
 * @date 2021/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InnerContract {

    /**
     * 对应枚举ContractDescEnum名称
     */
    private String contractDescEnumName;

    private String contractName;

    private String creator;

    private String contractHash;

}
