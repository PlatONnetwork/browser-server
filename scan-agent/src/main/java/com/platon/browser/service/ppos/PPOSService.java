package com.platon.browser.service.ppos;

import java.util.ArrayList;
import java.util.List;

import com.platon.browser.analyzer.ppos.*;
import org.springframework.stereotype.Service;

import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.DelegateExitResult;
import com.platon.browser.bean.TxAnalyseResult;
import com.platon.browser.analyzer.ppos.RestrictingCreateAnalyzer;
import com.platon.browser.analyzer.ppos.ReportAnalyzer;
import com.platon.browser.analyzer.ppos.StakeCreateAnalyzer;
import com.platon.browser.analyzer.ppos.StakeExitAnalyzer;
import com.platon.browser.analyzer.ppos.StakeIncreaseAnalyzer;
import com.platon.browser.analyzer.ppos.StakeModifyAnalyzer;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @description: ppos服务
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class PPOSService {

    @Resource
    private StakeCreateAnalyzer stakeCreateAnalyzer;

    @Resource
    private StakeModifyAnalyzer stakeModifyAnalyzer;

    @Resource
    private StakeIncreaseAnalyzer stakeIncreaseAnalyzer;

    @Resource
    private StakeExitAnalyzer stakeExitAnalyzer;

    @Resource
    private ReportAnalyzer reportAnalyzer;

    @Resource
    private DelegateCreateAnalyzer delegateCreateAnalyzer;

    @Resource
    private DelegateExitAnalyzer delegateExitAnalyzer;

    @Resource
    private ProposalTextAnalyzer proposalTextAnalyzer;

    @Resource
    private ProposalUpgradeAnalyzer proposalUpgradeAnalyzer;

    @Resource
    private ProposalParameterAnalyzer proposalParameterAnalyzer;

    @Resource
    private ProposalCancelAnalyzer proposalCancelAnalyzer;

    @Resource
    private ProposalVoteAnalyzer proposalVoteAnalyzer;

    @Resource
    private VersionDeclareAnalyzer proposalVersionAnalyzer;

    @Resource
    private RestrictingCreateAnalyzer restrictingCreateAnalyzer;

    @Resource
    private DelegateRewardClaimAnalyzer delegateRewardClaimAnalyzer;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private AddressCache addressCache;

    // 前一个区块号
    private long preBlockNumber = 0L;

    /**
     * 解析交易, 构造业务入库参数信息
     *
     * @param event
     * @return
     */
    public TxAnalyseResult analyze(CollectionEvent event) {
        long startTime = System.currentTimeMillis();

        TxAnalyseResult tar = TxAnalyseResult.builder().nodeOptList(new ArrayList<>()).delegationRewardList(new ArrayList<>()).build();

        List<Transaction> transactions = event.getTransactions();

        if (event.getBlock().getNum() == 0) {
            return tar;
        }

        // 普通交易和虚拟PPOS交易统一设置seq排序序号： 区块号*100000+自增号(allTxCount)
        int allTxCount = 0;
        for (Transaction tx : transactions) {
            // 设置普通交易的交易序号
            tx.setSeq(event.getBlock().getNum() * 100000 + allTxCount);
            this.addressCache.update(tx);
            // 自增
            allTxCount++;
            // 分析真实交易
            this.analyzePPosTx(event, tx, tar);
            // 分析虚拟交易
            List<Transaction> virtualTxes = tx.getVirtualTransactions();
            for (Transaction vt : virtualTxes) {
                // 设置合约调用ppos交易的交易序号
                vt.setSeq(event.getBlock().getNum() * 100000 + allTxCount);
                switch (vt.getTypeEnum()) {
                    // 如果是提案交易，且交易是由普通合约内部调用触发的，则
                    // 所构造的虚拟交易HASH的格式是：<普通合约调用hash>-<合约内部ppos交易索引>
                    // 由于底层在合约内部执行多个提案时，只有一个可以成功，是唯一的
                    // 所以在把提案数据存储到platscan数据库中时，可以把虚拟提案交易的"-<合约内部ppos交易索引>" 去掉
                    // 防止外部在查询提案时找不到相应的交易信息（也就是说通过普通合约代理执行的提案，在浏览器中查看提案所在交易时，是跳到普通合约交易的）
                    case PROPOSAL_TEXT: // 2000
                    case PROPOSAL_UPGRADE: // 2001
                    case PROPOSAL_PARAMETER: // 2002
                    case PROPOSAL_CANCEL: // 2005
                        // case PROPOSAL_VOTE: // 2003 投票提案可以同时有多笔成功(实测)
                    case VERSION_DECLARE: // 2004
                        vt.setHash(vt.getHash().split("-")[0]);
                    default:
                        break;
                }
                this.analyzePPosTx(event, vt, tar);
                // 自增
                allTxCount++;
            }
        }

        Block block = event.getBlock();
        // 如果当前区块号与前一个一样，证明这是重复处理的块(例如:某部分业务处理失败，由于重试机制进来此处)
        // 防止重复计算
        if (block.getNum() == this.preBlockNumber) {
            return tar;
        }
        this.networkStatCache.updateByBlock(event.getBlock());
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        this.preBlockNumber = block.getNum();
        return tar;
    }

    /**
     * 分析真实交易
     *
     * @param event
     * @param tx
     * @param tar
     * @return void
     * @date 2021/6/3
     */
    private void analyzePPosTx(CollectionEvent event, Transaction tx, TxAnalyseResult tar) {
        try {
            log.info("解析真实交易[{}]类型为enum:[{}] code:[{}] desc:[{}]", tx.getHash(), tx.getTypeEnum(), tx.getTypeEnum().getCode(), tx.getTypeEnum().getDesc());
            // 调用交易分析引擎分析交易，以补充相关数据
            NodeOpt nodeOpt = null;
            DelegationReward delegationReward = null;
            switch (tx.getTypeEnum()) {
                case STAKE_CREATE:
                    nodeOpt = this.stakeCreateAnalyzer.analyze(event, tx);
                    break;
                case STAKE_MODIFY:
                    nodeOpt = this.stakeModifyAnalyzer.analyze(event, tx);
                    break;
                case STAKE_INCREASE:
                    nodeOpt = this.stakeIncreaseAnalyzer.analyze(event, tx);
                    break;
                case STAKE_EXIT:
                    nodeOpt = this.stakeExitAnalyzer.analyze(event, tx);
                    break;
                case DELEGATE_CREATE:
                    this.delegateCreateAnalyzer.analyze(event, tx);
                    break;
                case DELEGATE_EXIT:
                    DelegateExitResult der = this.delegateExitAnalyzer.analyze(event, tx);
                    delegationReward = der.getDelegationReward();
                    break;
                case PROPOSAL_TEXT:
                    nodeOpt = this.proposalTextAnalyzer.analyze(event, tx);
                    if (Transaction.StatusEnum.SUCCESS.getCode() == tx.getStatus()) {
                        tar.setProposalQty(tar.getProposalQty() + 1);
                    }
                    break;
                case PROPOSAL_UPGRADE:
                    nodeOpt = this.proposalUpgradeAnalyzer.analyze(event, tx);
                    if (Transaction.StatusEnum.SUCCESS.getCode() == tx.getStatus()) {
                        tar.setProposalQty(tar.getProposalQty() + 1);
                    }
                    break;
                case PROPOSAL_PARAMETER:
                    nodeOpt = this.proposalParameterAnalyzer.analyze(event, tx);
                    if (Transaction.StatusEnum.SUCCESS.getCode() == tx.getStatus()) {
                        tar.setProposalQty(tar.getProposalQty() + 1);
                    }
                    break;
                case PROPOSAL_CANCEL:
                    nodeOpt = this.proposalCancelAnalyzer.analyze(event, tx);
                    if (Transaction.StatusEnum.SUCCESS.getCode() == tx.getStatus()) {
                        tar.setProposalQty(tar.getProposalQty() + 1);
                    }
                    break;
                case PROPOSAL_VOTE:
                    nodeOpt = this.proposalVoteAnalyzer.analyze(event, tx);
                    break;
                case VERSION_DECLARE:
                    nodeOpt = this.proposalVersionAnalyzer.analyze(event, tx);
                    break;
                case REPORT:
                    nodeOpt = this.reportAnalyzer.analyze(event, tx);
                    break;
                case RESTRICTING_CREATE:
                    this.restrictingCreateAnalyzer.analyze(event, tx);
                    break;
                case CLAIM_REWARDS:
                    delegationReward = this.delegateRewardClaimAnalyzer.analyze(event, tx);
                    break;
                default:
                    break;
            }
            if (nodeOpt != null) {
                tar.getNodeOptList().add(nodeOpt);
            }
            if (delegationReward != null) {
                tar.getDelegationRewardList().add(delegationReward);
            }
        } catch (BusinessException | NoSuchBeanException | BlockNumberException e) {
            log.debug("", e);
        }
    }

}