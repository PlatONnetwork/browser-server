package com.platon.browser.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
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
import com.platon.browser.util.RedisPipleTool;
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
    private Integer addressTransMaxItem;
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


    enum KeyTemplatePlaceholders{
        ADDRESS("{address}"),
        TX_TYPE("{txType}"),
        TX_HASH("{txHash}"),
        TIMESTAMP("{timestamp}");
        public String code;
        KeyTemplatePlaceholders(String code){
            this.code=code;
        }
    }
    /**
     * 交易类型按地址分类存储到缓存中
     * @param chainId
     * @param transactions
     */
    @Override
    public void classifyByAddress(String chainId, List<TransactionWithBLOBs> transactions ){
        /**
           # 按地址存储的交易的键模板
           address-trans-key-template: browser:{version}:dev:chain{}:address-trans-list::{address}:{txType}:{txHash}:{timestamp}
         */
        String keyTemplate = addressTransTemplate.replace("{}",chainId);

        Set<String> addressSet = new HashSet<>();
        transactions.forEach(transaction -> {
            Transaction tmp = new Transaction();
            BeanUtils.copyProperties(transaction,tmp);

            String commonKeyTemplate = keyTemplate.replace(KeyTemplatePlaceholders.TX_TYPE.code,transaction.getTxType())
                    .replace(KeyTemplatePlaceholders.TX_HASH.code,transaction.getHash())
                    .replace(KeyTemplatePlaceholders.TIMESTAMP.code,String.valueOf(transaction.getTimestamp().getTime()));

            String fromCacheKey = commonKeyTemplate.replace(KeyTemplatePlaceholders.ADDRESS.code,transaction.getFrom());
            redisTemplate.delete(fromCacheKey);
            redisTemplate.opsForValue().set(fromCacheKey,JSON.toJSONString(tmp));

            String toCacheKey = commonKeyTemplate.replace(KeyTemplatePlaceholders.ADDRESS.code,transaction.getTo());
            redisTemplate.delete(toCacheKey);
            redisTemplate.opsForValue().set(toCacheKey,JSON.toJSONString(tmp));

            // 记录from 和to地址到addressSet，方便后面对地址交易数量的控制
            addressSet.add(transaction.getFrom());
            addressSet.add(transaction.getTo());
        });

        // 删除旧缓存
        deleteOldCache(chainId,addressSet);
    }

    /**
     * 删除旧缓存
     * @param chainId
     * @param addressSet
     */
    private void deleteOldCache(String chainId,Set<String> addressSet) {
        String queryPatternTemplate = addressTransTemplate.replace("{}",chainId)
                .replace(KeyTemplatePlaceholders.TX_TYPE.code,"*")
                .replace(KeyTemplatePlaceholders.TX_HASH.code,"*")
                .replace(KeyTemplatePlaceholders.TIMESTAMP.code,"*");
        List<String> invalidKeyList = new ArrayList<>();
        addressSet.forEach(address->{
            String queryPattern = queryPatternTemplate.replace(KeyTemplatePlaceholders.ADDRESS.code,address);
            Set<String> keys = redisTemplate.keys(queryPattern);
            List<String> keyList = new ArrayList<>(keys);
            // 倒排
            Collections.sort(keyList,((k1, k2) -> Long.valueOf(k2.substring(k2.lastIndexOf(":")+1)).compareTo(Long.valueOf(k1.substring(k1.lastIndexOf(":")+1)))));
            int index = 1;
            for (String key:keyList){
                if(index>addressTransMaxItem) invalidKeyList.add(key);
                index++;
            }
        });
        if(invalidKeyList.size()>0) RedisPipleTool.batchDeleteByKeys(invalidKeyList,false,redisTemplate);
    }


    /**
     * 从缓存中模糊查询符合传参条件的交易列表
     * 结果按时间倒排
     * @param chainId
     */
    @Override
    public Collection<TransactionWithBLOBs> fuzzyQuery(String chainId, String addressPattern, String txTypePattern, String txHashPattern,String timestampPattern){
        addressPattern=StringUtils.isNotBlank(addressPattern)?addressPattern:"*";
        txTypePattern=StringUtils.isNotBlank(txTypePattern)?txTypePattern:"*";
        txHashPattern=StringUtils.isNotBlank(txHashPattern)?txHashPattern:"*";
        timestampPattern=StringUtils.isNotBlank(timestampPattern)?timestampPattern:"*";
        String queryPattern = addressTransTemplate.replace("{}",chainId)
                .replace(KeyTemplatePlaceholders.ADDRESS.code,addressPattern)
                .replace(KeyTemplatePlaceholders.TX_TYPE.code,txTypePattern)
                .replace(KeyTemplatePlaceholders.TX_HASH.code,txHashPattern)
                .replace(KeyTemplatePlaceholders.TIMESTAMP.code,timestampPattern);
        Set<String> keys = redisTemplate.keys(queryPattern);
        // 带有相同txHash的键去重
        Map<String,String> uniqueMap = new HashMap<>();
        keys.forEach(key->uniqueMap.put(key.split(":")[7],key));
        // 降序排序
        List<String> keyList = new ArrayList<>(uniqueMap.values());
        Collections.sort(keyList,((k1, k2) -> Long.valueOf(k2.substring(k2.lastIndexOf(":")+1)).compareTo(Long.valueOf(k1.substring(k1.lastIndexOf(":")+1)))));
        // 取中前addressTransMaxItem条记录作为批量查询值的目标键
        List<String> validKeys = new ArrayList<>();
        keyList.forEach(key->{
            if(validKeys.size()<addressTransMaxItem) validKeys.add(key);
        });
        Map<String,TransactionWithBLOBs> result = RedisPipleTool.batchQueryByKeys(validKeys,false,TransactionWithBLOBs.class,redisTemplate);
        if(result==null) return Collections.EMPTY_LIST;
        List<TransactionWithBLOBs> returnData = new ArrayList<>(result.values());
        Collections.sort(returnData,((t1, t2) -> Long.valueOf(t2.getTimestamp().getTime()).compareTo(t1.getTimestamp().getTime())));
        return returnData;
    }

    /**
     * 交易列表保留有效数据200条
     *
     * @param address chainId
     */
   /* @Override
    public void retentionValidData ( String address, String chainId ) {
        String caKey = addressTransTemplate.replace("{}", chainId);
        caKey.replace("{address}", address)
                .replace("{txType}", "*")
                .replace("{txHash}", "*")
                .replace("{timestamp}", "*");
        List <String> keys = new ArrayList <>();
        keys.add(caKey);
        Map <String, Transaction> result = RedisPipleTool.batchQueryByKeys(keys, false, Transaction.class, redisTemplate);
        List <Transaction> returnData = new ArrayList <>(result.values());
        //时间降排序
        Collections.sort(returnData, (( t1, t2 ) -> Long.valueOf(t2.getTimestamp().getTime()).compareTo(t1.getTimestamp().getTime())));
        //删除多余的
        List<String> hashList = new ArrayList <>();
        for (int i = 0; i < returnData.size(); i++) {
            if (i >= 200) {
                hashList.add(returnData.get(i).getHash());
            }
        }
        //todo:delete

        //RedisPipleTool.batchDeleteByKeys();
    }*/


}
