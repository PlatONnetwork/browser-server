package com.platon.browser.service;

import com.platon.browser.config.RedisKeyConfig;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.response.RespPage;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * 基础封装缓存获取逻辑
 * @Auther: Chendongming
 * @Date: 2019/4/11 15:43
 * @Description:
 */
@Slf4j
public class CacheBase {
    @Resource
    protected RedisKeyConfig redisKeyConfig;
    @Resource
    protected RedisTemplate<String,String> redisTemplate;
    @Resource
    protected I18nUtil i18n;

    private final Logger logger = LoggerFactory.getLogger(CacheBase.class);


    protected boolean validateParam(Collection<?> items){
        if(items.isEmpty()){
            // 无更新内容
            logger.debug("Empty Content");
            return false;
        }
        return true;
    }

    protected static class CachePageInfo<T>{
        Set<String> data;
        RespPage<T> page;
    }
    
    protected <T> CachePageInfo <T> getCachePageInfo(String cacheKey,int pageNum,int pageSize){
        RespPage<T> page = new RespPage<>();
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));
        CachePageInfo<T> cpi = new CachePageInfo<>();
        Long pagingTotalCount = redisTemplate.opsForZSet().size(cacheKey);
        if(pagingTotalCount==null) pagingTotalCount=0L;
        if(pagingTotalCount>redisKeyConfig.getMaxItem()){
            // 如果缓存数量大于maxItemNum，则以maxItemNum作为分页数量
            pagingTotalCount = redisKeyConfig.getMaxItem();
        }
        page.setTotalCount(pagingTotalCount);

        long pageCount = pagingTotalCount/pageSize;
        if(pagingTotalCount%pageSize!=0){
            pageCount+=1;
        }
        page.setTotalPages(pageCount);

        // Redis的缓存分页从索引0开始
        if(pageNum<=0){
            pageNum=1;
        }
        if(pageSize<=0){
            pageSize=1;
        }
        long start = (pageNum-1L)*pageSize;
        long end = (pageNum*pageSize)-1L;
        cpi.data = redisTemplate.opsForZSet().reverseRange(cacheKey,start,end);
//        cpi.data = jedisClient.zrevrange(cacheKey, start, end);
        cpi.page = page;
        return cpi;
    }

    protected <T> CachePageInfo <T> getCachePageInfoByStartEnd(String cacheKey,long start,long end,RedisTemplate<String,String> redisTemplate, long maxItemNum){
        RespPage<T> page = new RespPage<>();
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));

        CachePageInfo<T> cpi = new CachePageInfo<>();
        Long pagingTotalCount = redisTemplate.opsForZSet().size(cacheKey);
        if(pagingTotalCount!=null&&pagingTotalCount>maxItemNum){
            // 如果缓存数量大于maxItemNum，则以maxItemNum作为分页数量
            pagingTotalCount = maxItemNum;
        }
        page.setTotalCount(pagingTotalCount==null?0L:pagingTotalCount);

        cpi.data = redisTemplate.opsForZSet().reverseRange(cacheKey, start, end);
        cpi.page = page;
        return cpi;
    }

    protected <T> CachePageInfo <T> getCachePageInfoByStartEnd(String cacheKey,long start,long end){
        RespPage<T> page = new RespPage<>();
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));

        CachePageInfo<T> cpi = new CachePageInfo<>();
//        cpi.data = jedisClient.zrevrange(cacheKey, start, end);
        cpi.data = redisTemplate.opsForZSet().reverseRange(cacheKey,start,end);
        cpi.page = page;
        return cpi;
    }
}
