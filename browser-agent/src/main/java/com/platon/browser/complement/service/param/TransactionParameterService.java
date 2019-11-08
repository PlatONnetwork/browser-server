package com.platon.browser.complement.service.param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.delegate.DelegateCreate;
import com.platon.browser.common.complement.param.delegate.DelegateExit;
import com.platon.browser.common.complement.param.proposal.ProposalCancel;
import com.platon.browser.common.complement.param.proposal.ProposalText;
import com.platon.browser.common.complement.param.proposal.ProposalUpgrade;
import com.platon.browser.common.complement.param.proposal.ProposalVersion;
import com.platon.browser.common.complement.param.proposal.ProposalVote;
import com.platon.browser.common.complement.param.restricting.RestrictingCreate;
import com.platon.browser.common.complement.param.slash.Report;
import com.platon.browser.common.complement.param.stake.StakeExit;
import com.platon.browser.common.complement.param.stake.StakeIncrease;
import com.platon.browser.common.complement.param.stake.StakeModify;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.service.param.converter.DelegateCreateConverter;
import com.platon.browser.complement.service.param.converter.DelegateExitConverter;
import com.platon.browser.complement.service.param.converter.ProposalCancelConverter;
import com.platon.browser.complement.service.param.converter.ProposalTextConverter;
import com.platon.browser.complement.service.param.converter.ProposalUpgradeConverter;
import com.platon.browser.complement.service.param.converter.ProposalVersionConverter;
import com.platon.browser.complement.service.param.converter.ProposalVoteConverter;
import com.platon.browser.complement.service.param.converter.ReportConverter;
import com.platon.browser.complement.service.param.converter.RestrictingCreateConverter;
import com.platon.browser.complement.service.param.converter.StakeCreateConverter;
import com.platon.browser.complement.service.param.converter.StakeExitConverter;
import com.platon.browser.complement.service.param.converter.StakeIncreaseConverter;
import com.platon.browser.complement.service.param.converter.StakeModifyConverter;
import com.platon.browser.complement.service.supplement.SupplementService;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 业务入库参数服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class TransactionParameterService {

    @Autowired
    private SupplementService supplementService;
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
    private ProposalCancelConverter  proposalCancelConverter;
    @Autowired
    private ProposalVoteConverter  proposalVoteConverter;
    @Autowired
    private ProposalVersionConverter  proposalVersionConverter;
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
    public List<ComplementNodeOpt> getParameters(CollectionEvent event){
        List<CollectionTransaction> transactions = event.getTransactions();
        List<ComplementNodeOpt> nodeOptList = new ArrayList<>();
        
        int txQty = transactions.size();
        int proposalQty = 0;

        for (CollectionTransaction tx : transactions) {
        	//补充txInfo
        	supplementService.supplement(tx);
        	addressCache.update(tx);
        	
            try{
                // 失败的交易不分析业务数据
                if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) continue;
                // 调用交易分析引擎分析交易，以补充相关数据
                
                Optional<ComplementNodeOpt> nodeOpt = Optional.ofNullable(null);
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
                        proposalQty++;
                        break;
                    case PROPOSAL_UPGRADE: // 2001
                    	nodeOpt = proposalUpgradeConverter.convert(event,tx);
                        proposalQty++;
                        break;
                    case PROPOSAL_CANCEL: // 2005
                    	nodeOpt = proposalCancelConverter.convert(event,tx);
                        proposalQty++;
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
                
                nodeOpt.ifPresent(np -> nodeOptList.add(np));
                
            }catch (BusinessException e){
                log.debug("{}",e);
            }
           
        }
        
        networkStatCache.updateByBlock(txQty, proposalQty, event.getBlock().getTime());
        
        return nodeOptList;
    }
}