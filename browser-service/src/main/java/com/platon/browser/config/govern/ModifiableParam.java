package com.platon.browser.config.govern;

import com.platon.browser.dao.entity.Config;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 可修改的治理参数
 * @author: chendongming@juzix.net
 * @create: 2019-11-25 17:34:20
 **/
@Data
@Slf4j
@Builder
public class ModifiableParam {

    private Staking staking;
    private Slashing slashing;
    private Block block;

    public ModifiableParam init(List<Config> configList) {
        this.staking=Staking.builder()
                .maxValidators(BigDecimal.ZERO)
                .operatingThreshold(BigDecimal.ZERO)
                .stakeThreshold(BigDecimal.ZERO)
                .unStakeFreezeDuration(BigDecimal.ZERO)
                .build();
        this.slashing= Slashing.builder()
                .duplicateSignReportReward(BigDecimal.ZERO)
                .maxEvidenceAge(BigDecimal.ZERO)
                .slashBlocksReward(BigDecimal.ZERO)
                .slashFractionDuplicateSign(BigDecimal.ZERO)
                .build();
        this.block=Block.builder()
                .maxBlockGasLimit(BigDecimal.ZERO)
                .build();
        configList.forEach(config -> {
            ModifiableGovernParamEnum paramEnum = ModifiableGovernParamEnum.getMap().get(config.getName());
            switch (paramEnum){
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
                default:
                    break;
            }
        });
        return this;
    }
}
