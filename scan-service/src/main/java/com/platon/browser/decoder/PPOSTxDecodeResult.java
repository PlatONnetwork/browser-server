package com.platon.browser.decoder;

import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.OthersTxParam;
import com.platon.browser.param.TxParam;

/**
 * @description: 解码后的结果
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 20:38:57
 **/
public class PPOSTxDecodeResult {
    private TxParam param= new OthersTxParam();
    private Transaction.TypeEnum typeEnum= Transaction.TypeEnum.OTHERS;

    private Integer txErrCode;

    public Integer getTxErrCode() {
        return txErrCode;
    }

    public void setTxErrCode(Integer txErrCode) {
        this.txErrCode = txErrCode;
    }

    public TxParam getParam() {
        return param;
    }

    public PPOSTxDecodeResult setParam(TxParam param) {
        this.param = param;
        return this;
    }

    public Transaction.TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public PPOSTxDecodeResult setTypeEnum(Transaction.TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
        return this;
    }
}
