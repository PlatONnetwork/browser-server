package com.platon.browser.service;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.result.AddressExecuteResult;
import com.platon.browser.engine.result.BlockChainResult;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.engine.result.StakingExecuteResult;
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

    @Transactional
    public void insertOrUpdateChainInfo (List <CustomBlock> basicData, BlockChainResult bizData ) throws Exception {
        List <Block> blocks = new ArrayList <>();
        List <TransactionWithBLOBs> transactions = new ArrayList <>();
        basicData.forEach(block -> {
            blocks.add(block);
            transactions.addAll(block.getTransactionList());
        });
        // 批量入库区块数据
        if (blocks.size() > 0) blockMapper.batchInsert(blocks);
        // 批量入库交易数据
        if (transactions.size() > 0)  transactionMapper.batchInsert(transactions);

        // 质押相关数据
        StakingExecuteResult ser = bizData.getStakingExecuteResult();
        // 提案相关数据
        ProposalExecuteResult per = bizData.getProposalExecuteResult();
        // 地址相关数据
        AddressExecuteResult aer = bizData.getAddressExecuteResult();

        /*****************************批量新增操作 START**************************/

        // ****************新增节点质押相关数据*******************
        //批量入库新增节点数据
        if (ser.getAddNodes().size() > 0) customNodeMapper.batchInsertOrUpdateSelective(ser.getAddNodes(),Node.Column.values());
        //批量入库新增质押数据
        if (ser.getAddStakings().size() > 0) customStakingMapper.batchInsertOrUpdateSelective(ser.getAddStakings(), Staking.Column.values());
        //批量入库新增委托数据
        if (ser.getAddDelegations().size() > 0) customDelegationMapper.batchInsertOrUpdateSelective(ser.getAddDelegations(), Delegation.Column.values());
        //批量入库新增解委托数据
        if (ser.getAddUnDelegations().size() > 0) customUnDelegationMapper.batchInsertOrUpdateSelective(ser.getAddUnDelegations(),UnDelegation.Column.values());
        //批量入库新增惩罚数据
        if (ser.getAddSlashs().size() > 0) customSlashMapper.batchInsertOrUpdateSelective(ser.getAddSlashs(),Slash.Column.values());
        //批量入库新增操作数据
        if (ser.getAddNodeOpts().size() > 0) customNodeOptMapper.batchInsertOrUpdateSelective(ser.getAddNodeOpts(),NodeOpt.Column.values());

        // ****************新增提案相关数据*******************
        //批量入库新增治理数据
        if (per.getAddProposals().size() > 0) customProposalMapper.batchInsertOrUpdateSelective(per.getAddProposals(),Proposal.Column.values());
        //批量入库新增投票数据
        if (per.getAddVotes().size() > 0) customVoteMapper.batchInsertOrUpdateSelective(per.getAddVotes(),Vote.Column.values());

        // ****************新增地址相关数据*******************
        //批量入库地址新增数据
        if(aer.getAddAddress().size()>0) customAddressMapper.batchInsertOrUpdateSelective(aer.getAddAddress(),Address.Column.values());
        //批量入库统计数据
        /*****************************批量新增操作 END**************************/


        /*****************************批量更新操作 START**************************/
        // ****************更新质押相关数据*******************
        //批量入库或更新节点数据
        if (ser.getUpdateNodes().size() > 0) customNodeMapper.batchInsertOrUpdateSelective(ser.getUpdateNodes(),Node.Column.values());
        //批量入库更新质押数据
        if (ser.getUpdateStakings().size() > 0) customStakingMapper.batchInsertOrUpdateSelective(ser.getUpdateStakings(),Staking.Column.values());
        //批量入库更新委托数据
        if (ser.getUpdateDelegations().size() > 0) customDelegationMapper.batchInsertOrUpdateSelective(ser.getUpdateDelegations(),Delegation.Column.values());
        //批量入库更新解委托数据
        if (ser.getUpdateUnDelegations().size() > 0) customUnDelegationMapper.batchInsertOrUpdateSelective(ser.getUpdateUnDelegations(),UnDelegation.Column.values());

        // ****************更新提案相关数据*******************
        //批量更新提案
        if (per.getUpdateProposals().size() > 0) customProposalMapper.batchInsertOrUpdateSelective(per.getUpdateProposals(),Proposal.Column.values());

        // ****************更新地址相关数据*******************
        //批量更新地址
        if(aer.getUpdateAddress().size()>0) customAddressMapper.batchInsertOrUpdateSelective(aer.getUpdateAddress(),Address.Column.values());
        /*****************************批量更新操作 END**************************/
    }
}
