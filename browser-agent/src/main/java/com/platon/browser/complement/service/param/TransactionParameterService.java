package com.platon.browser.complement.service.param;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.slash.Report;
import com.platon.browser.common.complement.dto.stake.StakeCreate;
import com.platon.browser.common.complement.dto.stake.StakeExit;
import com.platon.browser.common.complement.dto.stake.StakeIncrease;
import com.platon.browser.common.complement.dto.stake.StakeModify;
import com.platon.browser.complement.service.param.converter.*;
import com.platon.browser.complement.service.supplement.SupplementService;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.queue.collection.event.CollectionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                    case DELEGATE_CREATE: // 1004 创建委托/发起委托

                        break;
                    case DELEGATE_EXIT: // 1005 减持/撤销委托

                        break;
                    case PROPOSAL_TEXT: // 2000 提交文本提案

                        break;
                    case PROPOSAL_UPGRADE: // 2001 提交升级提案

                        break;
                    case PROPOSAL_CANCEL: // 2005 提交取消提案

                        break;
                    case PROPOSAL_VOTE: // 2003 给提案投票

                        break;
                    case VERSION_DECLARE: // 2004 版本声明

                        break;
                    case REPORT: // 3000 举报双签
                        Report param3000 = reportConverter.convert(tx);
                        businessParams.add(param3000);
                        break;
                    case RESTRICTING_CREATE: // 4000 创建锁仓计划

                        break;
                    default:
                        break;
                }
            }catch (BusinessException e){
                log.debug("{}",e);
            }
            supplementService.supplement(tx);
        }
        return businessParams;
    }
}