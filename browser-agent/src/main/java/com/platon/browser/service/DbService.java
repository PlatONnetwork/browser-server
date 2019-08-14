package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.BlockBean;
import com.platon.browser.engine.BlockChainResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * User: dongqile
 * Date: 2019/8/13
 * Time: 21:25
 */
@Component
public class DbService {

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private UnDelegationMapper unDelegationMapper;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private SlashMapper slashMapper;
    @Autowired
    private NodeOptMapper nodeOptMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Transactional
    public void insertOrUpdateChainInfo (List <BlockBean> basicData, BlockChainResult bizData ) throws Exception {
        List <Block> blocks = new ArrayList <>();
        List <TransactionWithBLOBs> transactions = new ArrayList <>();
        basicData.forEach(block -> {
            blocks.add(block);
            transactions.addAll(block.getTransactionList());
        });
        // 批量入库区块数据
        if (blocks.size() > 0) {
            blockMapper.batchInsert(blocks);
        }
        // 批量入库交易数据
        if (transactions.size() > 0) {
            transactionMapper.batchInsert(transactions);
        }
        //批量入库新增投票数据
        if (bizData.getProposalExecuteResult().getAddVotes().size() > 0) {
            voteMapper.batchInsert(bizData.getProposalExecuteResult().getAddVotes());
        }
        //批量入库新增治理数据
        if (bizData.getProposalExecuteResult().getAddProposals().size() > 0) {
            proposalMapper.batchInsert(new ArrayList <>(bizData.getProposalExecuteResult().getAddProposals()));
        }
        //批量入库更新治理数据
        if (bizData.getProposalExecuteResult().getUpdateProposals().size() > 0) {
            proposalMapper.batchInsertSelective(new ArrayList <>(bizData.getProposalExecuteResult().getUpdateProposals()));
        }
        //批量入库新增质押数据
        if (bizData.getStakingExecuteResult().getAddStakings().size() > 0) {
            stakingMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddStakings()));
        }
        //批量入库更新质押数据
        if (bizData.getStakingExecuteResult().getUpdateStakings().size() > 0) {
            stakingMapper.batchInsertSelective(new ArrayList <>(bizData.getStakingExecuteResult().getUpdateStakings()));
        }
        //批量入库新增委托数据
        if (bizData.getStakingExecuteResult().getAddDelegations().size() > 0) {
            delegationMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddDelegations()));
        }
        //批量入库更新委托数据
        if (bizData.getStakingExecuteResult().getUpdateDelegations().size() > 0) {
            delegationMapper.batchInsertSelective(new ArrayList <>(bizData.getStakingExecuteResult().getUpdateDelegations()));
        }
        //批量入库新增解委托数据
        if (bizData.getStakingExecuteResult().getAddUnDelegations().size() > 0) {
            unDelegationMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddUnDelegations()));
        }
        //批量入库更新解委托数据
        if (bizData.getStakingExecuteResult().getUpdateUnDelegations().size() > 0) {
            unDelegationMapper.batchInsertSelective(new ArrayList <>(bizData.getStakingExecuteResult().getUpdateUnDelegations()));
        }
        //批量入库新增节点数据
        if (bizData.getStakingExecuteResult().getAddNodes().size() > 0) {
            nodeMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddNodes()));
        }
        //批量入库更新节点数据
        if (bizData.getStakingExecuteResult().getUpdateNodes().size() > 0) {
            nodeMapper.batchInsertSelective(new ArrayList <>(bizData.getStakingExecuteResult().getUpdateNodes()));
        }
        //批量入库新增惩罚数据
        if (bizData.getStakingExecuteResult().getAddSlash().size() > 0) {
            slashMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddSlash()));
        }
        //批量入库新增操作数据
        if (bizData.getStakingExecuteResult().getAddNodeOpts().size() > 0) {
            nodeOptMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddNodeOpts()));
        }
    }

}
