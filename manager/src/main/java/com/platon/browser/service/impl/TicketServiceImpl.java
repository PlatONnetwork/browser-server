package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    private final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private PlatonClient platon;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;



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

    @Override
    public BigDecimal getTicketIncome ( String txHash ,String chainId) {
        Transaction transaction = transactionMapper.selectByPrimaryKey(txHash);
        Map<String, VoteTicket> voteTicketMap = new HashMap<>();
        TxInfo txInfo = JSON.parseObject(transaction.getTxInfo(), TxInfo.class);
        TxInfo.Parameter parameter = txInfo.getParameters();
        BigDecimal income = new BigDecimal("0");
        if (parameter != null) {
            if (parameter.getCount() != null) {
                TicketContract ticketContract = platon.getTicketContract(chainId);
                List <String> ticketIds = ticketContract.VoteTicketIds(parameter.getCount(), txHash);
                StringBuilder sb = new StringBuilder();
                String tail = ticketIds.get(ticketIds.size() - 1);
                ticketIds.forEach(id -> {
                    sb.append(id);
                    if (!tail.equals(id)) sb.append(":");
                });
                try {
                    String str = ticketContract.GetBatchTicketDetail(sb.toString()).send();
                    List <VoteTicket> details = JSON.parseArray(str, VoteTicket.class);
                    details.forEach(voteTicket -> {
                        if (voteTicket.getState() == 2)
                            voteTicketMap.put(voteTicket.getTicketId(), voteTicket);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //map为空说明该交易中没有幸运票
                if (null == voteTicketMap || voteTicketMap.size() < 1) {
                    return income;
                } else {
                    //循环获取，分别求出收益
                    for(String ignored : ticketIds){
                        VoteTicket voteTicket = voteTicketMap.get(ignored);
                        if (!org.springframework.util.StringUtils.isEmpty(voteTicket)) {
                            double wheel = Math.floor(voteTicket.getBlockNumber() / 250);
                            BlockExample blockExample = new BlockExample();
                            Long endNumber = 0L;
                            Long beginNumber = 0L;
                            if (wheel == 0.0) {
                                endNumber = 250L;
                                beginNumber = 1L;
                            } else {
                                endNumber = (long) wheel * 250;
                                beginNumber = endNumber - 250;
                            }
                            blockExample.createCriteria()
                                    .andNumberBetween(beginNumber, endNumber)
                                    .andNodeIdEqualTo(voteTicket.getCandidateId())
                                    .andChainIdEqualTo(chainId);
                            List <Block> blocks = blockMapper.selectByExample(blockExample);
                            NodeRankingExample nodeRankingExample = new NodeRankingExample();
                            nodeRankingExample.createCriteria()
                                    .andChainIdEqualTo(chainId)
                                    .andNodeIdEqualTo(voteTicket.getCandidateId());
                            nodeRankingExample.setOrderByClause(" create_time desc ");
                            List <NodeRanking> nodeRankings = nodeRankingMapper.selectByExample(nodeRankingExample);
                            BigDecimal blockReward = BigDecimal.ZERO;
                            for(Block block : blocks){
                                blockReward = blockReward.add(new BigDecimal(block.getBlockReward()));
                            }
                            NodeRanking nodeRanking = nodeRankings.get(0);
                            income = income.add(new BigDecimal(1 - nodeRanking.getRewardRatio()).multiply(blockReward));
                        }
                    }

                }
            }
        }
        return income;
    }

}
