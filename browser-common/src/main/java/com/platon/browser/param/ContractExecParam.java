package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:58
 * tyType=1004发起委托(委托)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class ContractExecParam extends TxParam{

}
