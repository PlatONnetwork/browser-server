package com.platon.browser.complement.service.param;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.complement.service.param.converter.*;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 业务入库参数服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ParameterService {

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
     * @param tx
     * @return
     */
    public BusinessParam getParameter(CollectionTransaction tx){
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) throw new BusinessException("此交易失败!");
        // 调用交易分析引擎分析交易，以补充相关数据
        switch (tx.getTypeEnum()) {
            case STAKE_CREATE: // 1000 创建验证人
                return stakeCreateConverter.convert(tx);
            case STAKE_MODIFY: // 1001 编辑验证人
                return stakeModifyConverter.convert(tx);
            case STAKE_INCREASE: // 1002 增持质押
                return stakeIncreaseConverter.convert(tx);
            case STAKE_EXIT: // 1003 退出质押
                return stakeExitConverter.convert(tx);
            case DELEGATE_CREATE: // 1004 创建委托/发起委托

            case DELEGATE_EXIT: // 1005 减持/撤销委托

            case PROPOSAL_TEXT: // 2000 提交文本提案

            case PROPOSAL_UPGRADE: // 2001 提交升级提案

            case PROPOSAL_CANCEL: // 2005 提交取消提案

            case PROPOSAL_VOTE: // 2003 给提案投票

            case VERSION_DECLARE: // 2004 版本声明

            case REPORT: // 3000 举报双签
                return reportConverter.convert(tx);
            case RESTRICTING_CREATE: // 4000 创建锁仓计划

            default:
                break;
        }
        throw new BusinessException("此交易无业务参数!");
    }
}
