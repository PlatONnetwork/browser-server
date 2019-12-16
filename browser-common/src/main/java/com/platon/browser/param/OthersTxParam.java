package com.platon.browser.param;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 14:58
 * tyType=1004发起委托(委托)
 */
public class OthersTxParam extends TxParam{
    private String data;

    public String getData() {
        return data;
    }

    public OthersTxParam setData(String data) {
        this.data = data;
        return this;
    }
}
