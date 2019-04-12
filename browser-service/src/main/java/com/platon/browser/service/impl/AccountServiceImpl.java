package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.enums.NodeType;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.PendingTxService;
import com.platon.browser.service.TransactionService;
import com.platon.browser.service.cache.TransactionCacheService;
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

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PendingTxService pendingTxService;
    @Autowired
    private ChainsConfig chainsConfig;
    /*@Autowired
    private TicketService ticketService;*/
    @Autowired
    private NodeService nodeService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private PlatonClient platonClient;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private TransactionCacheService transactionCacheService;


    @Override
    public AddressDetail getAddressDetail(AddressDetailReq req) {
        long startTime = System.currentTimeMillis();

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



        // 取已完成交易(查询缓存中的200条记录)
        Collection<TransactionWithBLOBs> transactions = transactionCacheService.fuzzyQuery(req.getCid(),req.getAddress(),req.getTxType(),null,null);
        List<AccTransactionItem> data = new ArrayList<>();
        List<String> hashList = new ArrayList <>();
        transactions.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            hashList.add(initData.getHash());
            if(StringUtils.isNotBlank(bean.getNodeId())) nodeIds.add("0x" + bean.getNodeId());
            data.add(bean);
        });



        //返回标识用于判断跳转
        // 0——历史节点详情
        // 1——共识节点详情
        // 2——异常情况不跳转
        List<String> idList = new ArrayList<>(nodeIds);
        Map<String,String> consensusMap = new HashMap <>();
        Map<String,String> historyMap = new HashMap <>();
        NodeRankingExample nodeRankingExample = new NodeRankingExample();
        NodeRankingExample.Criteria criteria =nodeRankingExample.createCriteria().andChainIdEqualTo(req.getCid());
        if(idList.size() > 0){
            criteria.andNodeIdIn(idList);
        }
        List<NodeRanking> allNodeList = nodeRankingMapper.selectByExample(nodeRankingExample);
        if(allNodeList.size() > 0){
            allNodeList.forEach(allNode->{
                //有效节点，放入consensucMap
                if(allNode.getIsValid().equals(1)){
                    consensusMap.put(allNode.getNodeId(), NodeType.VALID_NODE.desc.trim());
                }
                //无效节点，放入historyMap
                else if(allNode.getIsValid().equals(0)){
                    historyMap.put(allNode.getNodeId(),NodeType.UNVALID_NODE.desc.trim());
                }
            });
        }

        // 取待处理交易
        Page page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<PendingTx> pendingTxes = pendingTxService.getList(req);
        returnData.setTradeCount(returnData.getTradeCount()+Long.valueOf(page.getTotal()).intValue());
        pendingTxes.forEach(initData -> {
            AccTransactionItem bean = new AccTransactionItem();
            bean.init(initData);
            if(StringUtils.isNotBlank(bean.getNodeId())) nodeIds.add("0x" + bean.getNodeId());

            data.add(bean);
        });



        // 按时间倒排
        Collections.sort(data,(c1,c2)->{
            long t1 = c1.getTimestamp().getTime(),t2 = c2.getTimestamp().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });


        data.forEach(accTransactionItem -> {
            accTransactionItem.setNodeId("0x" + accTransactionItem.getNodeId());
        });


        // 取节点名称
        TicketContract ticketContract = platonClient.getTicketContract(req.getCid());
        Map<String,String> nodeIdToName=nodeService.getNodeNameMap(req.getCid(),new ArrayList<>(nodeIds));

        //根据交易hash列表获取所有hash对应的交易有效票列表
        Map<String,Integer> voteHashMap = new HashMap <>();
        try {
            StringBuffer txHash = new StringBuffer();
            for(AccTransactionItem accTransactionItem : data){
                txHash.append(accTransactionItem.getTxHash()).append(":");
            }
            if(null != txHash){
                String hashs = txHash.toString();
                hashs = hashs.substring(0,hashs.lastIndexOf(":"));
                String voteNumber = ticketContract.GetTicketCountByTxHash(hashs).send();
                voteHashMap = JSON.parseObject(voteNumber,Map.class);
            }
        }catch (Exception e){
            for(AccTransactionItem accTransactionItem : data){
                voteHashMap.put(accTransactionItem.getTxHash(),0);
            }
            logger.error("get transaction voteNumber Exception !!!");
        }

        for(AccTransactionItem accTransactionItem : data){
            String nodeName = nodeIdToName.get(accTransactionItem.getNodeId());
            if(null != nodeName)accTransactionItem.setNodeName(nodeName);
             else accTransactionItem.setNodeName(" ");


            Integer number = voteHashMap.get(accTransactionItem.getTxHash());
            accTransactionItem.setValidVoteCount(number);
        }


        logger.error("node vote part one about vaildVote select time---------------------------------->{}", System.currentTimeMillis() - startTime);


        //设置交易收益
        //分组计算收益
        Map<String,BigDecimal> incomeMap = new HashMap <>();

        //根据投票交易hash查询区块列表
        if(hashList.size()>0){
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(req.getCid()).andVoteHashIn(hashList);
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            Map<String,List<Block>> groupMap = new HashMap <>();
            //根据hash分组hash-block
            blocks.forEach(block->{
                List<Block> group=groupMap.get(block.getVoteHash());
                if(group==null){
                    group=new ArrayList <>();
                    groupMap.put(block.getVoteHash(),group);
                }
                group.add(block);
            });

            groupMap.forEach((txHash,group)->{
                BigDecimal txIncome = BigDecimal.ZERO;
                for (Block block:group){
                    txIncome=txIncome.add(new BigDecimal(block.getBlockReward()).multiply(BigDecimal.valueOf(1-block.getRewardRatio())));
                }
                incomeMap.put(txHash,txIncome);
            });
        }

        logger.error("node vote part two about vaildVote income select time---------------------------------->{}", System.currentTimeMillis() - startTime);


        data.forEach(datas -> {
            BigDecimal inCome = incomeMap.get(datas.getTxHash());
            if(null == inCome) datas.setIncome(BigDecimal.ZERO);
            else datas.setIncome(inCome);
           /* if(null != consensusMap.get(datas.getNodeId())) {
                datas.setFlag(1);
            }else {
                if(null != historyMap.get(datas.getNodeId())) datas.setFlag(0);
            }*/
        });

        returnData.setTrades(data);
        return returnData;
    }

    @Override
    public AccountDetail getAccountDetail (String address, String chainId) {
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
