package com.platon.browserweb.common.req.iceReq;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * User: dongqile
 * Date: 2018/9/6
 * Time: 17:04
 */
public class GetKlineReq {

    /**
     * 代理商id
     */

    private long brokerId;

    /**
     * 货币对
     */
    @NotEmpty
    private String symbol;

    /**
     * K线类型
     *       klineType=MIN_1:获取一分钟交易行情图
     *       klineType=MIN_3:获取三分钟交易行情图
     *       klineType=MIN_5 :获取五分钟交易行情图
     *       klineType=MIN_10:获取十分钟交易行情图
     *       klineType=MIN_15:获取十五分钟交易行情图
     *       klineType=MIN_30:获取三十分钟交易行情图
     *       klineType=HOUR_1:获取一小时交易行情图
     *       klineType=HOUR_2:获取两小时交易行情图
     *       klineType=HOUR_4:获取四小时交易行情图
     *       klineType=HOUR_6:获取六小时交易行情图
     *       klineType=HOUR_12:获取十二小时交易行情图
     *       klineType=DAILY:获取一天交易行情图，日线
     *       klineType=WEEKLY:获取一周交易行情图，周线
     */
    @NotEmpty
    private String klineType;

    /**
     * 始（用户创建时间）
     */
    @NotNull
    private Date startTime;

    /**
     * 终（用户创建时间）
     */
    @NotNull
    private Date endTime;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol ( String symbol ) {
        this.symbol = symbol;
    }

    public String getKlineType () {
        return klineType;
    }

    public void setKlineType ( String klineType ) {
        this.klineType = klineType;
    }

    public Date getStartTime () {
        return startTime;
    }

    public void setStartTime ( Date startTime ) {
        this.startTime = startTime;
    }

    public Date getEndTime () {
        return endTime;
    }

    public void setEndTime ( Date endTime ) {
        this.endTime = endTime;
    }
}