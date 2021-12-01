package com.platon.browser.analyzer.epoch;

import com.platon.browser.bean.CommonConstant;
import com.platon.browser.service.statistic.StatisticService;
import com.platon.browser.utils.ChainVersionUtil;
import com.platon.browser.v0160.service.DelegateBalanceAdjustmentService;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.contracts.ppos.dto.resp.TallyResult;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.v0150.V0150Config;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.custommapper.NewBlockMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.param.epoch.NewBlock;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.proposal.ProposalService;
import com.platon.browser.v0150.bean.AdjustParam;
import com.platon.browser.v0150.service.StakingDelegateBalanceAdjustmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Service
public class OnNewBlockAnalyzer {

    @Resource
    private NodeCache nodeCache;

    @Resource
    private NewBlockMapper newBlockMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private ProposalCache proposalCache;

    @Resource
    private ProposalService proposalService;

    @Resource
    private ProposalMapper proposalMapper;

    @Resource
    private ParameterService parameterService;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private V0150Config v0150Config;

    @Resource
    private StakingDelegateBalanceAdjustmentService stakingDelegateBalanceAdjustmentService;

    @Resource
    private SpecialApi specialApi;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private StatisticService statisticService;

    @Resource
    private DelegateBalanceAdjustmentService delegateBalanceAdjustmentService;

    public void analyze(CollectionEvent event, Block block) throws NoSuchBeanException {

        long startTime = System.currentTimeMillis();

        networkStatCache.getNetworkStat().setCurNumber(event.getBlock().getNum());
        networkStatCache.getNetworkStat().setCurBlockHash(event.getBlock().getHash());
        NewBlock newBlock = NewBlock.builder()
                                    .nodeId(block.getNodeId())
                                    .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                                    .blockRewardValue(event.getEpochMessage().getBlockReward())
                                    .feeRewardValue(new BigDecimal(block.getTxFee()))
                                    .predictStakingReward(event.getEpochMessage().getStakeReward())
                                    .build();

        newBlockMapper.newBlock(newBlock);
        log.info("块高[{}]节点[{}]的手续费为[{}]出块奖励为[{}]", event.getBlock().getNum(), newBlock.getNodeId(), newBlock.getFeeRewardValue(), newBlock.getBlockRewardValue());

        // 检查当前区块是否有参数提案生效
        Set<String> proposalTxHashSet = proposalCache.get(block.getNum());
        if (proposalTxHashSet != null) {
            ProposalExample proposalExample = new ProposalExample();
            proposalExample.createCriteria().andHashIn(new ArrayList<>(proposalTxHashSet));
            List<Proposal> proposalList = proposalMapper.selectByExample(proposalExample);
            Map<String, Proposal> proposalMap = new HashMap<>();
            proposalList.forEach(p -> proposalMap.put(p.getHash(), p));
            List<Config> configList = new ArrayList<>();
            for (String hash : proposalTxHashSet) {
                try {
                    TallyResult tr = proposalService.getTallyResult(hash);
                    if (tr == null) {
                        continue;
                    }
                    if (tr.getStatus() == CustomProposal.StatusEnum.PASS.getCode() || tr.getStatus() == CustomProposal.StatusEnum.FINISH.getCode()) {
                        // 提案通过（参数提案，status=2）||提案生效（升级提案,status=5）：
                        // 把提案表中的参数覆盖到Config表中对应的参数
                        Proposal proposal = proposalMap.get(hash);
                        if (proposal.getType() == CustomProposal.TypeEnum.PARAMETER.getCode()) {
                            // 如果是参数提案
                            // 把提案表中的参数覆盖到Config表中对应的参数
                            Config config = new Config();
                            config.setModule(proposal.getModule());
                            config.setName(proposal.getName());
                            config.setStaleValue(proposal.getStaleValue());
                            config.setValue(proposal.getNewValue());
                            configList.add(config);
                        }
                        if (proposal.getType() == CustomProposal.TypeEnum.UPGRADE.getCode()) {
                            // 如果是升级提案
                            // 则查询治理参数详情，并把新参数值覆盖到Config表中对应的参数
                            List<GovernParam> governParamList = platOnClient.getGovernParamValue("");
                            governParamList.forEach(gp -> {
                                Config config = new Config();
                                config.setModule(gp.getParamItem().getModule());
                                config.setName(gp.getParamItem().getName());
                                config.setStaleValue(gp.getParamValue().getStaleValue());
                                config.setValue(gp.getParamValue().getValue());
                                configList.add(config);
                            });

                            BigInteger proposalVersion = new BigInteger(proposal.getNewVersion());
                            String proposalPipid = proposal.getPipId();
                            BigInteger configVersion = v0150Config.getAdjustmentActiveVersion();
                            String configPipid = v0150Config.getAdjustmentPipId();
                            if (proposalVersion.compareTo(configVersion) >= 0 && proposalPipid.equals(configPipid)) {
                                // 升级提案版本号及提案ID与配置文件中指定的一样，则执行调账逻辑
                                List<AdjustParam> adjustParams = specialApi.getStakingDelegateAdjustDataList(platOnClient.getWeb3jWrapper().getWeb3j(), BigInteger.valueOf(block.getNum()));
                                adjustParams.forEach(param -> {
                                    param.setBlockTime(block.getTime());
                                    param.setSettleBlockCount(chainConfig.getSettlePeriodBlockCount());
                                });
                                stakingDelegateBalanceAdjustmentService.adjust(adjustParams);
                            }
                            // alaya主网兼容底层升级到0.16.0的调账功能，对应底层issue1583
                            BigInteger v0160Version = ChainVersionUtil.toBigIntegerVersion(CommonConstant.V0160_VERSION);
                            if (proposalVersion.compareTo(v0160Version) == 0 && CommonConstant.ALAYA_CHAIN_ID == chainConfig.getChainId()) {
                                delegateBalanceAdjustmentService.adjust();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("get error", e);
                    throw new BusinessException(e.getMessage());
                }
            }
            if (!configList.isEmpty()) {
                // 更新配置表config及内存中的blockChainConfig
                parameterService.rotateConfig(configList);
            }
        }

        statisticService.nodeSettleStatisBlockNum(event);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }

}
