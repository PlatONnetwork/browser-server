package com.platon.browser.complement.service;

import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.delegate.DelegateCreateConverter;
import com.platon.browser.complement.converter.delegate.DelegateExitConverter;
import com.platon.browser.complement.converter.proposal.*;
import com.platon.browser.complement.converter.restricting.RestrictingCreateConverter;
import com.platon.browser.complement.converter.slash.ReportConverter;
import com.platon.browser.complement.converter.stake.StakeCreateConverter;
import com.platon.browser.complement.converter.stake.StakeExitConverter;
import com.platon.browser.complement.converter.stake.StakeIncreaseConverter;
import com.platon.browser.complement.converter.stake.StakeModifyConverter;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @description: 业务入库参数服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class TransactionParameterService {
    @Autowired
    private StakeCreateConverter stakeCreateConverter;
    @Autowired
    private StakeModifyConverter stakeModifyConverter;
    @Autowired
    private StakeIncreaseConverter stakeIncreaseConverter;
    @Autowired
    private StakeExitConverter stakeExitConverter;
    @Autowired
    private ReportConverter reportConverter;
    @Autowired
    private DelegateCreateConverter delegateCreateConverter;
    @Autowired
    private DelegateExitConverter delegateExitConverter;
    @Autowired
    private ProposalTextConverter proposalTextConverter;
    @Autowired
    private ProposalUpgradeConverter proposalUpgradeConverter;
    @Autowired
    private ProposalCancelConverter proposalCancelConverter;
    @Autowired
    private ProposalVoteConverter proposalVoteConverter;
    @Autowired
    private VersionDeclareConverter proposalVersionConverter;
    @Autowired
    private RestrictingCreateConverter restrictingCreateConverter;
    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private AddressCache addressCache;

    /**
     * 解析交易, 构造业务入库参数信息
     * @param event
     * @return
     */
    public List<NodeOpt> getParameters(CollectionEvent event) throws Exception{
        long startTime = System.currentTimeMillis();

        List<Transaction> transactions = event.getTransactions();
        List<NodeOpt> nodeOptList = new ArrayList<>();

        if(event.getBlock().getNum()==0) return nodeOptList;
        
        int txQty = transactions.size();
        int proposalQty = 0;

        for (Transaction tx : transactions) {
        	addressCache.update(tx);
            try{
                // 调用交易分析引擎分析交易，以补充相关数据
                Optional<NodeOpt> nodeOpt = Optional.ofNullable(null);
                switch (tx.getTypeEnum()) {
                    case STAKE_CREATE: // 1000 创建验证人
                    	nodeOpt = stakeCreateConverter.convert(event,tx);
                        break;
                    case STAKE_MODIFY: // 1001 编辑验证人
                    	nodeOpt = stakeModifyConverter.convert(event,tx);
                        break;
                    case STAKE_INCREASE: // 1002 增持质押
                    	nodeOpt = stakeIncreaseConverter.convert(event,tx);
                        break;
                    case STAKE_EXIT: // 1003 退出质押
                    	nodeOpt= stakeExitConverter.convert(event,tx);
                        break;
                    case DELEGATE_CREATE: // 1004
                    	delegateCreateConverter.convert(event,tx);
                        break;
                    case DELEGATE_EXIT: // 1005
                    	delegateExitConverter.convert(event,tx);
                        break;
                    case PROPOSAL_TEXT: // 2000
                    	nodeOpt = proposalTextConverter.convert(event,tx);
                    	if( Transaction.StatusEnum.SUCCESS.getCode()==tx.getStatus()) {
                    		proposalQty++;
                    	} 
                        break;
                    case PROPOSAL_UPGRADE: // 2001
                    	nodeOpt = proposalUpgradeConverter.convert(event,tx);
                     	if( Transaction.StatusEnum.SUCCESS.getCode()==tx.getStatus()) {
                    		proposalQty++;
                    	} 
                        break;
                    case PROPOSAL_CANCEL: // 2005
                    	nodeOpt = proposalCancelConverter.convert(event,tx);
                     	if( Transaction.StatusEnum.SUCCESS.getCode()==tx.getStatus()) {
                    		proposalQty++;
                    	} 
                        break;
                    case PROPOSAL_VOTE: // 2003
                     	nodeOpt = proposalVoteConverter.convert(event,tx);
                        break;
                    case VERSION_DECLARE: // 2004
                    	nodeOpt = proposalVersionConverter.convert(event,tx);
                        break;
                    case REPORT: // 3000
                    	nodeOpt = reportConverter.convert(event,tx);
                        break;
                    case RESTRICTING_CREATE: // 4000
                    	restrictingCreateConverter.convert(event,tx);
                        break;
                    default:
                        break;
                }
                
                nodeOpt.ifPresent(nodeOptList::add);
                
            }catch (BusinessException e){
                log.debug("",e);
            }
           
        }
        
        networkStatCache.updateByBlock(txQty, proposalQty, event.getBlock().getTime(), event.getBlock().getHash());

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return nodeOptList;
    }
}