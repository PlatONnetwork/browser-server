//package com.platon.browser.persistence.service;
//
//import com.platon.browser.dao.entity.*;
//import com.platon.browser.dao.mapper.*;
//import com.platon.browser.dto.CustomBlock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// *
// * @Auther: dongqile
// * @Date: 2019/8/17 20:09
// * @Description: 批量入库服务
// */
////@Component
//public class DbService {
//    private static Logger logger = LoggerFactory.getLogger(DbService.class);
//
//    @Autowired
//    private BlockMapper blockMapper;
//    @Autowired
//    private TransactionMapper transactionMapper;
//    @Autowired
//    private RpPlanMapper rpPlanMapper;
//    @Autowired
//    private CustomNodeMapper customNodeMapper;
//    @Autowired
//    private CustomStakingMapper customStakingMapper;
//    @Autowired
//    private CustomDelegationMapper customDelegationMapper;
//    @Autowired
//    private CustomUnDelegationMapper customUnDelegationMapper;
//    @Autowired
//    private CustomNodeOptMapper customNodeOptMapper;
//    @Autowired
//    private CustomSlashMapper customSlashMapper;
//    @Autowired
//    private CustomProposalMapper customProposalMapper;
//    @Autowired
//    private CustomVoteMapper customVoteMapper;
//    @Autowired
//    private CustomAddressMapper customAddressMapper;
//    @Autowired
//    private CustomNetworkStatMapper customNetworkStatMapper;
//
//    public static class CacheContainer{
//        public final List<Block> blocks = new ArrayList<>();
//        public final List<TransactionWithBLOBs> transactions = new ArrayList<>();
//        public final Set<NetworkStat> networkStats=new HashSet<>();
//        public void clear(){
//            blocks.clear();
//            transactions.clear();
//            networkStats.clear();
//        }
//    }
//    private CacheContainer cacheContainer = new CacheContainer();
//    @Transactional
//    public CacheContainer batchInsertOrUpdate ( List <CustomBlock> basicData, BlockChainStage bizData ) {
//        cacheContainer.clear();
//    	logger.debug("DbService insertOrUpdate");
//        basicData.forEach(block -> {
//            cacheContainer.blocks.add(block);
//            cacheContainer.transactions.addAll(block.getTransactionList());
//            // 区块交易列表设置为空，防止redis缓存数据剧增
//            block.setTransactionList(null);
//        });
//        // 批量入库区块数据并更新redis缓存
//        if (!cacheContainer.blocks.isEmpty()) {
//            blockMapper.batchInsert(cacheContainer.blocks);
//        }
//        // 批量入库交易数据并更新redis缓存
//        if (!cacheContainer.transactions.isEmpty()) {
//            transactionMapper.batchInsert(cacheContainer.transactions);
//        }
//        // 统计数据入库并更新redis缓存
//        NetworkStatStage nsr = bizData.getNetworkStatStage();
//        Set<NetworkStat> networkStats = nsr.exportNetworkStat();
//        if (!networkStats.isEmpty()) {
//            customNetworkStatMapper.batchInsertOrUpdateSelective(networkStats, NetworkStat.Column.values());
//            cacheContainer.networkStats.addAll(networkStats);
//        }
//        // ****************批量新增或更新质押相关数据*******************
//        StakingStage ser = bizData.getStakingStage();
//        // 批量入库或更新节点数据
//        Set<Node> nodes = ser.exportNodeSet();
//        if(!nodes.isEmpty()) customNodeMapper.batchInsertOrUpdateSelective(nodes, Node.Column.values());
//        // 批量入库或更新质押数据
//        Set<Staking> stakings = ser.exportStakingSet();
//        if(!stakings.isEmpty()) customStakingMapper.batchInsertOrUpdateSelective(stakings, Staking.Column.values());
//        // 批量入库或更新委托数据
//        Set<Delegation> delegations = ser.exportDelegationSet();
//        if(!delegations.isEmpty()) customDelegationMapper.batchInsertOrUpdateSelective(delegations, Delegation.Column.values());
//        // 批量入库或更新解委托数据
//        Set<UnDelegation> unDelegations = ser.exportUnDelegationSet();
//        if(!unDelegations.isEmpty()) customUnDelegationMapper.batchInsertOrUpdateSelective(unDelegations, UnDelegation.Column.values());
//        // 批量入库或更新惩罚数据
//        Set<Slash> slashes = ser.exportSlashSet();
//        if(!slashes.isEmpty()) customSlashMapper.batchInsertOrUpdateSelective(slashes, Slash.Column.values());
//        // 批量入库或更新节点操作数据
//        Set<NodeOpt> nodeOpts = ser.exportNodeOptSet();
//        if(!nodeOpts.isEmpty()) customNodeOptMapper.batchInsertOrUpdateSelective(nodeOpts, NodeOpt.Column.values());
//
//        // ****************批量新增或更新提案相关数据*******************
//        ProposalStage per = bizData.getProposalStage();
//        // 批量入库或更新提案数据
//        Set<Proposal> proposals = per.exportProposalSet();
//        if(!proposals.isEmpty()) customProposalMapper.batchInsertOrUpdateSelective(proposals, Proposal.Column.values());
//        // 批量入库或更新投票数据
//        Set<Vote> votes = per.exportVoteSet();
//        if(!votes.isEmpty()) customVoteMapper.batchInsertOrUpdateSelective(votes, Vote.Column.values());
//
//        // ****************批量新增或更新地址相关数据*******************
//        AddressStage aer = bizData.getAddressStage();
//        // 批量入库或更新投票数据
//        Set<Address> addresses = aer.exportAddressSet();
//        if(!addresses.isEmpty()) customAddressMapper.batchInsertOrUpdateSelective(addresses, Address.Column.values());
//
//        // ****************新增锁仓计划相关数据*******************
//        RestrictingStage rs = bizData.getRestrictingStage();
//        //批量插入锁仓计划
//        Set<RpPlan> planSet = rs.exportRpPlanSet();
//        if(!planSet.isEmpty())rpPlanMapper.batchInsert(new ArrayList <>(planSet));
//
//        return cacheContainer;
//    }
//}
