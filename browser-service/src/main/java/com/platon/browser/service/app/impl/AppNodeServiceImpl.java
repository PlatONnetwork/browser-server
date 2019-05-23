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
        logger.debug("list() begin");
        long beginTime = System.currentTimeMillis();
        long startTime = beginTime;
        List<AppNodeDto> returnData = customNodeRankingMapper.selectByChainIdAndIsValidOrderByRanking(chainId,1);
        logger.debug("selectByChainIdAndIsValidOrderByRanking() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        AppNodeListWrapper nodes = new AppNodeListWrapper();
        nodes.setList(returnData);

        beginTime = System.currentTimeMillis();
        TicketContract ticketContract = platon.getTicketContract(chainId);
        String price = ticketContract.GetTicketPrice().send();
        nodes.setTicketPrice(price);
        logger.debug("GetTicketPrice() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

        // 从链上查投票数
        if(returnData.size()>0){
            beginTime = System.currentTimeMillis();
            StringBuilder ids = new StringBuilder();
            List<String> nodeIds = new ArrayList<>();
            returnData.forEach(node->{
                ids.append(node.getNodeId()).append(":");
                nodeIds.add(node.getNodeId());
            });
            String idsStr = ids.toString();
            idsStr = idsStr.substring(0,idsStr.lastIndexOf(":"));
            logger.debug("Construct node ids Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            beginTime = System.currentTimeMillis();
            String countInfo = ticketContract.GetCandidateTicketCount(idsStr).send();
            logger.debug("GetCandidateTicketCount() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            beginTime = System.currentTimeMillis();
            Map<String,Integer> countMap = JSON.parseObject(countInfo, Map.class);
            countMap.forEach((k,v)->nodes.setVoteCount(nodes.getVoteCount()+v));
            logger.debug("JSON.parseObject(countInfo, Map.class) Time Consuming: {}ms",System.currentTimeMillis()-beginTime);

            beginTime = System.currentTimeMillis();
            // 从交易表中查询总投票数量
            Long totalVoteCount = customNodeRankingMapper.getVoteCountByNodeIds(chainId,nodeIds);
            nodes.setTotalCount(totalVoteCount==null?0:totalVoteCount);
            logger.debug("getVoteCountByNodeIds() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        }
        logger.debug("Total Time Consuming: {}ms",System.currentTimeMillis()-startTime);
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
            summaries.forEach(summary->{
                summary.setNodeId(summary.getNodeId().startsWith("0x")?summary.getNodeId():"0x"+summary.getNodeId());
                nodeIds.add(summary.getNodeId());
                summaryMap.put(summary.getNodeId(),summary);
            });


            // 查询
            Map<String,Long> validCountMap = new HashMap<>();
            summaries.forEach(summary->{
                try {
                    Map<String,String> hashPriceMap = new HashMap<>();
                    String [] hashes = summary.getHashes().split(",");
                    Arrays.asList(hashes).forEach(hashPrice->{
                        String [] tmp = hashPrice.split("-");
                        hashPriceMap.put(tmp[0],tmp[1]);
                    });

                    List<String> txHashes = new ArrayList<>(hashPriceMap.keySet());

                    // 取有效票数
                    long timeMillis = System.currentTimeMillis();
                    Map<String,Integer> validVoteMap = apiService.getVailInfo(txHashes, chainId);
                    logger.debug("apiService.getVailInfo(txHashes, chainId) Time Consuming: {}ms",System.currentTimeMillis()-timeMillis);
                    validVoteMap.forEach((k,v)->summary.setValidCountSum(summary.getValidCountSum()+v));

                    // 计算锁定金额
                    timeMillis = System.currentTimeMillis();
                    validCountMap.forEach((txHash,ticketCount)->{
                        String priceStr = hashPriceMap.get(txHash);
                        if(priceStr!=null){
                            BigInteger locked = BigInteger.valueOf(ticketCount).multiply(new BigInteger(priceStr));
                            summary.setLocked(summary.getLocked().add(locked));
                        }
                    });
                    logger.debug("Calculate locked amount Time Consuming: {}ms",System.currentTimeMillis()-timeMillis);

                    timeMillis = System.currentTimeMillis();
                    // 计算收益
                    Map<String, BigDecimal> incomeMap = apiService.getIncome(chainId,txHashes);
                    incomeMap.forEach((hash,income)->summary.setEarnings(summary.getEarnings().add(income)));
                    logger.debug("Calculate income amount Time Consuming: {}ms",System.currentTimeMillis()-timeMillis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                validCountMap.put(summary.getNodeId(),summary.getValidCountSum());
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
}
