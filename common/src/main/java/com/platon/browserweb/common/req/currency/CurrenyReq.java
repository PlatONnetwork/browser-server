package com.platon.browserweb.common.req.currency;

/**
 * User: dongqile
 * Date: 2018/7/27
 * Time: 17:13
 */
public class CurrenyReq {

    /*
* 货币对
* */
    private String symbol;

    /*
    * 是否选择
    * false：未选择
    * true：选中
    * */
    private boolean select;


    public boolean isSelect () {
        return select;
    }

    public void setSelect ( boolean select ) {
        this.select = select;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol ( String symbol ) {
        this.symbol = symbol;
    }


}