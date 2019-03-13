package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
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
import com.platon.browser.util.EnergonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.*;

import static org.web3j.utils.Convert.Unit.ETHER;

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
                List <VoteTicket> details = JSON.parseArray(str, VoteTicket.class);

                List <Ticket> data = new ArrayList <>();

                Set <Long> blockNumbers = new HashSet <>();
                details.forEach(detail -> {
                    Ticket ticket = new Ticket();
                    BeanUtils.copyProperties(detail, ticket);
                    ticket.setTxHash(transaction.getHash());
                    ticket.setState(detail.getState().intValue());
                    if(!ticket.getCandidateId().startsWith("0x")) ticket.setCandidateId("0x"+ticket.getCandidateId());
                    blockNumbers.add(ticket.getBlockNumber());
                    if (ticket.getRblockNumber() != null) blockNumbers.add(ticket.getRblockNumber());
                    data.add(ticket);
                });


                Map<String,BigDecimal> ticketIncomeMap = getTicketIncome(req.getCid(),ticketIds);

                if(blockNumbers.size()>0){
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
                            timestamp += 1536000000;
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

                        ticket.setIncome(BigDecimal.ZERO);
                        BigDecimal incomeInWei = ticketIncomeMap.get(ticket.getTicketId());
                        if(incomeInWei!=null){
                            BigDecimal income= Convert.fromWei(incomeInWei, ETHER);
                            ticket.setIncome(new BigDecimal(EnergonUtil.format(income)));
                        }
                    });
                }

                page.setTotal(data.size());
                page.setPageSize(data.size());
                returnData.init(page, data);
                returnData.setTotalPages(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnData;
    }

    /**
     * 计算投票收益
     * 找出票对应的所有区块，将区块按票ID分组，将每一组的每个块的（区块奖励x分红比例）累加，得出每张票的收益
     */
    @Override
    public Map<String,BigDecimal> getTicketIncome(String chainId, List<String> ticketIds) {
        BlockExample blockExample=new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(chainId)
                .andTicketIdIn(ticketIds);
        List<Block> blocks = blockMapper.selectByExample(blockExample);
        // 对区块按票ID分组
        Map<String,Set<Block>> ticketIdToBlocks = new HashMap<>();
        blocks.forEach(block -> {
            Set<Block> set=ticketIdToBlocks.get(block.getTicketId());
            if(set==null) {
                set=new HashSet<>();
                ticketIdToBlocks.put(block.getTicketId(),set);
            }
            set.add(block);
        });
        Map<String,BigDecimal> ticketIncomeMap = new HashMap<>();
        ticketIdToBlocks.forEach((ticketId,blockSet)->{
            // 统一设置默认值BigDecimal.ZERO
            ticketIncomeMap.put(ticketId,BigDecimal.ZERO);
            blockSet.forEach(block -> {
                String blockRewardStr=block.getBlockReward();
                if(StringUtils.isBlank(blockRewardStr)) return;
                try{
                    BigDecimal blockReward = new BigDecimal(blockRewardStr);
                    Double nodeRewardRatio = block.getRewardRatio();
                    if(nodeRewardRatio==null) nodeRewardRatio=0d;
                    Double ticketRewardRatio = 1-nodeRewardRatio;
                    BigDecimal ticketReward=blockReward.multiply(BigDecimal.valueOf(ticketRewardRatio));
                    BigDecimal rewardSum = ticketIncomeMap.get(ticketId);
                    rewardSum = rewardSum.add(ticketReward);
                    ticketIncomeMap.put(ticketId,rewardSum);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
        return ticketIncomeMap;
    }

    @Override
    public Map<String,BigDecimal> getTicketIncome ( String chainId, String txHash ) {
        Map<String,BigDecimal> incomeMap = new HashMap<>();
        Transaction transaction = transactionMapper.selectByPrimaryKey(txHash);
        TxInfo txInfo = JSON.parseObject(transaction.getTxInfo(), TxInfo.class);
        TxInfo.Parameter parameter = txInfo.getParameters();
        if (parameter != null) {
            if (parameter.getCount() != null) {
                TicketContract ticketContract = platon.getTicketContract(chainId);
                List <String> ticketIds = ticketContract.VoteTicketIds(parameter.getCount(), txHash);
                incomeMap=getTicketIncome(chainId,ticketIds);
            }
        }
        return incomeMap;
    }

}
