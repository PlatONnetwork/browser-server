package com.platon.browser.service;

import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.EsBlockRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.redis.RedisBlockService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class BlockSyncService {

    /**
     * 同步区块
     */
    @Getter
    @Setter
    private static volatile boolean done =false;
    @Autowired
    private EsBlockRepository esRepository;
    @Autowired
    private RedisBlockService redisService;
    @Value("${paging.block.page-size}")
    private int pageSize;
    @Value("${paging.block.page-count}")
    private int pageCount;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void sync(){
        ESQueryBuilderConstructor blockConstructor = new ESQueryBuilderConstructor();
        blockConstructor.setDesc("num");
        // 分页查询区块数据
        ESResult<Block> esResult=null;
        for (int pageNo = 0; pageNo <= pageCount; pageNo++) {
            try {
                esResult = esRepository.search(blockConstructor, Block.class, pageNo, pageSize);
            } catch (Exception e) {
                if(e.getMessage().contains("all shards failed")) {
                    break;
                }else {
                    log.error("【syncBlock()】查询ES出错:",e);
                }
            }
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            List<Block> blocks = esResult.getRsData();
            try{
                redisService.save(new HashSet<>(blocks),false);
                log.info("【syncBlock()】第{}页,{}条记录",pageNo,blocks.size());
            }catch (Exception e){
                log.error("【syncBlock()】同步区块到Redis出错:",e);
                throw e;
            }
            // 所有数据不够一页大小，退出
            if(blocks.size()<pageSize) break;
        }
        done=true;
    }
}
