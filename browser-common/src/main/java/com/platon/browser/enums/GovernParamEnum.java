package com.platon.browser.enums;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description: 治理参数枚举
 */
public enum GovernParamEnum {
    STAKING_STAKE_THRESHOLD("staking", "stakeThreshold"),
    STAKING_OPERATING_THRESHOLD("staking", "operatingThreshold"),
    STAKING_MAX_VALIDATORS("staking", "maxValidators"),
    STAKING_UN_STAKE_FREEZE_DURATION("staking", "unStakeFreezeDuration"),
    SLASHING_SLASH_FRACTION_DUPLICATE_SIGN("slashing", "slashFractionDuplicateSign"),
    SLASHING_DUPLICATE_SIGN_REPORT_REWARD("slashing", "duplicateSignReportReward"),
    SLASHING_MAX_EVIDENCE_AGE("slashing", "maxEvidenceAge"),
    SLASHING_SLASH_BLOCKS_REWARD("slashing", "slashBlocksReward"),
    BLOCK_MAX_BLOCK_GAS_LIMIT("block", "maxBlockGasLimit");
    private String module;
    private String name;
    GovernParamEnum(String module, String name) {
        this.module = module;
        this.name = name;
    }
    public String getModule() {
        return module;
    }
    public String getName() {
        return name;
    }
}
