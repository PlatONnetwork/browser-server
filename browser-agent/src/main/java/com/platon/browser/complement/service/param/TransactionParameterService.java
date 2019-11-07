package com.platon.browser.complement.service.param;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.delegate.DelegateCreate;
import com.platon.browser.common.complement.dto.delegate.DelegateExit;
import com.platon.browser.common.complement.dto.proposal.ProposalText;
import com.platon.browser.common.complement.dto.proposal.ProposalUpgrade;
import com.platon.browser.common.complement.dto.slash.Report;
import com.platon.browser.common.complement.dto.stake.StakeCreate;
import com.platon.browser.common.complement.dto.stake.StakeExit;
import com.platon.browser.common.complement.dto.stake.StakeIncrease;
import com.platon.browser.common.complement.dto.stake.StakeModify;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.service.param.converter.DelegateCreateConverter;
import com.platon.browser.complement.service.param.converter.DelegateExitConverter;
import com.platon.browser.complement.service.param.converter.ProposalTextConverter;
import com.platon.browser.complement.service.param.converter.ReportConverter;
import com.platon.browser.complement.service.param.converter.StakeCreateConverter;
import com.platon.browser.complement.service.param.converter.StakeExitConverter;
import com.platon.browser.complement.service.param.converter.StakeIncreaseConverter;
import com.platon.browser.complement.service.param.converter.StakeModifyConverter;
import com.platon.browser.complement.service.supplement.SupplementService;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.ProposalUpgradeParam;

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
    

    /**
     * 解析交易, 构造业务入库参数信息
     * @param event
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event){
        List<CollectionTransaction> transactions = event.getTransactions();
        List<BusinessParam> businessParams = new ArrayList<>();
        if(transactions.isEmpty()) return businessParams;
        for (CollectionTransaction tx : transactions) {
        	//补充txInfo
        	supplementService.supplement(tx);
             	
            try{
                // 失败的交易不分析业务数据
                if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) continue;
                // 调用交易分析引擎分析交易，以补充相关数据
                switch (tx.getTypeEnum()) {
                    case STAKE_CREATE: // 1000 创建验证人
                        StakeCreate param1000 = stakeCreateConverter.convert(tx);
                        businessParams.add(param1000);
                        break;
                    case STAKE_MODIFY: // 1001 编辑验证人
                        StakeModify param1001 = stakeModifyConverter.convert(tx);
                        businessParams.add(param1001);
                        break;
                    case STAKE_INCREASE: // 1002 增持质押
                        StakeIncrease param1002 = stakeIncreaseConverter.convert(tx);
                        businessParams.add(param1002);
                        break;
                    case STAKE_EXIT: // 1003 退出质押
                        StakeExit param1003 = stakeExitConverter.convert(tx);
                        // 补充质押周期
                        param1003.setStakingReductionEpoch(event.getEpochMessage().getSettleEpochRound().intValue());
                        businessParams.add(param1003);
                        break;
                    case DELEGATE_CREATE: // 1004
                    	DelegateCreate param1004 = delegateCreateConverter.convert(tx);
                        businessParams.add(param1004);
                        break;
                    case DELEGATE_EXIT: // 1005
                       	DelegateExit param1005 = delegateExitConverter.convert(tx);
                        businessParams.add(param1005);
                        break;
                    case PROPOSAL_TEXT: // 2000
                    	ProposalText param2000 = proposalTextConverter.convert(tx);
                        businessParams.add(param2000);
                        break;
                    case PROPOSAL_UPGRADE: // 2001 TODO: 提交升级提案
//                    	ProposalUpgrade param2001 = proposalUpgradeConverter.convert(tx);
//                        businessParams.add(param2000);
//                        break;
                    case PROPOSAL_CANCEL: // 2005 TODO: 提交取消提案

                        break;
                    case PROPOSAL_VOTE: // 2003 TODO: 给提案投票

                        break;
                    case VERSION_DECLARE: // 2004 TODO: 版本声明

                        break;
                    case REPORT: // 3000 举报双签
                        Report param3000 = reportConverter.convert(tx);
                        businessParams.add(param3000);
                        break;
                    case RESTRICTING_CREATE: // 4000 TODO: 创建锁仓计划

                        break;
                    default:
                        break;
                }
            }catch (BusinessException e){
                log.debug("{}",e);
            }
           
        }
          
        return businessParams;
    }
}