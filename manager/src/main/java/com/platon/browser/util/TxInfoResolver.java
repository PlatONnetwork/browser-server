package com.platon.browser.util;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.dto.transaction.CandidateTxInfo;
import com.platon.browser.enums.TransactionTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class TxInfoResolver {
    @Data
    private static class ResolveResult{
        String nodeId,nodeName;
        Integer voteCount=0;
        BigDecimal ticketPrice=BigDecimal.ZERO,deposit=BigDecimal.ZERO;
    }
    public static void resolve(String txType,String txInfo,String value,Object target){
        ResolveResult rr = new ResolveResult();
        try {
            TransactionTypeEnum typeEnum = TransactionTypeEnum.getEnum(txType);
            switch (typeEnum){
                // 投票交易
                case TRANSACTION_VOTE_TICKET:
                    if(StringUtils.isNotBlank(txInfo)){
                        TxInfo info = JSON.parseObject(txInfo,TxInfo.class);
                        TxInfo.Parameter parameter = info.getParameters();
                        if(parameter!=null){
                            rr.nodeId=parameter.getNodeId();
                            rr.voteCount=parameter.getCount();
                            rr.ticketPrice=Convert.fromWei(parameter.getPrice().toString(), Convert.Unit.ETHER);
                        }
                    }
                    break;
                // 竞选交易
                case TRANSACTION_CANDIDATE_APPLY_WITHDRAW:
                case TRANSACTION_CANDIDATE_WITHDRAW:
                case TRANSACTION_CANDIDATE_DEPOSIT:
                    if(StringUtils.isNotBlank(txInfo)){
                        CandidateTxInfo info = JSON.parseObject(txInfo,CandidateTxInfo.class);
                        CandidateTxInfo.Parameter parameter = info.getParameters();
                        if(parameter!=null){
                            rr.nodeId=parameter.getNodeId();
                            String extraStr = parameter.getExtra();
                            if(StringUtils.isNotBlank(extraStr)){
                                CandidateTxInfo.Extra extra = JSON.parseObject(extraStr, CandidateTxInfo.Extra.class);
                                if(extra!=null){
                                    rr.nodeName=extra.getNodeName();
                                }
                            }
                        }
                    }
                    if(StringUtils.isNotBlank(value)){
                        Double dep = Double.valueOf(value);
                        rr.deposit=BigDecimal.valueOf(dep);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        BeanUtils.copyProperties(rr,target);
    }
}
