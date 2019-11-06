package com.platon.browser.bootstrap.service;


import com.alibaba.fastjson.JSON;
import com.platon.browser.bootstrap.bean.InitializationResult;
import com.platon.browser.bootstrap.exception.InitializationException;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.common.complement.bean.AnnualizedRateInfo;
import com.platon.browser.common.complement.bean.PeriodValueElement;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.service.epoch.EpochRetryService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.util.VerUtil;
import com.platon.browser.utils.HexTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.platon.bean.Node;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
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
    private NodeCache nodeCache;

    @Transactional
    public InitializationResult init() throws Exception {
        // 检查数据库network_stat表,如果没有记录则添加一条,并从链上查询最新内置验证人节点入库至staking表和node表

        List<NetworkStat> networkStatList = networkStatMapper.selectByExample(null);
        if(networkStatList.isEmpty()){
            // 创建新的统计记录
            NetworkStat networkStat = CollectionNetworkStat.newInstance();
            networkStat.setCurNumber(1L);
            networkStatMapper.insert(networkStat);
            initialResult.setCollectedBlockNumber(0L);

            nodeMapper.deleteByExample(null);
            stakingMapper.deleteByExample(null);

            List<Staking> stakings = initInnerStake();
            initNodeCache(stakings);
            return initialResult;
        }

        if(networkStatList.size()>1) throw new InitializationException("启动自检出错:network_stat表存在多条网络统计状态数据!");
        NetworkStat networkStat = networkStatList.get(0);
        initialResult.setCollectedBlockNumber(networkStat.getCurNumber());

        StakingExample stakingExample = new StakingExample();
        stakingExample.createCriteria().andStatusEqualTo(CustomStaking.StatusEnum.CANDIDATE.getCode());
        List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
        initNodeCache(stakings);
        return initialResult;
    }

    private void initNodeCache(List<Staking> stakings){
        stakings.forEach(s->{
            NodeItem node = NodeItem.builder()
                    .nodeId(s.getNodeId())
                    .nodeName(s.getNodeName())
                    .annualizedRateInfo(JSON.parseObject(s.getAnnualizedRateInfo(),AnnualizedRateInfo.class))
                    .stakingBlockNum(BigInteger.valueOf(s.getStakingBlockNum()))
                    .stakingTxIndex(s.getStakingTxIndex())
                    .build();
            nodeCache.addNode(node);
        });
    }

    /**
     * 初始化入库内部质押节点
     * @throws Exception
     */
    private List<Staking> initInnerStake() throws Exception {
        epochRetryService.issueChange(BigInteger.ZERO);

        List<CustomNode> nodes = new ArrayList<>();
        List<CustomStaking> stakings = new ArrayList<>();

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
            Node candidate = candidateMap.get(v.getNodeId());

            CustomStaking staking = new CustomStaking();
            staking.updateWithNode(v);
            staking.setStakingReductionEpoch(BigInteger.ONE.intValue()); // 提前设置验证轮数
            staking.setIsInit(CustomStaking.YesNoEnum.YES.getCode());
            staking.setIsSettle(CustomStaking.YesNoEnum.YES.getCode());
            // 内置节点默认设置状态为1
            staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
            // 设置内置节点质押锁定金额
            BigDecimal initStakingLocked = Convert.toVon(chainConfig.getDefaultStakingLockedAmount(), Convert.Unit.LAT);
            staking.setStakingLocked(initStakingLocked);
            // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
            if(validatorSet.contains(v.getNodeId())) staking.setIsConsensus(CustomStaking.YesNoEnum.YES.getCode());


            // 使用实时候选人信息更新质押
            if(candidate!=null) {
                String nodeName = candidate.getNodeName();
                String programVersion=candidate.getProgramVersion().toString();
                BigInteger bigVersion = VerUtil.transferBigVersion(candidate.getProgramVersion());
                staking.setNodeName(nodeName);
                staking.setProgramVersion(programVersion);
                staking.setBigVersion(bigVersion.toString());
                staking.setExternalId(candidate.getExternalId());
                staking.setBenefitAddr(candidate.getBenifitAddress());
                staking.setDetails(candidate.getDetails());
                staking.setWebSite(candidate.getWebsite());
            }

            // 使用配置文件中的信息更新质押
            CustomStaking defaultStaking = defaultStakingMap.get(staking.getNodeId());
            if(StringUtils.isBlank(staking.getNodeName())&&defaultStaking!=null)
                staking.setNodeName(defaultStaking.getNodeName());

            // 记录年化率信息, 由于是周期开始，所以只记录成本，收益需要在结算周期切换时算
            PeriodValueElement pve = PeriodValueElement.builder()
                    .period(1L)
                    .value(staking.getStakingLocked())
                    .build();
            AnnualizedRateInfo ari = AnnualizedRateInfo.builder()
                    .cost(Collections.singletonList(pve))
                    .build();
            staking.setAnnualizedRateInfo(ari.toJSONString());


            CustomNode node = new CustomNode();
            node.updateWithNode(v);
            node.setTotalValue(BigDecimal.ZERO);
            node.setIsRecommend(CustomNode.YesNoEnum.NO.getCode());
            node.setStatVerifierTime(BigInteger.ONE.intValue()); // 提前设置验证轮数
            node.setStatExpectBlockQty(epochRetryService.getExpectBlockCount()); // 期望出块数=共识周期块数/实际参与共识节点数

            BeanUtils.copyProperties(staking,node);
            nodes.add(node);
            stakings.add(staking);
        });

        // 入库
        if(!nodes.isEmpty()) nodeMapper.batchInsert(new ArrayList<>(nodes));
        List<Staking> returnData = new ArrayList<>(stakings);
        if(!stakings.isEmpty()) stakingMapper.batchInsert(returnData);
        return new ArrayList<>(stakings);
    }
}
