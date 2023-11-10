package com.platon.browser.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 结算周期信息
 *
 * @date: 2021/12/22
 */

@Data
public class EpochInfo {

    /**
     * 出块奖励--废弃
     */
    private BigDecimal packageReward;

    /**
     * 结算周期质押奖励--废弃
     */
    private BigDecimal stakingReward;

    /**
     * 当前增发周期 //starts from 1
     */
    private BigDecimal chainAge;

    /**
     * 当前增发周期开始区块号
     */
    private BigDecimal yearStartBlockNum;

    /**
     * 当前增发周期结束区块号
     */
    private BigDecimal yearEndBlockNum;

    /**
     * 当前增发周期剩下的结算周期数
     */
    private BigDecimal remainEpoch;

    /**
     * 平均出块时间
     */
    private BigDecimal avgPackTime;

    /**
     * 当前结算周期的出块奖励
     */
    private BigDecimal curPackageReward;

    /**
     * 当前结算周期的质押奖励
     */
    private BigDecimal curStakingReward;

    /**
     * 下一个结算周期的出块奖励
     */
    private BigDecimal nextPackageReward;

    /**
     * 下一个结算周期的质押奖励
     */
    private BigDecimal nextStakingReward;

    public static void main(String[] args)  {
        String json = "{\n" +
                "\t\"nextPackageReward\": 4580147713699926138,\n" +
                "\t\"nextStakingReward\": 49236587922274205984720,\n" +
                "\t\"curPackageReward\": 4580147713699926138,\n" +
                "\t\"curStakingReward\": 49236587922274205984720,\n" +
                "\t\"chainAge\": 1,\n" +
                "\t\"yearStartBlockNum\": 1,\n" +
                "\t\"yearEndBlockNum\": 28605751,\n" +
                "\t\"remainEpoch\": 2661,\n" +
                "\t\"avgPackTime\": 1103\n" +
                "}";
        EpochInfo epochInfo = JSON.parseObject(json, EpochInfo.class);
        System.out.println("echoInfo:" + JSON.toJSONString(epochInfo));
        System.out.println("-1L % 1000L == 0" + (-1L % 1000L == 0));
        System.out.println("-1L % 1000L == " + (-1L % 1000L));
    }
}
