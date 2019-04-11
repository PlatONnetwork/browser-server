package com.platon.browser.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.service.cache.BlockCacheService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Component
public class BlockCacheServiceImpl extends CacheBase implements BlockCacheService {

    private final Logger logger = LoggerFactory.getLogger(BlockCacheServiceImpl.class);

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${platon.redis.max-item}")
    private long maxItemNum;

    /**
     * 清除区块缓存
     * @param chainId
     */
    @Override
    public void clearBlockCache(String chainId) {
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 更新区块缓存
     * @param chainId
     */
    @Override
    public void updateBlockCache(String chainId, Set<Block> items){
        if(!validateParam(chainsConfig,chainId,items))return;
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        updateCache(cacheKey,items,redisTemplate,maxItemNum);
    }

    /**
     * 重置区块缓存
     * @param chainId
     */
    @Override
    public void resetBlockCache(String chainId, boolean clearOld) {
        if(clearOld) clearBlockCache(chainId);
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        for(int i=0;i<1000;i++){
            PageHelper.startPage(i+1,500);
            List<Block> data = blockMapper.selectByExample(condition);
            if(data.size()==0) break;
            updateBlockCache(chainId,new HashSet<>(data));
        }
    }

    /**
     * 根据页数和每页大小获取区块的缓存分页
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public RespPage<BlockListItem> getBlockPage(String chainId, int pageNum, int pageSize){
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        CachePageInfo<BlockListItem> cpi = getCachePageInfo(cacheKey,pageNum,pageSize, BlockListItem.class,i18n,redisTemplate,maxItemNum);
        RespPage<BlockListItem> page = cpi.page;
        List<BlockListItem> blocks = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cpi.data.forEach(str -> {
            Block initData = JSON.parseObject(str, Block.class);
            BlockListItem bean = new BlockListItem();
            bean.init(initData);
            bean.setServerTime(serverTime);
            blocks.add(bean);
        });
        page.setData(blocks);
        // 设置总记录大小
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,0,0);
        if(cache.size()>0){
            Block block = JSON.parseObject(cache.iterator().next(), Block.class);
            page.setDisplayTotalCount(block.getNumber()==null?0:block.getNumber().intValue());
        }else{
            page.setDisplayTotalCount(0);
        }
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
    public List<BlockPushItem> getBlockPushCache(String chainId, int pageNum, int pageSize){
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        List<BlockPushItem> returnData = new LinkedList<>();
        cache.forEach(str -> {
            Block initData = JSON.parseObject(str, Block.class);
            BlockPushItem bean = new BlockPushItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }
}
