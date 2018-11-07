package com.platon.browser.service.impl;

import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.PendingTxExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CalculateMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.SearchParam;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.query.Query;
import com.platon.browser.dto.transaction.PendingTxDetail;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.enums.ChainEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.service.*;
import com.platon.browser.util.LimitQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存服务
 * 提供首页节点信息、统计信息、区块信息、交易信息
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PendingTxService pendingTxService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CalculateMapper calculateMapper;

    private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private Map<ChainEnum,List<NodeInfo>> nodeInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,IndexInfo> indexInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,StatisticInfo> statisticInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,LimitQueue<BlockInfo>> blockInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,LimitQueue<TransactionInfo>> transactionInfoMap = new ConcurrentHashMap<>();

    public CacheServiceImpl(){
        Arrays.asList(ChainEnum.values()).forEach(chainId -> {
            nodeInfoMap.put(chainId,new ArrayList<>());
            indexInfoMap.put(chainId,new IndexInfo());
            statisticInfoMap.put(chainId,new StatisticInfo());
            blockInfoMap.put(chainId,new LimitQueue<>(10));
            transactionInfoMap.put(chainId,new LimitQueue<>(10));
        });
    }

    @Override
    public List<NodeInfo> getNodeInfoList(ChainEnum chainId) {
        return Collections.unmodifiableList(nodeInfoMap.get(chainId));
    }

    @Override
    public void updateNodeInfoList(List<NodeInfo> nodeInfos,boolean override, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的节点缓存",chainId.desc,chainId.code);
        List<NodeInfo> cache = nodeInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                cache.clear();
            }
            cache.addAll(nodeInfos);
        }
    }

    @Override
    public IndexInfo getIndexInfo(ChainEnum chainId) {
        return indexInfoMap.get(chainId);
    }

    @Override
    public void updateIndexInfo(IndexInfo indexInfo, boolean override, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的指标缓存",chainId.desc,chainId.code);
        IndexInfo cache = indexInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                BeanUtils.copyProperties(indexInfo,cache);
            }else{
                if(StringUtils.isNotBlank(indexInfo.getNode())){
                    cache.setNode(indexInfo.getNode());
                }
                if(indexInfo.getCurrentHeight()!=0){
                    cache.setCurrentHeight(indexInfo.getCurrentHeight());
                }
                if(indexInfo.getConsensusNodeAmount()!=0){
                    cache.setConsensusNodeAmount(cache.getConsensusNodeAmount()+indexInfo.getConsensusNodeAmount());
                }
            }
        }
    }

    @Override
    public StatisticInfo getStatisticInfo(ChainEnum chainId) {
        StatisticInfo cache = statisticInfoMap.get(chainId);
        StatisticInfo copy = new StatisticInfo();
        BeanUtils.copyProperties(cache,copy);
        return copy;
    }

    @Override
    public void updateStatisticInfo(StatisticInfo statisticInfo, boolean override, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的统计缓存",chainId.desc,chainId.code);
        StatisticInfo cache = statisticInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                BeanUtils.copyProperties(statisticInfo,cache);
            }else{
                if(statisticInfo.getBlockCount()!=0){
                    cache.setBlockCount(cache.getBlockCount()+statisticInfo.getBlockCount());
                }
                if(statisticInfo.getTransactionCount()!=0){
                    cache.setCurrent(cache.getCurrent()+statisticInfo.getCurrent());
                    cache.setTransactionCount(cache.getTransactionCount()+statisticInfo.getTransactionCount());
                }
                if(statisticInfo.getBlockCount()!=0||statisticInfo.getTransactionCount()!=0){
                    cache.setAvgTransaction(BigDecimal.valueOf(cache.getTransactionCount()/cache.getBlockCount()));
                }
                if(statisticInfo.getHighestBlockNumber()!=0){
                    cache.setHighestBlockNumber(statisticInfo.getHighestBlockNumber());
                    cache.setAvgTime((cache.getHighestBlockNumber()-cache.getLowestBlockNumber())/cache.getHighestBlockNumber());
                }
                if(statisticInfo.getDayTransaction()!=0){
                    cache.setDayTransaction(cache.getDayTransaction()+statisticInfo.getDayTransaction());
                }
                if(statisticInfo.getBlockStatisticList()!=null){
                    Map<Long, StatisticItem> map = new HashMap<>();
                    List<StatisticItem> cacheStatisticItemList = cache.getBlockStatisticList();
                    cacheStatisticItemList.forEach(statisticItem -> map.put(statisticItem.getHeight(),statisticItem));
                    statisticInfo.getBlockStatisticList().forEach(statisticItem -> {
                        StatisticItem item = map.get(statisticItem.getHeight());
                        if(item==null){
                            cacheStatisticItemList.add(statisticItem);
                        }else{
                            item.setTransaction(item.getTransaction()+statisticItem.getTransaction());
                        }
                    });
                }
            }
        }
    }

    @Override
    public List<BlockInfo> getBlockInfoList(ChainEnum chainId) {
        LimitQueue<BlockInfo> cache = blockInfoMap.get(chainId);
        return Collections.unmodifiableList(cache.elements());
    }

    @Override
    public void updateBlockInfoList(List<BlockInfo> blockInfos, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的块列表缓存",chainId.desc,chainId.code);
        LimitQueue<BlockInfo> cache = blockInfoMap.get(chainId);
        synchronized (cache){
            blockInfos.forEach(e->cache.offer(e));
        }
    }

    @Override
    public List<TransactionInfo> getTransactionInfoList(ChainEnum chainId) {
        LimitQueue<TransactionInfo> cache = transactionInfoMap.get(chainId);
        return Collections.unmodifiableList(cache.elements());
    }

    @Override
    public void updateTransactionInfoList(List<TransactionInfo> transactionInfos, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的交易列表缓存",chainId.desc,chainId.code);
        LimitQueue<TransactionInfo> cache = transactionInfoMap.get(chainId);
        synchronized (cache){
            transactionInfos.forEach(e->cache.offer(e));
        }
    }

    @Override
    public Query findInfoByParam ( SearchParam param ) {
        //以太坊内部和外部账户都是20个字节，0x开头，string长度40,加上0x
        //以太坊区块hash和交易hash都是0x打头长度33
        //1.判断是否是块高
        //2.判断是否是地址
        //3.不是以上两种情况，就为交易hash或者区块hash，需要都查询
        Query query = new Query();
        boolean isAccountOrContract = false;
        boolean isHash = false;
        String par = param.getParameter();
        boolean result=param.getParameter().matches("[0-9]+");
        if (!result) {
            //为false则可能为区块交易hash或者为账户
            if(par.length()<=2){
                throw new BusinessException("请输入长度大于2的查询关键字!");
            }
            if(par.substring(0, 2).equals("0x") && par.length() == 42){
                isAccountOrContract = true;
            }else
                isHash = true;

        }

        if (isAccountOrContract) {
            //内部账户account
            long accountSum = calculateMapper.countTransactionOrContract("account",param.getCid(),param.getParameter(),param.getParameter());
            if(accountSum > 0){
                AccountDetailReq adr = new AccountDetailReq();
                adr.setCid(param.getCid());
                adr.setAddress(param.getParameter());
                AccountDetail detail = accountService.getAccountDetail(adr);
                query.setStruct(detail);
                query.setType("account");
            }else {
                //外部账户contract
                long addressSum = calculateMapper.countTransactionOrContract("contract",param.getCid(),param.getParameter(),param.getParameter());
                if(addressSum > 0){
                    ContractDetailReq contractDetailReq = new ContractDetailReq();
                    contractDetailReq.setCid(param.getCid());
                    contractDetailReq.setAddress(param.getParameter());
                    ContractDetail detail = contractService.getContractDetail(contractDetailReq);
                    query.setStruct(detail);
                    query.setType("contract");
                }
            }
            return query;
        }

        if (isHash) {
            //交易hash或者区块hash
            long transactionSum = calculateMapper.countTransaction(param.getParameter(),param.getCid());
            if(transactionSum > 0){
                TransactionDetailReq transactionDetailReq = new TransactionDetailReq();
                transactionDetailReq.setCid(param.getCid());
                transactionDetailReq.setTxHash(param.getParameter());
                TransactionDetail transactionDetail = transactionService.getTransactionDetail(transactionDetailReq);
                query.setStruct(transactionDetail);
                query.setType("transaction");
            }else {
                long blockSum = calculateMapper.countBlock(param.getParameter(),param.getCid());
                if(blockSum > 0){
                    BlockExample blockExample = new BlockExample();
                    blockExample.createCriteria().andChainIdEqualTo(param.getCid()).andHashEqualTo(param.getParameter());
                    List<Block> blocks = blockMapper.selectByExample(blockExample);
                    BlockDetail blockDetail = new BlockDetail();
                    Block block = blocks.get(0);
                    BeanUtils.copyProperties(block,blockDetail);
                    blockDetail.setHeight(block.getNumber());
                    blockDetail.setTimestamp(block.getTimestamp().getTime());
                    query.setType("block");
                    query.setStruct(blockDetail);
                }
            }
            long  pendingTxSum = calculateMapper.countPendingTx(param.getParameter(),param.getCid());
            if(pendingTxSum > 0){
                PendingTxDetailReq pendingTxDetailReq = new PendingTxDetailReq();
                pendingTxDetailReq.setCid(param.getCid());
                pendingTxDetailReq.setTxHash(param.getParameter());
                PendingTxDetail pendingTxDetail = pendingTxService.getTransactionDetail(pendingTxDetailReq);
                query.setType("pending");
                query.setStruct(pendingTxDetail);
            }
            return query;
        }
        //区块高度
        query.setType("block");
        BlockDetailReq req = new BlockDetailReq();
        req.setCid(param.getCid());
        req.setHeight(Long.valueOf(param.getParameter()));
        BlockDetail blockDetail = blockService.getBlockDetail(req);
        query.setStruct(blockDetail);

        return query;
    }

    public static void main(String args[]){
        String address = "0x493301712671ada506ba6ca7891f436d29185821";
        String fix = address.substring(0,2);
        System.out.println(address.length());
        boolean a = address.substring(0, 2).equals("0x") && address.length() == 42;
        System.out.println(fix);
        System.out.println(a);
    }
}
