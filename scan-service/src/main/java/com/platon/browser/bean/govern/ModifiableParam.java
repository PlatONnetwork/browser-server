package com.platon.browser.bean.govern;

import com.platon.browser.dao.entity.Config;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 可修改的治理参数
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-25 17:34:20
 **/
@Builder
public class ModifiableParam {

    private Staking staking;

    private Slashing slashing;

    private Block block;

    private Reward reward;

    private Restricting restricting;

    public ModifiableParam init(List<Config> configList) {
        this.staking = Staking.builder()
                              .maxValidators(BigDecimal.ZERO)
                              .operatingThreshold(BigDecimal.ZERO)
                              .stakeThreshold(BigDecimal.ZERO)
                              .unStakeFreezeDuration(BigDecimal.ZERO)
                              .unDelegateFreezeDuration(BigDecimal.ZERO)
                              .rewardPerMaxChangeRange(0)
                              .rewardPerChangeInterval(0)
                              .build();
        this.slashing = Slashing.builder()
                                .duplicateSignReportReward(BigDecimal.ZERO)
                                .maxEvidenceAge(BigDecimal.ZERO)
                                .slashBlocksReward(BigDecimal.ZERO)
                                .slashFractionDuplicateSign(BigDecimal.ZERO)
                                .zeroProduceCumulativeTime(0)
                                .zeroProduceNumberThreshold(0)
                                .zeroProduceFreezeDuration(0)
                                .build();
        this.block = Block.builder()
                          .maxBlockGasLimit(BigDecimal.ZERO)
                          .build();
        this.reward = Reward.builder().increaseIssuanceRatio(BigDecimal.ZERO).build();
        this.restricting = Restricting.builder().minimumRelease(BigDecimal.ZERO).build();
        configList.forEach(config -> {
            ModifiableGovernParamEnum paramEnum = ModifiableGovernParamEnum.getMap().get(config.getName());
            switch (paramEnum) {
                // 质押相关
                case STAKE_THRESHOLD:
                    staking.setStakeThreshold(new BigDecimal(config.getValue()));
                    break;
                case OPERATING_THRESHOLD:
                    staking.setOperatingThreshold(new BigDecimal(config.getValue()));
                    break;
                case MAX_VALIDATORS:
                    staking.setMaxValidators(new BigDecimal(config.getValue()));
                    break;
                case UN_STAKE_FREEZE_DURATION:
                    staking.setUnStakeFreezeDuration(new BigDecimal(config.getValue()));
                    break;
                case UN_DELEGATE_FREEZE_DURATION:
                    staking.setUnDelegateFreezeDuration(new BigDecimal(config.getValue()));
                    break;
                // 惩罚相关
                case SLASH_FRACTION_DUPLICATE_SIGN:
                    slashing.setSlashFractionDuplicateSign(new BigDecimal(config.getValue()));
                    break;
                case DUPLICATE_SIGN_REPORT_REWARD:
                    slashing.setDuplicateSignReportReward(new BigDecimal(config.getValue()));
                    break;
                case MAX_EVIDENCE_AGE:
                    slashing.setMaxEvidenceAge(new BigDecimal(config.getValue()));
                    break;
                case SLASH_BLOCKS_REWARD:
                    slashing.setSlashBlocksReward(new BigDecimal(config.getValue()));
                    break;
                // 区块相关
                case MAX_BLOCK_GAS_LIMIT:
                    block.setMaxBlockGasLimit(new BigDecimal(config.getValue()));
                    break;
                case ZERO_PRODUCE_CUMULATIVE_TIME:
                    slashing.setZeroProduceCumulativeTime(Integer.valueOf(config.getValue()));
                    break;
                case ZERO_PRODUCE_NUMBER_THRESHOLD:
                    slashing.setZeroProduceNumberThreshold(Integer.valueOf(config.getValue()));
                    break;
                case ZERO_PRODUCE_FREEZE_DURATION:
                    slashing.setZeroProduceFreezeDuration(Integer.valueOf(config.getValue()));
                    break;
                case REWARD_PER_CHANGE_INTERVAL:
                    staking.setRewardPerChangeInterval(Integer.valueOf(config.getValue()));
                    break;
                case REWARD_PER_MAX_CHANGE_RANGE:
                    staking.setRewardPerMaxChangeRange(Integer.valueOf(config.getValue()));
                    break;
                case INCREASE_ISSUANCE_RATIO:
                    reward.setIncreaseIssuanceRatio(new BigDecimal(config.getValue()));
                    break;
                case RESTRICTING_MINIMUM_RELEASE:
                    restricting.setMinimumRelease(new BigDecimal(config.getValue()));
                    break;
                default:
                    break;
            }
        });
        return this;
    }

    public Staking getStaking() {
        return staking;
    }

    public void setStaking(Staking staking) {
        this.staking = staking;
    }

    public Slashing getSlashing() {
        return slashing;
    }

    public void setSlashing(Slashing slashing) {
        this.slashing = slashing;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Restricting getRestricting() {
        return restricting;
    }

    public void setRestricting(Restricting restricting) {
        this.restricting = restricting;
    }

}
