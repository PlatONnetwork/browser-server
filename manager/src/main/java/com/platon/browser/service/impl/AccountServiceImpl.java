package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.dto.ticket.VoteTicket;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.service.*;
import com.platon.browser.util.EnergonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PendingTxService pendingTxService;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private PlatonClient platonClient;


    @Override
    public AddressDetail getAddressDetail(AddressDetailReq req) {
        AddressDetail returnData = new AddressDetail();
        try {
            EthGetBalance balance = chainsConfig.getWeb3j(req.getCid()).ethGetBalance(req.getAddress(), DefaultBlockParameterName.LATEST).send();
            BigDecimal v = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).setScale(8,RoundingMode.DOWN);
            returnData.setBalance(EnergonUtil.format(v));
        } catch (IOException e) {
            returnData.setBalance("0(error)");
        }

        Set<String> nodeIds = new HashSet<>();
        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria first = condition.createCriteria().andChainIdEqualTo(req.getCid())
                .andFromEqualTo(req.getAddress());
        TransactionExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andToEqualTo(req.getAddress());
        condition.or(second);
        Long transactionCount = transactionMapper.countByExample(condition);
        returnData.setTradeCount(transactionCount.intValue());

        // 取已完成交易
        Page page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<TransactionWithBLOBs> transactions = transactionService.getList(req);
        List<AccTransactionItem> data = new ArrayList<>();
        transactions.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            if(StringUtils.isNotBlank(bean.getNodeId())) nodeIds.add(bean.getNodeId());
            BigDecimal income = ticketService.getTicketIncomeSum(bean.getTxHash(),req.getCid());
            bean.setIncome(income);
            data.add(bean);
        });

        // 取待处理交易
        page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<PendingTx> pendingTxes = pendingTxService.getList(req);
        returnData.setTradeCount(returnData.getTradeCount()+Long.valueOf(page.getTotal()).intValue());
        pendingTxes.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            if(StringUtils.isNotBlank(bean.getNodeId())) nodeIds.add(bean.getNodeId());
            data.add(bean);
        });

        // 按时间倒排
        Collections.sort(data,(c1,c2)->{
            long t1 = c1.getTimestamp().getTime(),t2 = c2.getTimestamp().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });

        // 取节点名称 和 有效票数
        TicketContract ticketContract = platonClient.getTicketContract(req.getCid());
        Map<String,String> nodeIdToName=nodeService.getNodeNameMap(req.getCid(),new ArrayList<>(nodeIds));
        data.forEach(el->{
            if(StringUtils.isBlank(el.getNodeId())) return;
            el.setNodeName(nodeIdToName.get(el.getNodeId()));

            // 取有效票数
            el.setValidVoteCount(0);
            List<String> ticketIds = ticketContract.VoteTicketIds(Integer.valueOf(String.valueOf(el.getVoteCount())),el.getTxHash());
            if(ticketIds.size()>0){
                StringBuilder sb = new StringBuilder();
                ticketIds.forEach(id->{
                    sb.append(id).append(":");
                });
                String param = sb.toString();
                param=param.substring(0,param.lastIndexOf(":"));
                try {
                    String detailStr = ticketContract.GetBatchTicketDetail(param).send();
                    List <VoteTicket> details = JSON.parseArray(detailStr, VoteTicket.class);
                    AtomicInteger validCount=new AtomicInteger(0);
                    if(details.size()>0){
                        details.forEach(voteTicket -> {
                            if(voteTicket.getState()==1) validCount.incrementAndGet();
                        });
                        el.setValidVoteCount(validCount.intValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        returnData.setTrades(data);
        return returnData;
    }

    @Override
    public AccountDetail getAccountDetail (String address,String chainId) {
        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria first = condition.createCriteria().andChainIdEqualTo(chainId)
                .andFromEqualTo(address).andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code);
        TransactionExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(chainId)
                .andToEqualTo(address).andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code);
        condition.or(second);
        List<Transaction> voteTransactionList = transactionMapper.selectByExample(condition);
        Set<String> nodeIdList = new HashSet <>();
        BigInteger voteSum = BigInteger.ZERO;
        for(Transaction transaction : voteTransactionList){
            voteSum = voteSum.add(new BigInteger(transaction.getValue()));
            String txInfo = transaction.getTxInfo();
            try{
                TxInfo txInfoObject =  JSON.parseObject(txInfo, TxInfo.class);
                TxInfo.Parameter parameter  = txInfoObject.getParameters();
                if(parameter != null && StringUtils.isNotBlank(parameter.getNodeId())){
                    nodeIdList.add(parameter.getNodeId());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setNodeCount(nodeIdList.size());
        accountDetail.setVotePledge(Convert.fromWei(voteSum.toString(), Convert.Unit.ETHER).toString());

        return accountDetail;
    }

}
