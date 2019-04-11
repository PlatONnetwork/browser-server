package com.platon.browser.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;
import com.platon.browser.service.cache.StatisticCacheService;
import com.platon.browser.service.cache.TransactionCacheService;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class TransactionCacheServiceImpl extends CacheBase implements TransactionCacheService {

    private final Logger logger = LoggerFactory.getLogger(TransactionCacheServiceImpl.class);

    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    @Value("${platon.redis.max-item}")
    private long maxItemNum;
    @Value("${platon.redis.key.address-trans-key-template}")
    private String addressTransTemplate;
    @Value("${platon.redis.key.address-trans-max-item}")
    private String addressTransMaxItem;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private StatisticCacheService statisticCacheService;

    /**
     * 清除首页统计缓存
     * @param chainId
     */
    @Override
    public void clearTransactionCache(String chainId) {
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 更新交易缓存
     * @param chainId
     */
    @Override
    public void updateTransactionCache(String chainId, Set<Transaction> items){
        if(!validateParam(chainsConfig,chainId,items))return;
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        updateCache(cacheKey,items,redisTemplate,maxItemNum);
    }

    /**
     * 重置交易缓存
     * @param chainId
     */
    @Override
    public void resetTransactionCache(String chainId,boolean clearOld) {
        if(clearOld) clearTransactionCache(chainId);
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("block_number desc,transaction_index desc");
        for(int i=0;i<500;i++){
            PageHelper.startPage(i+1,1000);
            List<Transaction> data = transactionMapper.selectByExample(condition);
            if(data.size()==0) break;
            updateTransactionCache(chainId,new HashSet<>(data));
        }
    }

    /**
     * 根据页数和每页大小获取交易的缓存分页
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public RespPage<TransactionListItem> getTransactionPage(String chainId, int pageNum, int pageSize){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        CachePageInfo<TransactionListItem> cpi = getCachePageInfo(cacheKey,pageNum,pageSize, TransactionListItem.class,i18n,redisTemplate,maxItemNum);
        RespPage<TransactionListItem> page = cpi.page;
        List<TransactionListItem> transactions = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cpi.data.forEach(str->{
            TransactionListItem bean = new TransactionListItem();
            Transaction transaction = JSON.parseObject(str, Transaction.class);
            bean.init(transaction);
            bean.setServerTime(serverTime);
            transactions.add(bean);
        });
        page.setData(transactions);
        // 设置总记录大小
//        Long displayCount = transactionMapper.countByExample(new TransactionExample());
        Long displayCount = statisticCacheService.getTransCount(chainId);
        page.setDisplayTotalCount(displayCount.intValue());
        return page;
    }

    /**
     * 获取区块推送数据
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<TransactionPushItem> getTransactionPushCache(String chainId, int pageNum, int pageSize){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        List<TransactionPushItem> returnData = new LinkedList<>();
        cache.forEach(str -> {
            Transaction initData = JSON.parseObject(str, Transaction.class);
            TransactionPushItem bean = new TransactionPushItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }

    /**
     * 交易类型按地址分类存储到缓存中
     * @param chainId
     * @param transactions
     */
    @Override
    public void classifyByAddress(String chainId, List<TransactionWithBLOBs> transactions ){
        /**
         * # 按地址存储交易的键前缀
           address-trans-prefix: browser:${version}:${profile}:chain{}:tran-list
           # 按地址存储的交易的键模板
           address-trans-key-template: ${platon.redis.key.address-trans-prefix}:{address}:{txType}:{txHash}:{timestamp}
         */
        String keyTemplate = addressTransTemplate.replace("{}",chainId);
        transactions.forEach(transaction -> {
            String commonKeyTemplate = keyTemplate.replace("{txType}",transaction.getTxType())
                    .replace("txHash",transaction.getHash())
                    .replace("{timestamp}",String.valueOf(transaction.getTimestamp().getTime()));

            String fromCacheKey = commonKeyTemplate.replace("{address}",transaction.getFrom());
            redisTemplate.opsForValue().set(fromCacheKey,JSON.toJSONString(transaction));

            String toCacheKey = commonKeyTemplate.replace("{address}",transaction.getTo());

            Transaction tmp = new Transaction();
            BeanUtils.copyProperties(transaction,tmp);
            redisTemplate.opsForValue().set(toCacheKey,JSON.toJSONString(tmp));
        });
    }

    /**
     * 从缓存中模糊查询符合传参条件的交易列表
     * @param chainId
     */
    @Override
    public Collection<Transaction> fuzzyQuery(String chainId, String addressPattern, String txTypePattern, String txHashPattern,String timestampPattern){
        String queryPattern = addressTransTemplate.replace("{}",chainId);
        queryPattern=StringUtils.isNotBlank(addressPattern)?queryPattern.replace("{address}",addressPattern):queryPattern.replace("{address}","*");
        queryPattern=StringUtils.isNotBlank(txTypePattern)?queryPattern.replace("{txType}",txTypePattern):queryPattern.replace("{txType}","*");
        queryPattern=StringUtils.isNotBlank(txTypePattern)?queryPattern.replace("{txHash}",txHashPattern):queryPattern.replace("{txHash}","*");
        queryPattern=StringUtils.isNotBlank(txTypePattern)?queryPattern.replace("{timestamp}",timestampPattern):queryPattern.replace("{timestamp}","*");
        Set<String> keys = redisTemplate.keys(queryPattern);
        Map<String,Transaction> result = batchQueryByKeys(new ArrayList<>(keys),false,Transaction.class,redisTemplate);
        return result.values();
    }
}
