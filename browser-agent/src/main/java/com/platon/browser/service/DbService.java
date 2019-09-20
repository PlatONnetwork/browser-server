package com.platon.browser.service;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.stage.*;
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
    	logger.debug("DbService insertOrUpdate");
        List <Block> blocks = new ArrayList <>();
        List <TransactionWithBLOBs> transactions = new ArrayList <>();
        basicData.forEach(block -> {
            blocks.add(block);
            transactions.addAll(block.getTransactionList());
        });
        // 批量入库区块数据并更新redis缓存
        if (!blocks.isEmpty()) {
            blockMapper.batchInsert(blocks);
            blockCacheService.update(new HashSet <>(blocks));
        }
        // 批量入库交易数据并更新redis缓存
        if (!transactions.isEmpty()) {
            transactionMapper.batchInsert(transactions);
            transactionCacheService.update(new HashSet <>(transactions));
        }
        // 统计数据入库并更新redis缓存
        NetworkStatStage nsr = bizData.getNetworkStatStage();
        Set<NetworkStat> networkStats = nsr.exportNetworkStat();
        if (!networkStats.isEmpty()) {
            customNetworkStatMapper.batchInsertOrUpdateSelective(networkStats, NetworkStat.Column.values());
            networkStatCacheService.update(networkStats);
        }
        // ****************批量新增或更新质押相关数据*******************
        StakingStage ser = bizData.getStakingStage();
        // 批量入库或更新节点数据
        Set<Node> nodes = ser.exportNode();
        if(!nodes.isEmpty()) customNodeMapper.batchInsertOrUpdateSelective(nodes, Node.Column.values());
        // 批量入库或更新质押数据
        Set<Staking> stakings = ser.exportStaking();
        if(!stakings.isEmpty()) customStakingMapper.batchInsertOrUpdateSelective(stakings, Staking.Column.values());
        // 批量入库或更新委托数据
        Set<Delegation> delegations = ser.exportDelegation();
        if(!delegations.isEmpty()) customDelegationMapper.batchInsertOrUpdateSelective(delegations, Delegation.Column.values());
        // 批量入库或更新解委托数据
        Set<UnDelegation> unDelegations = ser.exportUnDelegation();
        if(!unDelegations.isEmpty()) customUnDelegationMapper.batchInsertOrUpdateSelective(unDelegations, UnDelegation.Column.values());
        // 批量入库或更新惩罚数据
        Set<Slash> slashes = ser.exportSlash();
        if(!slashes.isEmpty()) customSlashMapper.batchInsertOrUpdateSelective(slashes, Slash.Column.values());
        // 批量入库或更新节点操作数据
        Set<NodeOpt> nodeOpts = ser.exportNodeOpt();
        if(!nodeOpts.isEmpty()) customNodeOptMapper.batchInsertOrUpdateSelective(nodeOpts, NodeOpt.Column.values());

        // ****************批量新增或更新提案相关数据*******************
        ProposalStage per = bizData.getProposalStage();
        // 批量入库或更新提案数据
        Set<Proposal> proposals = per.exportProposal();
        if(!proposals.isEmpty()) customProposalMapper.batchInsertOrUpdateSelective(proposals, Proposal.Column.values());
        // 批量入库或更新投票数据
        Set<Vote> votes = per.exportVote();
        if(!votes.isEmpty()) customVoteMapper.batchInsertOrUpdateSelective(votes, Vote.Column.values());

        // ****************批量新增或更新地址相关数据*******************
        AddressStage aer = bizData.getAddressStage();
        // 批量入库或更新投票数据
        Set<Address> addresses = aer.exportAddress();
        if(!addresses.isEmpty()) customAddressMapper.batchInsertOrUpdateSelective(addresses, Address.Column.values());

        // ****************新增锁仓计划相关数据*******************
        RestrictingStage rs = bizData.getRestrictingStage();
        //批量插入锁仓计划
        Set<RpPlan> planSet = rs.exportRpPlan();
        if(!planSet.isEmpty())rpPlanMapper.batchInsert(new ArrayList <>(planSet));
    }
}
