
package com.platon.browser.config.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigInteger;

@Data
public class Common {
    // 结算周期规定的分钟数（整数）
    @JSONField(name = "ExpectedMinutes")
    private BigInteger expectedMinutes;
    // 系统分配的节点出块时间窗口
    @JSONField(name = "NodeBlockTimeWindow")
    private BigInteger nodeBlockTimeWindow;
    // 每个验证人每个view出块数量目标值
    @JSONField(name = "PerRoundBlocks")
    private BigInteger perRoundBlocks;
    // 当前共识轮验证节点数量
    @JSONField(name = "ValidatorCount")
    private BigInteger validatorCount;
    // 增发周期的分钟数
    @JSONField(name = "AdditionalCycleTime")
    private BigInteger additionalCycleTime;
}
