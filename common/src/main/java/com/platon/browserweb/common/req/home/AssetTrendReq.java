package com.platon.browserweb.common.req.home;

import com.github.fartherp.framework.common.validate.Value;
import com.platon.browserweb.common.validate.CurrencyGroup;
import com.platon.browserweb.common.validate.QuoteCurrencyGroup;
import com.platon.browserweb.common.validate.SymbolGroup;
import com.platon.browserweb.common.validate.TypeGroup;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/6/26
 * Time: 11:02
 * 用户资产变化趋势请求参数
 */
public class AssetTrendReq {

    /**
     *  始（时间）
     */
    private String startTime;

    /**
     *  终（时间）
     */
    private String endTime;

    /**
     *  币种（例：BTC,ETH等...）
     */
    @NotEmpty(groups = CurrencyGroup.class)
    private String currency;

    /**
     *  代理商id
     */
    private long brokerId;


    /**
     * 默认近一个月
     */
    private Integer dateType = 30;

    /**
     * 计价币代码
     */
    @NotEmpty(groups = QuoteCurrencyGroup.class)
    private String quoteCurrency;


    @NotEmpty(groups = TypeGroup.class)
    @Value(values = {"REGISTER", "USER_ONLINE","USER_ACTIVE"})
    private String type;

    @NotEmpty(groups = SymbolGroup.class)
    private String symbol;

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol ( String symbol ) {
        this.symbol = symbol;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getStartTime () {
        return startTime;
    }

    public void setStartTime ( String startTime ) {
        this.startTime = startTime;
    }

    public String getEndTime () {
        return endTime;
    }

    public void setEndTime ( String endTime ) {
        this.endTime = endTime;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
    }

    public String getQuoteCurrency () {
        return quoteCurrency;
    }

    public void setQuoteCurrency ( String quoteCurrency ) {
        this.quoteCurrency = quoteCurrency;
    }
}