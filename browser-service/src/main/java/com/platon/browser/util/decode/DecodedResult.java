package com.platon.browser.util.decode;

import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.OthersTxParam;
import com.platon.browser.param.TxParam;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 解码后的结果
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 20:38:57
 **/
@Data
@Accessors(chain = true)
public class DecodedResult {
    private TxParam param= OthersTxParam.builder().build();
    private Transaction.TypeEnum typeEnum= Transaction.TypeEnum.OTHERS;
}
