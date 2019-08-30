package com.platon.browser.service;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.stage.*;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

/**
 * User: dongqile
 * Date: 2019/8/13
 * Time: 21:25
 */
@Component
public class DbService {
    private static Logger logger = LoggerFactory.getLogger(DbService.class);

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

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
    public void insertOrUpdate ( List <CustomBlock> basicData, BlockChainStage bizData ) throws Exception {
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
        if (nsr.getNetworkStatUpdateStage().size() > 0) {
            customNetworkStatMapper.batchInsertOrUpdateSelective(nsr.getNetworkStatUpdateStage(), NetworkStat.Column.values());
            networkStatCacheService.update(nsr.getNetworkStatUpdateStage());
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
    }
}
