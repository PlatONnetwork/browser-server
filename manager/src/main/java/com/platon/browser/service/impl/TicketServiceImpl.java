package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.dto.ticket.VoteTicket;
import com.platon.browser.req.ticket.TicketListReq;
import com.platon.browser.service.TicketService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private PlatonClient platon;

    /**
     * 通过账户信息获取交易列表, 以太坊账户有两种类型：外部账户-钱包地址，内部账户-合约地址
     * @param req
     * @return
     */
    @Override
    public RespPage<Ticket> getList(TicketListReq req) {
        RespPage<Ticket> returnData = new RespPage<>();

        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        Page page = PageHelper.startPage(1,1);
        List<Transaction> transactions = transactionMapper.selectByExample(condition);

        if(transactions.size()==0) return returnData;
        Transaction transaction = transactions.get(0);
        String txInfo = transaction.getTxInfo();
        if(StringUtils.isNotBlank(txInfo)){
            TxInfo info = JSON.parseObject(txInfo,TxInfo.class);

            TicketContract ticketContract = platon.getTicketContract(req.getCid());
            List<String> ticketIds = ticketContract.VoteTicketIds(info.getParameters().getCount(),transaction.getHash());
            if(ticketIds.size()==0) return returnData;

            StringBuilder sb = new StringBuilder();
            String tail = ticketIds.get(ticketIds.size()-1);
            ticketIds.forEach(id->{
                sb.append(id);
                if(!tail.equals(id)) sb.append(":");
            });
            try {
                String str = ticketContract.GetBatchTicketDetail(sb.toString()).send();
                List<VoteTicket> details = JSON.parseArray(str,VoteTicket.class);

                List<Ticket> data = new ArrayList<>();
                details.forEach(detail->{
                    Ticket ticket = new Ticket();
                    BeanUtils.copyProperties(detail,ticket);
                    ticket.setTxHash(transaction.getHash());
                    ticket.setState(detail.getState().intValue());
                    data.add(ticket);
                });
                page.setTotal(data.size());
                page.setPageSize(data.size());
                returnData.init(page,data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnData;
    }

}
