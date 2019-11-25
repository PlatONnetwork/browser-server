package com.platon.browser.config.govern;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @description: 可修改的惩罚参数配置
 * @author: chendongming@juzix.net
 * @create: 2019-11-25 18:31:37
 **/
@Data
@Slf4j
@Builder
public class Slashing {
    private BigDecimal slashFractionDuplicateSign;
    private BigDecimal duplicateSignReportReward;
    private BigDecimal maxEvidenceAge;
    private BigDecimal slashBlocksReward;
}
