package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 16:10
 * txType=4000创建锁仓计划(创建锁仓)
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestrictingParam extends TxParam{
    /**
     * 锁仓释放到账账户
     */
    private String account;

    /**
     * 锁仓具体计划
     */
    private List<PlanParam> plan;
}
