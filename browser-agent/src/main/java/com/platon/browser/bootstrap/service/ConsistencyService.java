package com.platon.browser.bootstrap.service;


import com.platon.browser.common.service.elasticsearch.EsBlockService;
import com.platon.browser.common.service.redis.RedisBlockService;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: MySQL/ES/Redis启动一致性服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class ConsistencyService {

    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private EsBlockService esBlockService;
    @Autowired
    private RedisBlockService redisBlockService;

    public void synchronize(){
        // TODO: MySQL/ES/Redis启动一致性检查功能
    }
}
