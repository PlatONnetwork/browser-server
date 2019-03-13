//package com.platon.browser.redis;
//
//import com.alibaba.fastjson.JSON;
//import com.platon.browser.bean.Ticket;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.connection.StringRedisConnection;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.util.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class RedisQueryTest {
//
//    private static Logger logger = LoggerFactory.getLogger(RedisQueryTest.class);
//
//    @Autowired
//    protected RedisTemplate<String,String> redisTemplate;
//
//    private String keyPrefix="browser:0.7.0:test:chain1:ticket:{blockNumber}:{txHash}:{ticketId}";
//
//    @Test
//    public void queryTest() throws FileNotFoundException {
//        BufferedReader br = new BufferedReader(new FileReader("D:\\Workspace\\Upgrade\\browser-server\\server\\src\\test\\resources\\tickets.json"));
//        StringBuilder sb = new StringBuilder();
//        br.lines().forEach(line->sb.append(line));
//        List<Ticket> tickets = JSON.parseArray(sb.toString(),Ticket.class);
//
//        //logger.error("{}",tickets);
//
//        tickets.forEach(ticket -> {
//            String cacheKey = keyPrefix.replace("{blockNumber}",ticket.getBlockNumber().toString())
//                    .replace("{txHash}",ticket.getTxHash())
//                    .replace("{ticketId}",ticket.getTicketId());
//            redisTemplate.delete(cacheKey);
//            redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(ticket));
//        });
//
//        Set<String> keys = redisTemplate.keys("browser:0.7.0:test:chain1:ticket*");
//        Map<String,Ticket> result = batchQueryByKeys(new ArrayList<>(keys),false,Ticket.class);
//        System.out.println(result);
//
//    }
//
//
//    public <T> Map<String,T> batchQueryByKeys(List<String> keys,Boolean useParallel,Class<T> clazz){
//        if(null == keys || keys.size() == 0 ){
//            return null;
//        }
//
//        if(null == useParallel){
//            useParallel = true;
//        }
//
//        List<Object> results = redisTemplate.executePipelined( (RedisCallback<Object>) connection -> {
//            StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
//            keys.forEach(key->stringRedisConn.get(key));
//            return null;
//        });
//        if(null == results || results.size() == 0 ){return null;}
//
//        Map<String,T> resultMap  =  null;
//
//        if(useParallel){
//            Map<String,T> resultMapOne  = Collections.synchronizedMap(new HashMap<>());
//            keys.parallelStream().forEach(key -> {
//                String res = (String)results.get(keys.indexOf(key));
//                resultMapOne.put(key,JSON.parseObject(res,clazz));
//            });
//            resultMap = resultMapOne;
//        }else{
//            Map<String,T> resultMapTwo  = new HashMap<>();
//            keys.forEach(key-> {
//                String res = (String)results.get(keys.indexOf(key));
//                resultMapTwo.put(key,JSON.parseObject(res,clazz));
//            });
//            resultMap = resultMapTwo;
//        }
//        return  resultMap;
//    }
//
//
//}
//
