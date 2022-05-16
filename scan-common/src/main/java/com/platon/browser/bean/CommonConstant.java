package com.platon.browser.bean;

/**
 * 公共常数
 *
 * @date 2021/4/4
 */
public class CommonConstant {

    /**
     * 链路ID名
     */
    public static final String TRACE_ID = "trace-id";

    /**
     * 0.16.0版本号
     */
    public static final String V0160_VERSION = "0.16.0";

    /**
     * alaya主网链id
     */
    public static final long ALAYA_CHAIN_ID = 201018;

    /**
     * 请求参数链路ID名
     */
    public static final String REQ_TRACE_ID = "traceId";

    /**
     * 用于24小时出块率计算的多少个结算周期
     */
    public static final int BLOCK_RATE_SETTLE_EPOCH_NUM = 7;

    /**
     * 重试次数，宁愿丢失数据也不要阻碍主流程追块
     */
    public static final int reTryNum = 3;

}
