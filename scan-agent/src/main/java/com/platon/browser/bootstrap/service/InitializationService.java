package com.platon.browser.bootstrap.service;

import com.platon.contracts.ppos.dto.resp.Node;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.*;
import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.enums.AddressTypeEnum;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.publisher.GasEstimateEventPublisher;
import com.platon.browser.service.elasticsearch.*;
import com.platon.browser.service.epoch.EpochRetryService;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.ppos.StakeEpochService;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.v0152.analyzer.ErcCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @description: 启动初始化服务
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class InitializationService {

    private static final InitializationResult initialResult = new InitializationResult();

    @Resource
    private EpochRetryService epochRetryService;

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private NodeMapper nodeMapper;

    @Resource
    private StakingMapper stakingMapper;

    @Resource
    private NetworkStatMapper networkStatMapper;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private NodeCache nodeCache;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private AddressCache addressCache;

    @Resource
    private ParameterService parameterService;

    @Resource
    private ProposalCache proposalCache;

    @Resource
    private GasEstimateLogMapper gasEstimateLogMapper;

    @Resource
    private GasEstimateEventPublisher gasEstimateEventPublisher;

    @Resource
    private StakeEpochService stakeEpochService;

    @Resource
    private EsBlockRepository ESBlockRepository;

    @Resource
    private EsTransactionRepository ESTransactionRepository;

    @Resource
    private EsDelegationRewardRepository ESDelegationRewardRepository;

    @Resource
    private EsNodeOptRepository ESNodeOptRepository;

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    @Resource
    private EsTransferTxRepository esTransferTxRepository;

    @Resource
    private ErcCache ercCache;

    /**
     * 进入应用初始化子流程
     *
     * @param traceId
     * @return com.platon.browser.bootstrap.bean.InitializationResult
     * @date 2021/4/19
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public InitializationResult init(String traceId) throws BlockNumberException {
        log.info("进入应用初始化子流程");
        proposalCache.init();
        ercCache.init();

        // 检查数据库network_stat表,如果没有记录则添加一条,并从链上查询最新内置验证人节点入库至staking表和node表
        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        if (networkStat == null) {
            // 确保chainConfig先就绪
            try {
                parameterService.initConfigTable();
            } catch (Exception e) {
                log.error("", e);
                throw new BusinessException("初始化错误:" + e.getMessage());
            }

            // 创建新的统计记录
            networkStat = CollectionNetworkStat.newInstance();
            networkStat.setId(1);
            networkStat.setCurNumber(-1L);
            networkStat.setAvgPackTime(0L);
            networkStat.setIssueRates(chainConfig.getAddIssueRate().toPlainString());
            networkStat.setErc20TxQty(0);
            networkStat.setErc721TxQty(0);
            networkStatMapper.insert(networkStat);
            initialResult.setCollectedBlockNumber(-1L);
            // 删除节点表和质押表、地址表数据
            nodeMapper.deleteByExample(null);
            stakingMapper.deleteByExample(null);
            addressMapper.deleteByExample(null);
            log.info("删除节点表、质押表、地址表数据");
            // 初始化内置节点
            List<com.platon.browser.dao.entity.Node> nodeList = initInnerStake();
            // 初始化节点缓存
            nodeCache.init(nodeList);
            // 初始化网络缓存
            networkStatCache.init(networkStat);
            // 初始化内置地址
            addressCache.initOnFirstStart();
            // 初始化ES
            initEs();

            return initialResult;
        }

        // 确保chainConfig先就绪
        parameterService.overrideBlockChainConfig();

        initialResult.setCollectedBlockNumber(networkStat.getCurNumber());

        // 初始化节点缓存
        List<com.platon.browser.dao.entity.Node> nodeList = nodeMapper.selectByExample(null);
        nodeCache.init(nodeList);

        // 初始化EVM合约地址缓存，用于后续交易的类型判断（调用EVM合约）
        AddressExample addressExample = new AddressExample();
        addressExample.createCriteria().andTypeEqualTo(AddressTypeEnum.EVM_CONTRACT.getCode());
        List<Address> addressList = addressMapper.selectByExample(addressExample);
        addressCache.initEvmContractAddressCache(addressList);

        // 初始化EVM合约地址缓存，用于后续交易的类型判断（调用EVM合约）
        addressExample = new AddressExample();
        addressExample.createCriteria().andTypeEqualTo(AddressTypeEnum.ERC20_EVM_CONTRACT.getCode());
        addressList = addressMapper.selectByExample(addressExample);

        // 初始化WASM合约地址缓存，用于后续交易的类型判断（调用WASM合约）
        addressExample = new AddressExample();
        addressExample.createCriteria().andTypeEqualTo(AddressTypeEnum.WASM_CONTRACT.getCode());
        addressList = addressMapper.selectByExample(addressExample);
        addressCache.initWasmContractAddressCache(addressList);

        // 初始化网络缓存
        networkStatCache.init(networkStat);

        // 确保epochRetryService中的各种属性处于当前块状态：当前共识和结算周期验证人、前一共识和结算周期验证人等
        epochRetryService.issueChange(BigInteger.valueOf(networkStat.getCurNumber()));
        epochRetryService.settlementChange(BigInteger.valueOf(networkStat.getCurNumber()));
        epochRetryService.consensusChange(BigInteger.valueOf(networkStat.getCurNumber()));

        // 检查gas price估算数据表
        GasEstimateLogExample condition = new GasEstimateLogExample();
        condition.setOrderByClause("seq asc");
        List<GasEstimateLog> gasEstimateLogs = gasEstimateLogMapper.selectByExample(condition);
        gasEstimateLogs.forEach(e -> {
            List<GasEstimate> estimates = JSON.parseArray(e.getJson(), GasEstimate.class);
            if (estimates != null && !estimates.isEmpty()) {
                gasEstimateEventPublisher.publish(e.getSeq(), estimates, traceId);
            }
        });

        return initialResult;
    }

    /**
     * 初始化入库内部质押节点
     *
     * @throws Exception
     */
    private List<com.platon.browser.dao.entity.Node> initInnerStake() throws BlockNumberException {
        log.info("初始化内置节点");
        epochRetryService.issueChange(BigInteger.ZERO);
        epochRetryService.settlementChange(BigInteger.ZERO);
        epochRetryService.consensusChange(BigInteger.ZERO);

        List<CustomNode> nodes = new ArrayList<>();
        List<CustomStaking> stakingList = new ArrayList<>();

        List<Node> validators = epochRetryService.getPreValidators();
        Set<String> validatorSet = new HashSet<>();
        validators.forEach(v -> validatorSet.add(v.getNodeId()));

        // 配置中的默认内置节点信息
        Map<String, CustomStaking> defaultStakingMap = new HashMap<>();
        chainConfig.getDefaultStakingList().forEach(staking -> defaultStakingMap.put(staking.getNodeId(), staking));

        List<Node> nodeList = epochRetryService.getPreVerifiers();
        for (int index = 0; index < nodeList.size(); index++) {
            Node v = nodeList.get(index);
            CustomStaking staking = new CustomStaking();
            staking.updateWithVerifier(v);
            staking.setStakingTxIndex(index);
            staking.setStakingReductionEpoch(BigInteger.ONE.intValue()); // 提前设置验证轮数
            staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
            staking.setIsInit(CustomStaking.YesNoEnum.YES.getCode());
            staking.setIsSettle(CustomStaking.YesNoEnum.YES.getCode());
            staking.setStakingLocked(chainConfig.getDefaultStakingLockedAmount());
            // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
            if (validatorSet.contains(v.getNodeId())) staking.setIsConsensus(CustomStaking.YesNoEnum.YES.getCode());

            if (StringUtils.isBlank(staking.getNodeName())) staking.setNodeName("platon.node." + (index + 1));

            // 更新年化率信息, 由于是周期开始，所以只记录成本，收益需要在结算周期切换时算
            AnnualizedRateInfo ari = new AnnualizedRateInfo();
            // |- 质押的成本
            List<PeriodValueElement> stakeCosts = new ArrayList<>();
            stakeCosts.add(new PeriodValueElement().setPeriod(0L).setValue(BigDecimal.ZERO));
            BigDecimal stakeCostVal = staking.getStakingLocked() // 锁定的质押金
                                             .add(staking.getStakingHes()) // 犹豫期的质押金
                                             .add(staking.getStatDelegateHes()) // 犹豫期的委托金
                                             .add(staking.getStatDelegateLocked()); // 锁定的委托金
            stakeCosts.add(new PeriodValueElement().setPeriod(1L).setValue(stakeCostVal));
            ari.setStakeCost(stakeCosts);
            // |- 委托的成本
            List<PeriodValueElement> delegateCosts = new ArrayList<>();
            delegateCosts.add(new PeriodValueElement().setPeriod(0L).setValue(BigDecimal.ZERO));
            BigDecimal delegateCostVal = staking.getStatDelegateLocked() // 锁定的委托金
                                                .add(staking.getStatDelegateHes()); // 犹豫期的委托金
            delegateCosts.add(new PeriodValueElement().setPeriod(1L).setValue(delegateCostVal));
            ari.setDelegateCost(delegateCosts);

            staking.setAnnualizedRateInfo(ari.toJSONString());
            staking.setPredictStakingReward(epochRetryService.getStakeReward());

            staking.setRewardPer(0);
            staking.setNextRewardPer(0); // 下一结算周期委托奖励比例
            staking.setNextRewardPerModEpoch(0); // 【下一结算周期委托奖励比例】修改所在结算周期
            staking.setDeleAnnualizedRate(0.0);
            staking.setPreDeleAnnualizedRate(0.0);
            staking.setHaveDeleReward(BigDecimal.ZERO);
            staking.setTotalDeleReward(BigDecimal.ZERO);
            staking.setExceptionStatus(1);

            BigInteger curSettleEpochRound = EpochUtil.getEpoch(BigInteger.ONE, chainConfig.getSettlePeriodBlockCount()); // 当前块所处的结算周期轮数
            // 更新解质押到账需要经过的结算周期数
            BigInteger unStakeFreezeDuration = stakeEpochService.getUnStakeFreeDuration();
            // 理论上的退出区块号, 实际的退出块号还要跟状态为进行中的提案的投票截至区块进行对比，取最大者
            BigInteger unStakeEndBlock = stakeEpochService.getUnStakeEndBlock("", curSettleEpochRound, false);
            staking.setUnStakeFreezeDuration(unStakeFreezeDuration.intValue());
            staking.setUnStakeEndBlock(unStakeEndBlock.longValue());
            staking.setLowRateSlashCount(0);
            staking.setZeroProduceFreezeDuration(0);
            staking.setZeroProduceFreezeEpoch(0);

            // 使用当前质押信息生成节点信息
            CustomNode node = new CustomNode();
            node.updateWithCustomStaking(staking);
            node.setStakingTxIndex(index);
            node.setTotalValue(staking.getStakingLocked());
            node.setIsRecommend(CustomNode.YesNoEnum.NO.getCode());
            node.setStatVerifierTime(BigInteger.ONE.intValue()); // 提前设置验证轮数
            node.setStatExpectBlockQty(epochRetryService.getExpectBlockCount()); // 期望出块数=共识周期块数/实际参与共识节点数
            node.setPreTotalDeleReward(BigDecimal.ZERO);

            nodes.add(node);
            stakingList.add(staking);
        }

        // 入库
        List<com.platon.browser.dao.entity.Node> returnData = new ArrayList<>(nodes);
        if (!nodes.isEmpty()) nodeMapper.batchInsert(returnData);
        if (!stakingList.isEmpty()) stakingMapper.batchInsert(new ArrayList<>(stakingList));
        return returnData;
    }

    /**
     * 初始化ES
     *
     * @param
     * @return void
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/19
     */
    private void initEs() {
        log.info("初始化ES");
        try {
            ESBlockRepository.initIndex();
            ESTransactionRepository.initIndex();
            ESDelegationRewardRepository.initIndex();
            ESNodeOptRepository.initIndex();
            esTransferTxRepository.initIndex();
            esErc20TxRepository.initIndex();
            esErc721TxRepository.initIndex();
        } catch (Exception e) {
            log.error("初始化ES异常", e);
        }

    }

}
