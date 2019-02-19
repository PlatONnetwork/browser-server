package com.platon.browser.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.PendingTxService;
import com.platon.browser.service.TicketService;
import com.platon.browser.service.TransactionService;
import com.platon.browser.util.EnergonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private TicketService ticketService;


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

        // 取已完成交易
        Page page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<TransactionWithBLOBs> transactions = transactionService.getList(req);
        returnData.setTradeCount(Long.valueOf(page.getTotal()).intValue());
        List<AccTransactionItem> data = new ArrayList<>();
        transactions.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            if(StringUtils.isNotBlank(bean.getNodeId())) nodeIds.add(bean.getNodeId().replace("0x",""));
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
            if(StringUtils.isNotBlank(bean.getNodeId())) nodeIds.add(bean.getNodeId().replace("0x",""));
            data.add(bean);
        });

        // 按时间倒排
        Collections.sort(data,(c1,c2)->{
            long t1 = c1.getTimestamp().getTime(),t2 = c2.getTimestamp().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });

        Map<String,String> nodeIdToName=new HashMap<>();
        if(nodeIds.size()>0){
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(req.getCid())
                    .andNodeIdIn(new ArrayList<>(nodeIds));
            List<NodeRanking> nodes = nodeRankingMapper.selectByExample(nodeRankingExample);
            nodes.forEach(node->{
                if(!node.getNodeId().startsWith("0x")) node.setNodeId("0x"+node.getNodeId());
                nodeIdToName.put(node.getNodeId(),node.getName());
            });
        }
        data.forEach(el->el.setNodeName(nodeIdToName.get(el.getNodeId())));
        returnData.setTrades(data);
        return returnData;
    }

}
