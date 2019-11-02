package com.platon.browser.complement.service.transaction;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.stake.StakeCreate;
import com.platon.browser.common.complement.dto.stake.StakeExit;
import com.platon.browser.common.complement.dto.stake.StakeIncrease;
import com.platon.browser.common.complement.dto.stake.StakeModify;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.param.StakeExitParam;
import com.platon.browser.param.StakeIncreaseParam;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.util.VerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 业务入库参数服务
 */
@Slf4j
@Service
public class ParameterService {

    /**
     * 解析交易, 构造业务入库参数信息
     * @param tx
     * @return
     */
    public BusinessParam getParameter(CollectionTransaction tx){
        // 调用交易分析引擎分析交易，以补充相关数据
        BusinessParam businessParam;
        switch (tx.getTypeEnum()) {
            case STAKE_CREATE: // 1000
                // 发起质押
                StakeCreateParam param1000 = tx.getTxParam(StakeCreateParam.class);
                param1000.setBlockNumber(tx.getNum());
                BigInteger bigVersion = VerUtil.transferBigVersion(param1000.getProgramVersion());
                businessParam= StakeCreate.builder()
                        .benefitAddr(param1000.getBenefitAddress())
                        .stakingHes(new BigDecimal(param1000.getAmount()))
                        .webSite(param1000.getWebsite())
                        .stakingBlockNum(BigInteger.valueOf(param1000.getBlockNumber()))
                        .txHash(tx.getHash())
                        .stakingTxIndex(tx.getIndex())
                        .joinTime(tx.getTime())
                        .stakingAddr(tx.getFrom())
                        .bigVersion(bigVersion.toString())
                        .programVersion(param1000.getProgramVersion().toString())
                        .build();
                BeanUtils.copyProperties(param1000,businessParam);
                return businessParam;
            case STAKE_MODIFY: // 1001
                // 修改质押信息
                StakeModifyParam param1001 = tx.getTxParam(StakeModifyParam.class);
                businessParam= StakeModify.builder().build();
                BeanUtils.copyProperties(param1001,businessParam);
                return businessParam;
            case STAKE_INCREASE: // 1002
                // 增持质押
                StakeIncreaseParam param1002 = tx.getTxParam(StakeIncreaseParam.class);
                businessParam= StakeIncrease.builder().build();
                BeanUtils.copyProperties(param1002,businessParam);
                return businessParam;
            case STAKE_EXIT: // 1003
                // 撤销质押
                StakeExitParam param1003 = tx.getTxParam(StakeExitParam.class);
                businessParam= StakeExit.builder().build();
                BeanUtils.copyProperties(param1003,businessParam);
                return businessParam;
            case DELEGATE_CREATE: // 1004
                // 发起委托

            case DELEGATE_EXIT: // 1005
                // 减持/撤销委托

            case PROPOSAL_TEXT: // 2000
                // 提交文本提案

            case PROPOSAL_UPGRADE: // 2001
                // 提交升级提案

            case PROPOSAL_CANCEL: // 2005
                // 提交取消提案

            case PROPOSAL_VOTE: // 2003
                // 给提案投票

            case VERSION_DECLARE: // 2004
                // 版本声明

            case REPORT: // 3000
                // 举报双签

            case RESTRICTING_CREATE: // 4000
                //创建锁仓计划

            default:
                break;
        }
        throw new BusinessException("此交易无业务参数!");
    }
}
