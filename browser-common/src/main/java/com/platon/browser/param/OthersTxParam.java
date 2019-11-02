package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

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
public class OthersTxParam extends TxParam{

}
