package com.platon.browser.config;

import org.springframework.stereotype.Service;

import com.platon.browser.redis.JedisClusterManager;
import com.platon.browser.redis.RedisCommands;

@Service
public class RedisFactory {

    public RedisCommands createRedisCommands(){
         return JedisClusterManager.getInstance();
    }

}
