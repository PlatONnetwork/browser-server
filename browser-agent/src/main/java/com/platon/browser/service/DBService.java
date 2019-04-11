package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.BlockMissingExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.entity.VoteTx;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.thread.AnalyseThread;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
public class DBService {
    private static Logger logger = LoggerFactory.getLogger(DBService.class);
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BlockMissingMapper blockMissingMapper;
    @Autowired
    protected RedisCacheService redisCacheService;
    @Autowired
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private VoteTxMapper voteTxMapper;
    @Value("${platon.redis.key.tran-list-prefix}")
    private String tranListPrefix;
    @Autowired
    protected RedisTemplate <String,String> redisTemplate;

    @Transactional
    public void flush(AnalyseThread.AnalyseResult result){
        long beginTime = System.currentTimeMillis();
        if(result.blocks.size()>0){
            long blockInertBeginTime = beginTime;
            blockMapper.batchInsert(result.blocks);
            logger.debug("  |-Time Consuming(blockMapper.batchInsert()): {}ms",System.currentTimeMillis()-blockInertBeginTime);
            long updateBlockCacheBeginTime = System.currentTimeMillis();
            redisCacheService.updateBlockCache(chainId, new HashSet<>(result.blocks));
            logger.debug("  |-Time Consuming(redisCacheService.updateBlockCache): {}ms",System.currentTimeMillis()-updateBlockCacheBeginTime);
        }

        if(result.transactions.size()>0){
            transactionMapper.batchInsert(result.transactions);
            List<VoteTx> voteTxes = new ArrayList <>();
            result.transactions.forEach(transaction ->{
                if(transaction.getTxType().equals(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code)){
                    TxInfo ticketTxInfo = JSON.parseObject(transaction.getTxInfo(),TxInfo.class);
                    TxInfo.Parameter ticketParameter = ticketTxInfo.getParameters();
                    VoteTx voteTx = new VoteTx();
                    voteTx.setHash(transaction.getHash());
                    voteTx.setTotals(ticketParameter.getCount().longValue());
                    voteTx.setCompleteFlag("N");
                    voteTxes.add(voteTx);
                }
            });
            if(voteTxes.size() > 0){
                voteTxMapper.batchInsert(voteTxes);
            }
            redisCacheService.updateTransactionCache(chainId,new HashSet<>(result.transactions));
            //批量更新redis
            batchInsertTransactionList(result.transactions);
        }

//        if(result.nodes.size()>0){
//            customNodeRankingMapper.insertOrUpdate(result.nodes);
//            redisCacheService.updateNodePushCache(platon.getChainId(), new HashSet<>(result.nodes));
//        }

        if(result.errorBlocks.size()>0) {
            // 先删后插，防止重复主键导致插入失败，以及防止因为重复主键错误导致整个事务回滚
            List<Long> numbers = new ArrayList<>();
            result.errorBlocks.forEach(err->numbers.add(err.getNumber()));
            BlockMissingExample example = new BlockMissingExample();
            example.createCriteria().andChainIdEqualTo(chainId).andNumberIn(numbers);
            blockMissingMapper.deleteByExample(example);
            blockMissingMapper.batchInsert(result.errorBlocks);
        }

        long updateStatisticsCache = System.currentTimeMillis();
        redisCacheService.updateStatisticsCache(chainId);
        logger.debug("  |-Time Consuming(redisCacheService.updateStatisticsCache()): {}ms",System.currentTimeMillis()-updateStatisticsCache);

        logger.debug("Time Consuming(Total): {}ms",System.currentTimeMillis()-beginTime);
    }

    private void batchInsertTransactionList( List<TransactionWithBLOBs> transactions ){
        //tran-list-prefix: browser:${version}:${profile}:chain{}:tran-list:{from}:{to}:{txType}:{txHash}:{createTime}
        String cakey = tranListPrefix.replace("{}",chainId);
        transactions.forEach(transaction -> {
            SimpleDateFormat time = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            String CacheKey = cakey.replace("{from}",transaction.getFrom())
                    .replace("{to}",transaction.getTo())
                    .replace("{txType}",transaction.getTxType())
                    .replace("{txHash}",transaction.getHash())
                    .replace("{createTime}",time.format(transaction.getCreateTime()));
            redisTemplate.delete(CacheKey);
            redisTemplate.opsForValue().set(CacheKey,JSON.toJSONString(transaction));
        });

    }
}
