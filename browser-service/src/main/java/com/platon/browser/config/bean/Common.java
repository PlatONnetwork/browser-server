
package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Common {
    // 结算周期规定的分钟数（整数）
    private BigInteger ExpectedMinutes;
    // 系统分配的节点出块时间窗口
    private BigInteger NodeBlockTimeWindow;
    // 每个验证人每个view出块数量目标值
    private BigInteger PerRoundBlocks;
    // 当前共识轮验证节点数量
    private BigInteger ValidatorCount;
    // 增发周期的分钟数
    private BigInteger AdditionalCycleTime;
}
