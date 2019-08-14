package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.BlockInfo;
import com.platon.browser.engine.BlockChainResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public void insertOrUpdateChainInfo ( List <BlockInfo> basicData, BlockChainResult bizData )throws Exception {
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
        if(bizData.getStakingExecuteResult().getUpdateStakings().size()>0){
            stakingMapper.batchInsertSelective(new ArrayList <>(bizData.getStakingExecuteResult().getUpdateStakings()));
        }
        //批量入库新增委托数据
        if(bizData.getStakingExecuteResult().getAddDelegations().size()>0){
            delegationMapper.batchInsert(new ArrayList <>(bizData.getStakingExecuteResult().getAddDelegations()));
        }
        //批量入库更新委托数据

    }

}