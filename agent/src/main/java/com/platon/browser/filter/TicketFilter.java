//package com.platon.browser.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.bean.TransactionBean;
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.dao.entity.Ticket;
//import com.platon.browser.dao.entity.Transaction;
//import com.platon.browser.enums.TransactionTypeEnum;
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.web3j.platon.contracts.TicketContract;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * User: dongqile
// * Date: 2019/1/7
// * Time: 14:28
// */
//@Component
//public class TicketFilter {
//    private static Logger log = LoggerFactory.getLogger(TicketFilter.class);
//    @Autowired
//    private PlatonClient platon;
//
//    @Data
//    public static class TxInfo{
//        @Data
//        public static class Parameter{
//            BigInteger price;
//            Integer count;
//            String nodeId;
//        }
//        String functionName,type;
//        Parameter parameters;
//    }
//
//    @Data
//    public static class VoteTicket {
//        private Long BlockNumber;
//        private String CandidateId;
//        private Long Deposit;
//        private String Owner;
//        private Long RBlockNumber;
//        private Long State;
//        private String TicketId;
//    }
//
//    public List<Ticket> analyse(List<TransactionBean> transactions) {
//        List<Ticket> tickets = new ArrayList<>();
//
//        List<Transaction> voteTransactions = new ArrayList<>();
//        transactions.forEach(transaction -> {
//            TransactionTypeEnum typeEnum = TransactionTypeEnum.getEnum(transaction.getTxType());
//            switch (typeEnum){
//                case TRANSACTION_VOTE_TICKET: voteTransactions.add(transaction);
//            }
//        });
//
//        voteTransactions.forEach(transaction -> {
//            String txInfo = transaction.getTxInfo();
//            if(StringUtils.isNotBlank(txInfo)){
//                TxInfo info = JSON.parseObject(txInfo,TxInfo.class);
//
//                TicketContract ticketContract = platon.getTicketContract();
//                List<String> ticketIds = ticketContract.VoteTicketIds(info.getParameters().count,transaction.getHash());
//                if(ticketIds.size()==0) return;
//
//                StringBuilder sb = new StringBuilder();
//                String tail = ticketIds.get(ticketIds.size()-1);
//                ticketIds.forEach(id->{
//                    sb.append(id);
//                    if(!tail.equals(id)) sb.append(":");
//                });
//                try {
//                    String str = ticketContract.GetBatchTicketDetail(sb.toString()).send();
//                    List<VoteTicket> details = JSON.parseArray(str,VoteTicket.class);
//                    details.forEach(detail->{
//                        Ticket ticket = new Ticket();
//                        BeanUtils.copyProperties(detail,ticket);
//                        ticket.setTxHash(transaction.getHash());
//                        ticket.setState(detail.getState().intValue());
//                        tickets.add(ticket);
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return tickets;
//    }
//
//}