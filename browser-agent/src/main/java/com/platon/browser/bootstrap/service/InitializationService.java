package com.platon.browser.bootstrap.service;


import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.dto.AnnualizedRateInfo;
import com.platon.browser.common.complement.dto.PeriodValueElement;
import com.platon.browser.common.service.epoch.EpochRetryService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.utils.HexTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.platon.bean.Node;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.*;

/**
 * @description: 启动初始化服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class InitializationService {
    private static final InitializationResult initialResult = new InitializationResult();

    @Autowired
    private EpochRetryService epochRetryService;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private NodeCache nodeCache;
    @Autowired
    private NetworkStatCache networkStatCache;

    @Transactional
    public InitializationResult init() throws Exception {
        // 检查数据库network_stat表,如果没有记录则添加一条,并从链上查询最新内置验证人节点入库至staking表和node表
        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        if(networkStat==null){
            // 创建新的统计记录
            networkStat = CollectionNetworkStat.newInstance();
            networkStat.setId(1);
            networkStat.setCurNumber(0L);
            networkStatMapper.insert(networkStat);
            initialResult.setCollectedBlockNumber(0L);
            // 删除节点表和质押表、地址表数据
            nodeMapper.deleteByExample(null);
            stakingMapper.deleteByExample(null);
            addressMapper.deleteByExample(null);
            // 初始化内置节点
            List<com.platon.browser.dao.entity.Node> nodeList = initInnerStake();
            // 初始化节点缓存
            nodeCache.init(nodeList);
            // 初始化网络缓存
            networkStatCache.init(networkStat);
            return initialResult;
        }

        initialResult.setCollectedBlockNumber(networkStat.getCurNumber());

        // 初始化节点缓存
        List<com.platon.browser.dao.entity.Node> nodeList = nodeMapper.selectByExample(null);
        nodeCache.init(nodeList);
        // 初始化网络缓存
        networkStatCache.init(networkStat);
        return initialResult;
    }

    /**
     * 初始化入库内部质押节点
     * @throws Exception
     */
    private List<com.platon.browser.dao.entity.Node> initInnerStake() throws Exception {
        epochRetryService.issueChange(BigInteger.ZERO);

        List<CustomNode> nodes = new ArrayList<>();
        List<CustomStaking> stakingList = new ArrayList<>();

        List<Node> validators = epochRetryService.getPreValidators();
        Set<String> validatorSet = new HashSet<>();
        validators.forEach(v->validatorSet.add(v.getNodeId()));

        // 查询所有候选人
        Map<String,Node> candidateMap = new HashMap<>();
        List<Node> candidates = epochRetryService.getCandidates();
        candidates.forEach(node->candidateMap.put(HexTool.prefix(node.getNodeId()),node));

        // 配置中的默认内置节点信息
        Map<String,CustomStaking> defaultStakingMap = new HashMap<>();
        chainConfig.getDefaultStakings().forEach(staking -> defaultStakingMap.put(staking.getNodeId(),staking));

        epochRetryService.getPreVerifiers().forEach(v->{
            CustomStaking staking = new CustomStaking();
            staking.updateWithVerifier(v);
            staking.setStakingReductionEpoch(BigInteger.ONE.intValue()); // 提前设置验证轮数
            staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
            staking.setIsInit(CustomStaking.YesNoEnum.YES.getCode());
            staking.setIsSettle(CustomStaking.YesNoEnum.YES.getCode());
            staking.setStakingLocked(Convert.toVon(chainConfig.getDefaultStakingLockedAmount(), Convert.Unit.LAT));
            // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
            if(validatorSet.contains(v.getNodeId())) staking.setIsConsensus(CustomStaking.YesNoEnum.YES.getCode());

            // 使用实时候选人信息更新质押
            Node candidate = candidateMap.get(v.getNodeId());
            if(candidate!=null) {
                staking.updateWithCandidate(candidate);
            }

            // 使用配置文件中的信息更新质押
            CustomStaking defaultStaking = defaultStakingMap.get(staking.getNodeId());
            if(StringUtils.isBlank(staking.getNodeName())&&defaultStaking!=null){
                staking.setNodeName(defaultStaking.getNodeName());
            }

            // 更新年化率信息, 由于是周期开始，所以只记录成本，收益需要在结算周期切换时算
            PeriodValueElement pve = PeriodValueElement.builder()
                    .period(1L)
                    .value(staking.getStakingLocked())
                    .build();
            AnnualizedRateInfo ari = AnnualizedRateInfo.builder()
                    .cost(Collections.singletonList(pve))
                    .build();
            staking.setAnnualizedRateInfo(ari.toJSONString());

            // 使用当前质押信息生成节点信息
            CustomNode node = new CustomNode();
            node.updateWithCustomStaking(staking);
            node.setTotalValue(staking.getStakingLocked());
            node.setIsRecommend(CustomNode.YesNoEnum.NO.getCode());
            node.setStatVerifierTime(BigInteger.ONE.intValue()); // 提前设置验证轮数
            node.setStatExpectBlockQty(epochRetryService.getExpectBlockCount()); // 期望出块数=共识周期块数/实际参与共识节点数

            nodes.add(node);
            stakingList.add(staking);
        });

        // 入库
        List<com.platon.browser.dao.entity.Node> returnData = new ArrayList<>(nodes);
        if(!nodes.isEmpty()) nodeMapper.batchInsert(returnData);
        if(!stakingList.isEmpty()) stakingMapper.batchInsert(new ArrayList<>(stakingList));
        return returnData;
    }
}
