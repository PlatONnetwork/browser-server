package com.platon.browser.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.CustomNodeRankingMapper;
import com.platon.browser.dao.mapper.CustomTransactionMapper;
import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.dto.app.node.AppUserNodeDto;
import com.platon.browser.dto.app.transaction.AppTransactionSummaryDto;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.app.AppUserNodeListReq;
import com.platon.browser.service.ApiService;
import com.platon.browser.service.app.AppNodeService;
import com.platon.browser.util.CacheTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;
import org.web3j.platon.contracts.TicketContract;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:42
 */
@Service
public class AppNodeServiceImpl implements AppNodeService {

    private final Logger logger = LoggerFactory.getLogger(AppNodeServiceImpl.class);
    @Autowired
    private PlatonClient platon;
    @Autowired
    private CustomNodeRankingMapper customNodeRankingMapper;
    @Autowired
    private CustomTransactionMapper customTransactionMapper;

    @Autowired
    private ApiService apiService;

    @Override
    public AppNodeListWrapper list(String chainId) throws Exception {
        AppNodeListWrapper nodes = CacheTool.CHAINID_NODES_MAP.get(chainId);
        if(nodes==null) {
            updateNodeCache(chainId);
            nodes = CacheTool.CHAINID_NODES_MAP.get(chainId);
        }
        return nodes;
    }

    @Override
    public AppNodeDetailDto detail(String chainId, String nodeId) {
        AppNodeDetailDto node = customNodeRankingMapper.detailByChainIdAndNodeId(chainId,nodeId);
        return node;
    }

    @Override
    public List<AppUserNodeDto> getUserNodeList(String chainId, AppUserNodeListReq req) throws Exception {

        long beginTime = System.currentTimeMillis();
        long startTime = beginTime;
        // 从交易表中统计出请求中的钱包列表参与投票的节点总票数，按节点ID分组
        List<AppTransactionSummaryDto> summaries = customTransactionMapper.summaryByAddress(chainId, TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code,req.getWalletAddrs());
        logger.debug("summaryByAddress() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        List<String> nodeIds = new ArrayList<>();

        List<AppUserNodeDto> userNodeDtos = Collections.EMPTY_LIST;
        if(summaries.size()>0){

            Map<String,AppTransactionSummaryDto> summaryMap = new HashMap<>();

            Map<String,String> hashPriceMap = new HashMap<>();
            Set<String> hashSet = new HashSet<>();
            summaries.forEach(summary->{
                String nodeId = summary.getNodeId();
                nodeIds.add(nodeId);
                summaryMap.put(nodeId,summary);

                // 交易hash与当时票价的映射
                String [] hashes = summary.getHashes().split(",");
                Arrays.asList(hashes).forEach(hashPrice->{
                    if(!hashPrice.contains("-")) return;
                    String [] tmp = hashPrice.split("-");
                    hashPriceMap.put(tmp[0],tmp[1]);
                    // 把交易hash存储于每个汇总中，方便后面取有效票数累加
                    summary.getHashSet().add(tmp[0]);
                });
                // 收集所有交易HASh，用于批量查询投票数
                hashSet.addAll(hashPriceMap.keySet());
            });

            List<String> txHashes = new ArrayList<>(hashSet);

            // 调链批量查询有效票数
            beginTime = System.currentTimeMillis();
            Map<String,Integer> validVoteMap = apiService.getVailInfo(new ArrayList<>(txHashes), chainId);
            logger.debug("apiService.getVailInfo(txHashes, chainId) Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            // 调链批量查询收益
            beginTime = System.currentTimeMillis();
            Map<String, BigDecimal> incomeMap = apiService.getIncome(chainId,txHashes);
            logger.debug("Calculate income amount Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            // 累加计算
            summaries.forEach(summary->{
                try {
                    long timeMillis = System.currentTimeMillis();
                    summary.getHashSet().forEach(txHash->{
                        Integer validCount = validVoteMap.get(txHash);
                        // 累加有效票数
                        if(validCount!=null) summary.setValidCountSum(summary.getValidCountSum()+validCount);
                        // 累加计算锁定金额
                        String priceStr = hashPriceMap.get(txHash);
                        if(priceStr!=null){
                            BigInteger locked = BigInteger.valueOf(validCount).multiply(new BigInteger(priceStr));
                            summary.setLocked(summary.getLocked().add(locked));
                        }
                        // 累加计算收益
                        BigDecimal income = incomeMap.get(txHash);
                        if(income!=null) summary.setEarnings(summary.getEarnings().add(income));
                    });
                    logger.debug("Calculate ticket count and income Time Consuming: {}ms",System.currentTimeMillis()-timeMillis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // 从节点表查询节点名称和国家代码
            if(nodeIds.size()>0){
                beginTime = System.currentTimeMillis();
                userNodeDtos = customNodeRankingMapper.getNodeListByNodeIds(chainId,nodeIds);
                logger.debug("getNodeListByNodeIds() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
            }

            // 设置与当前钱包相关的总投票数
            beginTime = System.currentTimeMillis();
            userNodeDtos.forEach(node->{
                AppTransactionSummaryDto summary = summaryMap.get(node.getNodeId());
                if(summary!=null){
                    node.setTotalTicketNum(String.valueOf(summary.getVoteCountSum()));
                    node.setValidNum(String.valueOf(summary.getValidCountSum()));
                    node.setTransactionTime(summary.getLastVoteTime());
                    node.setLocked(summary.getLocked().toString());
                    node.setEarnings(summary.getEarnings().toString());
                }
            });
            logger.debug("Total ticket count setup Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        }

        logger.debug("Total Time Consuming: {}ms",System.currentTimeMillis()-startTime);
        return userNodeDtos;
    }

    @Override
    public void updateNodeCache(String chainId){
        logger.debug("list() begin");
        long beginTime = System.currentTimeMillis();
        long startTime = beginTime;
        List<AppNodeDto> returnData = customNodeRankingMapper.selectByChainIdAndIsValidOrderByRanking(chainId,1);
        logger.debug("selectByChainIdAndIsValidOrderByRanking() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        AppNodeListWrapper nodes = new AppNodeListWrapper();
        nodes.setList(returnData);

        beginTime = System.currentTimeMillis();
        TicketContract ticketContract = platon.getTicketContract(chainId);
        try {
            String price = ticketContract.GetTicketPrice().send();
            nodes.setTicketPrice(price);
            logger.debug("GetTicketPrice() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            CacheTool.TICKET_PRICE_MAP.put(chainId,price);

            // 从链上查投票数
            if(returnData.size()>0){
                beginTime = System.currentTimeMillis();
                StringBuilder ids = new StringBuilder();
                List<String> nodeIds = new ArrayList<>();
                returnData.forEach(node->{
                    ids.append(node.getNodeId()).append(":");
                    nodeIds.add(node.getNodeId());
                    nodes.setVoteCount(nodes.getVoteCount()+Long.valueOf(node.getTicketCount()));
                });

                beginTime = System.currentTimeMillis();
                // 从交易表中查询总投票数量
                Long totalVoteCount = customTransactionMapper.getTotalVoteCountByNodeIds(chainId,nodeIds);
                nodes.setTotalCount(totalVoteCount==null?0:totalVoteCount);
                logger.debug("getVoteCountByNodeIds() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
            }
            logger.debug("Total Time Consuming: {}ms",System.currentTimeMillis()-startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CacheTool.CHAINID_NODES_MAP.put(chainId,nodes);
    }
}
