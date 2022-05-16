package com.platon.browser.service.govern;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.bean.govern.ModifiableParam;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.dao.custommapper.CustomConfigMapper;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 治理参数服务
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-25 20:36:04
 **/
@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, Error.class})
public class ParameterService {

    @Resource
    private ConfigMapper configMapper;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private CustomConfigMapper customConfigMapper;

    /**
     * 使用debug_economic_config接口返回的数据初始化配置表，只有从第一个块开始同步时需要调用
     */
    public void initConfigTable() throws Exception {
        log.info("治理参数初始化...");
        configMapper.deleteByExample(null);
        // 调用提案合约只是为了取得完整的可治理参数列表，至于此时的值是不是链第0块时的值，是不能确定的，所以需要
        // 使用debugEconomicConfig接口获取回来的最初始的值进行替代
        List<GovernParam> governParamList = platOnClient.getProposalContract().getParamList("").send().getData();
        List<Config> configList = new ArrayList<>();
        int id = 1;
        Date date = new Date();
        for (GovernParam gp : governParamList) {
            Config config = new Config();
            config.setId(id);
            config.setModule(gp.getParamItem().getModule());
            config.setName(gp.getParamItem().getName());
            config.setRangeDesc(gp.getParamItem().getDesc());
            config.setActiveBlock(0L);
            configList.add(config);
            config.setCreateTime(date);
            config.setUpdateTime(date);

            // Alaya版本特殊处理【锁仓最小释放金额属性】，因为：
            // 在Alaya版本中，debug_economic接口不会返回minimumRelease参数，因此需要在提案合约中查询出来并设置到BlockChainConfig实例中
            // 防止后面代码 getValueInBlockChainConfig("minimumRelease") 时取不到参数值报错
            ModifiableGovernParamEnum paramEnum = ModifiableGovernParamEnum.getMap().get(config.getName());
            if (paramEnum == ModifiableGovernParamEnum.RESTRICTING_MINIMUM_RELEASE) {
                // 如果参数是锁仓最小释放金额，则把blockChainConfig中的锁仓最小释放金额属性设置为当前查询的值
                String minimumRelease = gp.getParamValue().getValue();
                chainConfig.setRestrictingMinimumRelease(new BigDecimal(minimumRelease));
                // 接下来的代码就可以从blockChainConfig实例中获取此值了
            }

            // 浏览器刚启动时在BlockChainConfig中调用debugEconomicConfig接口取得链刚启动时的参数
            // 所以从零开始同步时，需要从BlockChainConfig取得初始参数值
            String initValue = getValueInBlockChainConfig(config.getName());
            config.setInitValue(initValue);
            config.setStaleValue(initValue);
            config.setValue(initValue);
            id++;
        }
        configMapper.batchInsert(configList);
    }

    /**
     * 使用配置表中的配置覆盖内存中的BlockChainConfig，在重新启动的时候调用
     */
    public void overrideBlockChainConfig() {
        // 使用数据库config表的配置覆盖当前配置
        List<Config> configList = configMapper.selectByExample(null);
        ModifiableParam modifiableParam = ModifiableParam.builder().build().init(configList);

        //创建验证人最低的质押Token数(K)
        chainConfig.setStakeThreshold(modifiableParam.getStaking().getStakeThreshold());
        //委托人每次委托及赎回的最低Token数(H)
        chainConfig.setDelegateThreshold(modifiableParam.getStaking().getOperatingThreshold());
        //节点质押退回锁定周期
        chainConfig.setUnStakeRefundSettlePeriodCount(modifiableParam.getStaking().getUnStakeFreezeDuration().toBigInteger());
        //备选结算周期验证节点数量(U)
        chainConfig.setSettlementValidatorCount(modifiableParam.getStaking().getMaxValidators().toBigInteger());
        //举报最高处罚n3‱
        chainConfig.setDuplicateSignSlashRate(modifiableParam.getSlashing().getSlashFractionDuplicateSign().divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //举报奖励n4%
        chainConfig.setDuplicateSignRewardRate(modifiableParam.getSlashing().getDuplicateSignReportReward().divide(BigDecimal.valueOf(100), 2, RoundingMode.FLOOR));
        //证据有效期
        chainConfig.setEvidenceValidEpoch(modifiableParam.getSlashing().getMaxEvidenceAge());
        //扣除区块奖励的个数
        chainConfig.setSlashBlockRewardCount(modifiableParam.getSlashing().getSlashBlocksReward());
        //默认每个区块的最大Gas
        chainConfig.setMaxBlockGasLimit(modifiableParam.getBlock().getMaxBlockGasLimit());
        // 零出块次数阈值，在指定时间范围内达到该次数则处罚
        chainConfig.setZeroProduceNumberThreshold(modifiableParam.getSlashing().getZeroProduceNumberThreshold());
        // 上一次零出块后，在往后的N个共识周期内如若再出现零出块，则在这N个共识周期完成时记录零出块信息
        chainConfig.setZeroProduceCumulativeTime(modifiableParam.getSlashing().getZeroProduceCumulativeTime());
        //零出块锁定结算周期数
        chainConfig.setZeroProduceFreezeDuration(modifiableParam.getSlashing().getZeroProduceFreezeDuration());
        //奖励比例改变周期
        chainConfig.setRewardPerChangeInterval(modifiableParam.getStaking().getRewardPerChangeInterval());
        //奖励比例最大变更范围+/-xxx
        chainConfig.setRewardPerMaxChangeRange(modifiableParam.getStaking().getRewardPerMaxChangeRange());
        //增发比例
        chainConfig.setAddIssueRate(modifiableParam.getReward().getIncreaseIssuanceRatio().divide(new BigDecimal(10000)));
        //锁仓释放最小金额
        chainConfig.setRestrictingMinimumRelease(modifiableParam.getRestricting().getMinimumRelease());
    }

    /**
     * 配置值轮换：value旧值覆盖到stale_value，参数中的新值覆盖value
     *
     * @param activeConfigList 被激活的配置信息列表
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void rotateConfig(List<Config> activeConfigList) {
        // 更新配置表
        customConfigMapper.rotateConfig(activeConfigList);
        //更新内存中的BlockChainConfig
        overrideBlockChainConfig();
    }

    /**
     * 根据参数提案中的参数name获取当前blockChainConfig中的对应的当前值
     *
     * @param name
     * @return
     */
    public String getValueInBlockChainConfig(String name) {
        ModifiableGovernParamEnum paramEnum = ModifiableGovernParamEnum.getMap().get(name);
        String staleValue = "";
        switch (paramEnum) {
            // 质押相关
            case STAKE_THRESHOLD:
                staleValue = chainConfig.getStakeThreshold().toString();
                break;
            case OPERATING_THRESHOLD:
                staleValue = chainConfig.getDelegateThreshold().toString();
                break;
            case MAX_VALIDATORS:
                staleValue = chainConfig.getSettlementValidatorCount().toString();
                break;
            case UN_STAKE_FREEZE_DURATION:
                staleValue = chainConfig.getUnStakeRefundSettlePeriodCount().toString();
                break;
            // 惩罚相关
            case SLASH_FRACTION_DUPLICATE_SIGN:
                staleValue = chainConfig.getDuplicateSignSlashRate().multiply(BigDecimal.valueOf(10000)).setScale(0).toString();
                break;
            case DUPLICATE_SIGN_REPORT_REWARD:
                staleValue = chainConfig.getDuplicateSignRewardRate().multiply(BigDecimal.valueOf(100)).setScale(0).toString();
                break;
            case MAX_EVIDENCE_AGE:
                staleValue = chainConfig.getEvidenceValidEpoch().toString();
                break;
            case SLASH_BLOCKS_REWARD:
                staleValue = chainConfig.getSlashBlockRewardCount().toString();
                break;
            // 区块相关
            case MAX_BLOCK_GAS_LIMIT:
                staleValue = chainConfig.getMaxBlockGasLimit().toString();
                break;
            // 零出块次数阈值，在指定时间范围内达到该次数则处罚
            case ZERO_PRODUCE_NUMBER_THRESHOLD:
                staleValue = chainConfig.getZeroProduceNumberThreshold().toString();
                break;
            // 上一次零出块后，在往后的N个共识周期内如若再出现零出块，则在这N个共识周期完成时记录零出块信息
            case ZERO_PRODUCE_CUMULATIVE_TIME:
                staleValue = chainConfig.getZeroProduceCumulativeTime().toString();
                break;
            // 节点零出块惩罚被锁定时间
            case ZERO_PRODUCE_FREEZE_DURATION:
                staleValue = chainConfig.getZeroProduceFreezeDuration().toString();
                break;
            case REWARD_PER_MAX_CHANGE_RANGE:
                staleValue = chainConfig.getRewardPerMaxChangeRange().toString();
                break;
            case REWARD_PER_CHANGE_INTERVAL:
                staleValue = chainConfig.getRewardPerChangeInterval().toString();
                break;
            case INCREASE_ISSUANCE_RATIO:
                staleValue = chainConfig.getAddIssueRate().multiply(new BigDecimal(10000)).setScale(0).toPlainString();
                break;
            case RESTRICTING_MINIMUM_RELEASE:
                //最小锁仓释放金额(LAT)
                staleValue = chainConfig.getRestrictingMinimumRelease().toString();
                break;
            default:
                break;
        }
        return staleValue;
    }

}
