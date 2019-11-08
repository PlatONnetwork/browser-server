package com.platon.browser.complement.service.param;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.delegate.DelegateCreate;
import com.platon.browser.common.complement.dto.delegate.DelegateExit;
import com.platon.browser.common.complement.dto.proposal.ProposalCancel;
import com.platon.browser.common.complement.dto.proposal.ProposalText;
import com.platon.browser.common.complement.dto.proposal.ProposalUpgrade;
import com.platon.browser.common.complement.dto.proposal.ProposalVersion;
import com.platon.browser.common.complement.dto.proposal.ProposalVote;
import com.platon.browser.common.complement.dto.restricting.RestrictingCreate;
import com.platon.browser.common.complement.dto.slash.Report;
import com.platon.browser.common.complement.dto.stake.StakeCreate;
import com.platon.browser.common.complement.dto.stake.StakeExit;
import com.platon.browser.common.complement.dto.stake.StakeIncrease;
import com.platon.browser.common.complement.dto.stake.StakeModify;
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
    public List<BusinessParam> getParameters(CollectionEvent event){
        List<CollectionTransaction> transactions = event.getTransactions();
        List<BusinessParam> businessParams = new ArrayList<>();
        if(transactions.isEmpty()) return businessParams;
        
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
                switch (tx.getTypeEnum()) {
                    case STAKE_CREATE: // 1000 创建验证人
                        StakeCreate param1000 = stakeCreateConverter.convert(event,tx);
                        businessParams.add(param1000);
                        break;
                    case STAKE_MODIFY: // 1001 编辑验证人
                        StakeModify param1001 = stakeModifyConverter.convert(event,tx);
                        businessParams.add(param1001);
                        break;
                    case STAKE_INCREASE: // 1002 增持质押
                        StakeIncrease param1002 = stakeIncreaseConverter.convert(event,tx);
                        businessParams.add(param1002);
                        break;
                    case STAKE_EXIT: // 1003 退出质押
                        StakeExit param1003 = stakeExitConverter.convert(event,tx);
                        // 补充质押周期
                        businessParams.add(param1003);
                        break;
                    case DELEGATE_CREATE: // 1004
                    	DelegateCreate param1004 = delegateCreateConverter.convert(event,tx);
                        businessParams.add(param1004);
                        break;
                    case DELEGATE_EXIT: // 1005
                       	DelegateExit param1005 = delegateExitConverter.convert(event,tx);
                        businessParams.add(param1005);
                        break;
                    case PROPOSAL_TEXT: // 2000
                    	ProposalText param2000 = proposalTextConverter.convert(event,tx);
                        businessParams.add(param2000);
                        proposalQty++;
                        break;
                    case PROPOSAL_UPGRADE: // 2001
                    	ProposalUpgrade param2001 = proposalUpgradeConverter.convert(event,tx);
                        businessParams.add(param2001);
                        proposalQty++;
                        break;
                    case PROPOSAL_CANCEL: // 2005
                       	ProposalCancel param2005 = proposalCancelConverter.convert(event,tx);
                        businessParams.add(param2005);
                        proposalQty++;
                        break;
                    case PROPOSAL_VOTE: // 2003
                     	ProposalVote param2003 = proposalVoteConverter.convert(event,tx);
                        businessParams.add(param2003);
                        break;
                    case VERSION_DECLARE: // 2004
                       	ProposalVersion param2004 = proposalVersionConverter.convert(event,tx);
                        businessParams.add(param2004);
                        break;
                    case REPORT: // 3000
                        Report param3000 = reportConverter.convert(event,tx);
                        businessParams.add(param3000);
                        break;
                    case RESTRICTING_CREATE: // 4000
                    	RestrictingCreate param4000 = restrictingCreateConverter.convert(event,tx);
                    	businessParams.add(param4000);
                        break;
                    default:
                        break;
                }
            }catch (BusinessException e){
                log.debug("{}",e);
            }
           
        }
        
        networkStatCache.updateByBlock(txQty, proposalQty, event.getBlock().getTime());
        
        return businessParams;
    }
}