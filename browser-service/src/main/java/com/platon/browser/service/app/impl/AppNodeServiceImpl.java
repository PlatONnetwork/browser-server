package com.platon.browser.service.app.impl;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.CustomNodeRankingMapper;
import com.platon.browser.dao.mapper.CustomTransactionMapper;
import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.dto.app.transaction.AppUserNodeDto;
import com.platon.browser.dto.app.transaction.AppUserNodeTransactionDto;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.app.AppUserNodeListReq;
import com.platon.browser.service.ApiService;
import com.platon.browser.service.app.AppNodeService;
import com.platon.browser.util.LocalCacheTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        AppNodeListWrapper nodes = LocalCacheTool.APP_CHAINID_NODES_MAP.get(chainId);
        if(nodes==null) {
            updateLocalNodeCache(chainId);
            nodes = LocalCacheTool.APP_CHAINID_NODES_MAP.get(chainId);
        }
        return nodes;
    }

    @Override
    public AppNodeDetailDto detail(String chainId, String nodeId) {
        AppNodeDetailDto node = customNodeRankingMapper.detailByChainIdAndNodeId(chainId,nodeId);
        return node;
    }

    @Override
    public List<com.platon.browser.dto.app.node.AppUserNodeDto> getUserNodeList(String chainId, AppUserNodeListReq req) throws Exception {

        long beginTime = System.currentTimeMillis();
        long startTime = beginTime;
        // 从交易表中统计出请求中的钱包列表参与投票的节点总票数，按节点ID分组
        List<AppUserNodeDto> userNodes = customTransactionMapper.getUserNodeByWalletAddress(chainId, TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code,req.getWalletAddrs());
        List<AppUserNodeTransactionDto> nodeTransactions = customTransactionMapper.getUserNodeTransactionByWalletAddress(chainId, TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code,req.getWalletAddrs());

        Map<String,List<String>> nodeIdTxHashListMap = new HashMap<>();
        Map<String,String> txHashPriceMap = new HashMap<>();

        nodeTransactions.forEach(tr->{
            List<String> txHashes = nodeIdTxHashListMap.get(tr.getNodeId());
            if(txHashes==null) {
                txHashes = new ArrayList<>();
                nodeIdTxHashListMap.put(tr.getNodeId(),txHashes);
            }
            txHashes.add(tr.getTxHash());
            txHashPriceMap.put(tr.getTxHash(),tr.getPrice());
        });

        logger.debug("summaryByAddress() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        List<String> nodeIds = new ArrayList<>();

        List<com.platon.browser.dto.app.node.AppUserNodeDto> userNodeDtos = Collections.EMPTY_LIST;
        if(userNodes.size()>0){

            Map<String, AppUserNodeDto> nodeMap = new HashMap<>();

            Set<String> txHashSet = new HashSet<>();
            userNodes.forEach(node->{
                String nodeId = node.getNodeId();
                nodeIds.add(nodeId);
                nodeMap.put(nodeId,node);

                // 收集所有交易HASh，用于批量查询投票数
                List<String> hashes = nodeIdTxHashListMap.get(node.getNodeId());
                if(hashes!=null) txHashSet.addAll(hashes);
            });

            List<String> txHashes = new ArrayList<>(txHashSet);

            // 调链批量查询有效票数
            beginTime = System.currentTimeMillis();
            Map<String,Integer> validVoteMap = apiService.getVailInfo(new ArrayList<>(txHashes), chainId);
            logger.debug("apiService.getVailInfo(txHashes, chainId) Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            // 调链批量查询收益
            beginTime = System.currentTimeMillis();
            Map<String, BigDecimal> incomeMap = apiService.getIncome(chainId,txHashes);
            logger.debug("Calculate income amount Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            // 累加计算
            userNodes.forEach(node->{
                try {
                    long timeMillis = System.currentTimeMillis();
                    List<String> hashForNode = nodeIdTxHashListMap.get(node.getNodeId());
                    if(hashForNode!=null) {
                        hashForNode.forEach(txHash -> {
                            Integer validCount = validVoteMap.get(txHash);
                            // 累加有效票数
                            if (validCount != null) node.setValidCountSum(node.getValidCountSum() + validCount);
                            // 累加计算锁定金额
                            String priceStr = txHashPriceMap.get(txHash);
                            if (priceStr != null) {
                                BigInteger locked = BigInteger.valueOf(validCount).multiply(new BigInteger(priceStr));
                                node.setLocked(node.getLocked().add(locked));
                            }
                            // 累加计算收益
                            BigDecimal income = incomeMap.get(txHash);
                            if (income != null) node.setEarnings(node.getEarnings().add(income));
                        });
                    }
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
                AppUserNodeDto summary = nodeMap.get(node.getNodeId());
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

    /**
     * 更新本地进程缓存
     * @param chainId
     */
    @Override
    public void updateLocalNodeCache(String chainId){
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

            LocalCacheTool.TICKET_PRICE_MAP.put(chainId,price);

            // 设置总投票数量
            nodes.setTotalCount(51200);

            if(returnData.size()>0){
                returnData.forEach(node->nodes.setVoteCount(nodes.getVoteCount()+Long.valueOf(node.getTicketCount())));
            }
            logger.debug("Total Time Consuming: {}ms",System.currentTimeMillis()-startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalCacheTool.APP_CHAINID_NODES_MAP.put(chainId,nodes);
    }
}
