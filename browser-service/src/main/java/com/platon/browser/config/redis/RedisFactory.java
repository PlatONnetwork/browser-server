package com.platon.browser.config.redis;

import org.springframework.stereotype.Service;

@Service
public class RedisFactory {

    public RedisCommands createRedisCommands(){
         return JedisClusterManager.getInstance();
    }

}
