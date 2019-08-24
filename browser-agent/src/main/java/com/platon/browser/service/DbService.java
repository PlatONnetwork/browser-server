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

        //质押统计数据补充
        dataOfStakingStatistics();

        //地址相关数据补充
        dataOfAddressStatistics();

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


    public void dataOfStakingStatistics () throws Exception{

        /**
         *  1.补充统计质押相关数据
         *      a.stat_delegate_has  关联的委托记录中犹豫期金额汇总
         *      b.stat_delegate_locked  关联的委托记录中锁定期金额汇总
         *      c.stat_delegate_reduction   关联的委托记录中退回中金额汇总
         *      d.stat_delegate_qty  关联的委托地址数
         */
        try {
            BlockChain.NODE_CACHE.getAllNode().forEach(node -> {
                for (Map.Entry <Long, CustomStaking> customStakingMap : node.getStakings().entrySet()) {
                    //只统计不为历史的委托数据
                    BigInteger statDelegateHas = BigInteger.ZERO;
                    BigInteger statDelegateLocked = BigInteger.ZERO;
                    BigInteger statDelegateReduction = BigInteger.ZERO;
                    BigInteger statDelegateQty = BigInteger.ZERO;
                    for (Map.Entry <String, CustomDelegation> customDelegationMap : customStakingMap.getValue().getDelegations().entrySet()) {
                        if (customDelegationMap.getValue().getIsHistory().equals(CustomDelegation.YesNoEnum.NO)) {
                            statDelegateHas = statDelegateHas.add(new BigInteger(customDelegationMap.getValue().getDelegateHas()));
                            statDelegateLocked = statDelegateLocked.add(new BigInteger(customDelegationMap.getValue().getDelegateLocked()));
                            statDelegateReduction = statDelegateReduction.add(new BigInteger(customDelegationMap.getValue().getDelegateReduction()));
                            statDelegateQty = statDelegateQty.add(BigInteger.ONE);
                        }
                    }
                    customStakingMap.getValue().setStatDelegateHas(statDelegateHas.toString());
                    customStakingMap.getValue().setStatDelegateLocked(statDelegateLocked.toString());
                    customStakingMap.getValue().setStatDelegateReduction(statDelegateReduction.toString());
                    customStakingMap.getValue().setStatDelegateQty(statDelegateQty.intValue());
                }
            });
        } catch (Exception e) {
            throw new Exception("[DbService]supply Address info exception on dataOfStakingStatistics()");
        }

    }

    public void dataOfAddressStatistics () throws BusinessException {

        /**
         * 2.补充统计地址相关数据
         *      a.staking_value  质押的金额
         *      b.delegate_value  委托的金额
         *      c.redeemed_value   赎回中的金额，包含委托和质押
         *      d.candidate_count   已委托的验证人
         *      e.delegate_hes   未锁定委托
         *      f.delegate_locked   已锁定委托
         *      g.delegate_unlock  已经解锁的
         *      h.delegate_reduction  赎回中的
         *
         */

        for (CustomAddress customAddress : BlockChain.ADDRESS_CACHE.getAllAddress()) {
            BigInteger stakingValue = BigInteger.ZERO;
            BigInteger delegateValue = BigInteger.ZERO;
            BigInteger statkingRedeemed = BigInteger.ZERO;
            BigInteger delegateReddemed = BigInteger.ZERO;
            BigInteger redeemedValue = BigInteger.ZERO;
            BigInteger candidateCount = BigInteger.ZERO;
            BigInteger delegateHes = BigInteger.ZERO;
            BigInteger delegateLocked = BigInteger.ZERO;
            BigInteger delegateUnlock = BigInteger.ZERO;
            BigInteger delegateReduction = BigInteger.ZERO;
            for (CustomStaking stakings : BlockChain.NODE_CACHE.getAllStaking()) {
                if (stakings.getStakingAddr().equals(customAddress.getAddress())) {
                    stakingValue = stakingValue.add(new BigInteger(stakings.getStakingHas()).add(new BigInteger(stakings.getStakingLocked())));
                    statkingRedeemed = statkingRedeemed.add(new BigInteger(stakings.getStakingReduction()));
                }

            }
            for (Delegation delegation : BlockChain.NODE_CACHE.getDelegationByIsHistory(CustomDelegation.YesNoEnum.NO)) {
                if (delegation.getDelegateAddr().equals(customAddress.getAddress())) {
                    delegateValue = delegateValue.add(new BigInteger(delegation.getDelegateHas()).add(new BigInteger(delegation.getDelegateLocked())));
                    delegateReddemed = delegateReddemed.add(new BigInteger(delegation.getDelegateReduction()));
                    delegateHes = delegateHes.add(new BigInteger(delegation.getDelegateHas()));
                    delegateLocked = delegateLocked.add(new BigInteger(delegation.getDelegateLocked()));
                    delegateReduction = delegateReduction.add(new BigInteger(delegation.getDelegateReduction()));
                    candidateCount = candidateCount.add(BigInteger.ONE);
                    Integer status = 0;
                    try {
                        status = BlockChain.NODE_CACHE.getNode(delegation.getNodeId()).getStakings().get(delegation.getStakingBlockNum()).getStatus();
                    } catch (NoSuchBeanException e) {
                        throw new BusinessException("[DbService]supply Address info exception on dataOfAddressStatistics()");
                    }
                    if (status.equals(CustomStaking.StatusEnum.EXITING.code) || status.equals(CustomStaking.StatusEnum.EXITED.code)) {
                        delegateUnlock = delegateUnlock.add(new BigInteger(delegation.getDelegateHas()));
                    }
                }
            }
            redeemedValue = statkingRedeemed.add(delegateReddemed);

            //address引用对象更新
            customAddress.setStakingValue(stakingValue.toString());
            customAddress.setDelegateValue(delegateValue.toString());
            customAddress.setRedeemedValue(redeemedValue.toString());
            customAddress.setCandidateCount(candidateCount.intValue());
            customAddress.setDelegateValue(delegateHes.toString());
            customAddress.setDelegateLocked(delegateLocked.toString());
            customAddress.setDelegateUnlock(delegateUnlock.toString());
            customAddress.setDelegateReduction(delegateReduction.toString());
        }
    }


}
