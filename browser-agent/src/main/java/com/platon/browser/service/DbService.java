package com.platon.browser.service;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.AddressCacheUpdater;
import com.platon.browser.engine.cache.StakingCacheUpdater;
import com.platon.browser.engine.stage.*;
import com.platon.browser.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 批量入库服务
 */
@Component
public class DbService {
    private static Logger logger = LoggerFactory.getLogger(DbService.class);

    @Autowired
    private BlockChain blockChain;
    @Autowired
    private AddressCacheUpdater addressCacheUpdater;
    @Autowired
    private StakingCacheUpdater stakingCacheUpdater;

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private RpPlanMapper rpPlanMapper;
    @Autowired
    private CustomNodeMapper customNodeMapper;
    @Autowired
    private CustomStakingMapper customStakingMapper;
    @Autowired
    private CustomDelegationMapper customDelegationMapper;
    @Autowired
    private CustomUnDelegationMapper customUnDelegationMapper;
    @Autowired
    private CustomNodeOptMapper customNodeOptMapper;
    @Autowired
    private CustomSlashMapper customSlashMapper;
    @Autowired
    private CustomProposalMapper customProposalMapper;
    @Autowired
    private CustomVoteMapper customVoteMapper;
    @Autowired
    private CustomAddressMapper customAddressMapper;
    @Autowired
    private CustomNetworkStatMapper customNetworkStatMapper;

    @Autowired
    private BlockCacheService blockCacheService;
    @Autowired
    private TransactionCacheService transactionCacheService;
    @Autowired
    private NetworkStatCacheService networkStatCacheService;

    @Transactional
    public void insertOrUpdate ( List <CustomBlock> basicData, BlockChainStage bizData ) {
        List <Block> blocks = new ArrayList <>();
        List <TransactionWithBLOBs> transactions = new ArrayList <>();
        basicData.forEach(block -> {
            blocks.add(block);
            transactions.addAll(block.getTransactionList());
        });
        // 批量入库区块数据并更新redis缓存
        if (blocks.size() > 0) {
            blockMapper.batchInsert(blocks);
            blockCacheService.update(new HashSet <>(blocks));
        }
        // 批量入库交易数据并更新redis缓存
        if (transactions.size() > 0) {
            transactionMapper.batchInsert(transactions);
            transactionCacheService.update(new HashSet <>(transactions));
        }
        // 统计数据入库并更新redis缓存
        NetworkStatStage nsr = bizData.getNetworkStatStage();
        Set<NetworkStat> networkStats = nsr.exportNetworkStat();
        if (networkStats.size() > 0) {
            customNetworkStatMapper.batchInsertOrUpdateSelective(networkStats, NetworkStat.Column.values());
            networkStatCacheService.update(networkStats);
        }
        // ****************批量新增或更新质押相关数据*******************
        StakingStage ser = bizData.getStakingStage();
        // 批量入库或更新节点数据
        Set<Node> nodes = ser.exportNode();
        if(nodes.size()>0) customNodeMapper.batchInsertOrUpdateSelective(nodes, Node.Column.values());
        // 批量入库或更新质押数据
        Set<Staking> stakings = ser.exportStaking();
        if(stakings.size()>0) customStakingMapper.batchInsertOrUpdateSelective(stakings, Staking.Column.values());
        // 批量入库或更新委托数据
        Set<Delegation> delegations = ser.exportDelegation();
        if(delegations.size()>0) customDelegationMapper.batchInsertOrUpdateSelective(delegations, Delegation.Column.values());
        // 批量入库或更新解委托数据
        Set<UnDelegation> unDelegations = ser.exportUnDelegation();
        if(unDelegations.size()>0) customUnDelegationMapper.batchInsertOrUpdateSelective(unDelegations, UnDelegation.Column.values());
        // 批量入库或更新惩罚数据
        Set<Slash> slashes = ser.exportSlash();
        if(slashes.size()>0) customSlashMapper.batchInsertOrUpdateSelective(slashes, Slash.Column.values());
        // 批量入库或更新节点操作数据
        Set<NodeOpt> nodeOpts = ser.exportNodeOpt();
        if(nodeOpts.size()>0) customNodeOptMapper.batchInsertOrUpdateSelective(nodeOpts, NodeOpt.Column.values());

        // ****************批量新增或更新提案相关数据*******************
        ProposalStage per = bizData.getProposalStage();
        // 批量入库或更新提案数据
        Set<Proposal> proposals = per.exportProposal();
        if(proposals.size()>0) customProposalMapper.batchInsertOrUpdateSelective(proposals, Proposal.Column.values());
        // 批量入库或更新投票数据
        Set<Vote> votes = per.exportVote();
        if(votes.size()>0) customVoteMapper.batchInsertOrUpdateSelective(votes, Vote.Column.values());

        // ****************批量新增或更新地址相关数据*******************
        AddressStage aer = bizData.getAddressStage();
        // 批量入库或更新投票数据
        Set<Address> addresses = aer.exportAddress();
        if(addresses.size()>0) customAddressMapper.batchInsertOrUpdateSelective(addresses, Address.Column.values());

        // ****************新增锁仓计划相关数据*******************
        RestrictingStage rs = bizData.getRestrictingStage();
        //批量插入锁仓计划
        Set<RpPlan> planSet = rs.exportRpPlan();
        if(planSet.size()>0)rpPlanMapper.batchInsert(new ArrayList <>(planSet));
    }

    public void batchSave(List<CustomBlock> basicData, BlockChainStage bizData) throws BusinessException {
        try{
            // 入库前更新统计信息
            addressCacheUpdater.updateAddressStatistics();
            stakingCacheUpdater.updateStakingStatistics();
            // 串行批量入库
            insertOrUpdate(basicData,bizData);
            blockChain.commitResult();
            // 缓存整理
            BlockChain.NODE_CACHE.sweep();
            BlockChain.PROPOSALS_CACHE.sweep();
        }catch (Exception e){
            throw new BusinessException("数据批量入库出错："+e.getMessage());
        }
    }
}
