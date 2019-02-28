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
        long voteCount=0;
        BigDecimal ticketPrice=BigDecimal.ZERO,deposit=BigDecimal.ZERO;
    }
    public static void resolve(String txType,String txInfo,String value,Object target){
        ResolveResult rr = new ResolveResult();
        try {
            if(StringUtils.isNotBlank(txInfo)){
                TransactionTypeEnum typeEnum = TransactionTypeEnum.getEnum(txType);
                switch (typeEnum){
                    // 投票交易
                    case TRANSACTION_VOTE_TICKET:
                        TxInfo ticketTxInfo = JSON.parseObject(txInfo,TxInfo.class);
                        TxInfo.Parameter ticketParameter = ticketTxInfo.getParameters();
                        if(ticketParameter!=null){
                            rr.nodeId=ticketParameter.getNodeId();
                            rr.voteCount=ticketParameter.getCount();
                            rr.ticketPrice=Convert.fromWei(ticketParameter.getPrice().toString(), Convert.Unit.ETHER);
                        }
                        break;
                    // 竞选交易
                    case TRANSACTION_CANDIDATE_APPLY_WITHDRAW:
                    case TRANSACTION_CANDIDATE_WITHDRAW:
                    case TRANSACTION_CANDIDATE_DEPOSIT:
                        CandidateTxInfo candidateTxInfo = JSON.parseObject(txInfo,CandidateTxInfo.class);
                        CandidateTxInfo.Parameter candidateParameter = candidateTxInfo.getParameters();
                        if(candidateParameter!=null){
                            rr.nodeId=candidateParameter.getNodeId();
                            String extraStr = candidateParameter.getExtra();
                            if(StringUtils.isNotBlank(extraStr)){
                                CandidateTxInfo.Extra extra = JSON.parseObject(extraStr, CandidateTxInfo.Extra.class);
                                if(extra!=null){
                                    rr.nodeName=extra.getNodeName();
                                }
                            }
                        }
                        if(StringUtils.isNotBlank(value)){
                            Double dep = Double.valueOf(value);
                            rr.deposit=BigDecimal.valueOf(dep);
                        }
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        BeanUtils.copyProperties(rr,target);
    }
}
