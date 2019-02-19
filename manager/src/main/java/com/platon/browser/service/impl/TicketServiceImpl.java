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
import com.platon.browser.enums.TicketStatusEnum;
import com.platon.browser.req.ticket.TicketListReq;
import com.platon.browser.service.TicketService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;

import java.math.BigDecimal;
import java.util.*;

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

                Set<Long> blockNumbers = new HashSet<>();
                details.forEach(detail->{
                    Ticket ticket = new Ticket();
                    BeanUtils.copyProperties(detail,ticket);
                    ticket.setTxHash(transaction.getHash());
                    ticket.setState(detail.getState().intValue());
                    blockNumbers.add(ticket.getBlockNumber());
                    if(ticket.getRblockNumber()!=null) blockNumbers.add(ticket.getRblockNumber());
                    data.add(ticket);
                });

                /**
                 * 关于预计过期时间和实际过期时间
                 * 预计过期时间：通过blockNumber取到出块时间，再加1563000秒
                 * 1、状态为正常时：只有预计过期时间；
                 * 2、状态为选中时：通过rblockNumber查询区块信息，查到则有实际过期时间，其值等于预计过期时间；查不到则只有预计过期时间；
                 * 3、状态为掉榜或过期时：预计过期时间=实际过期时间
                 */
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(req.getCid())
                        .andNumberIn(new ArrayList<>(blockNumbers));
                List<Block> blocks = blockMapper.selectByExample(blockExample);
                Map<Long,Long> blockNumberToTimestamp = new HashMap<>();
                blocks.forEach(block -> blockNumberToTimestamp.put(block.getNumber(),block.getTimestamp().getTime()));

                data.forEach(ticket -> {
                    Long timestamp = blockNumberToTimestamp.get(ticket.getBlockNumber());
                    // 所有票都有预计过期时间
                    if(timestamp!=null){
                        timestamp += 1563000000;
                        ticket.setEstimateExpireTime(new Date(timestamp));
                    }
                    TicketStatusEnum statusEnum = TicketStatusEnum.getEnum(ticket.getState());
                    switch (statusEnum){
                        case SELECTED:
                            // 状态为选中时：通过rblockNumber查询区块信息，查到则有实际过期时间，其值等于预计过期时间；查不到则只有预计过期时间；
                            timestamp = blockNumberToTimestamp.get(ticket.getRblockNumber());
                            if(timestamp!=null){
                                ticket.setActualExpireTime(new Date(timestamp));
                            }
                            break;
                        case OFF_LIST:
                        case EXPIRED:
                            // 状态为掉榜或过期时：预计过期时间=实际过期时间
                            ticket.setActualExpireTime(ticket.getEstimateExpireTime());
                            break;
                    }
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
