package com.platon.browser.service;

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.TestDataUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class RedisCacheServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceTest.class);
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    @Value("${platon.redis.key.max-item}")
    private long maxItemNum;
    @Value("${platon.redis.key.node}")
    private String nodeCacheKeyTemplate;

    @Test
    public void updateNodeCache(){
        Set<NodeRanking> nodes = generateNodes();
        redisCacheService.updateNodeCache("1",nodes);
        String cacheKey = nodeCacheKeyTemplate.replace("{}","1");
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,0,-1);
        Assert.assertEquals(nodes.size(),cache.size());
    }


    @Test
    public void getNodeCache(){
        Set<NodeRanking> nodes = generateNodes();
        redisCacheService.updateNodeCache("1",nodes);
        List<NodeInfo> nodeInfoList = redisCacheService.getNodeList("1");
        Assert.assertEquals(nodes.size(),nodeInfoList.size());
    }


    private Set<NodeRanking> generateNodes(){
        Set<NodeRanking> nodes = new HashSet<>();

        for(int i=0;i<200;i++){
            NodeRanking node = new NodeRanking();
            while (true){
                try {
                    String ip = TestDataUtil.getForeignRandomIp();
                    if(i%2==0){
                        ip = TestDataUtil.getChinaRandomIp();
                    }
                    logger.info("IP: {}",ip);

                    CityResponse response = GeoUtil.getResponse(ip);
                    Location location = response.getLocation();
                    Country country = response.getCountry();
                    Subdivision subdivision = response.getMostSpecificSubdivision();
                    City city = response.getCity();
                    logger.info("国家：{}", country.getName());
                    logger.info("城市：{}", city.getName());
                    logger.info("纬度：{}", location.getLatitude());
                    logger.info("经度：{}", location.getLongitude());
                    logger.info("dma code：{}", country.getIsoCode());
                    logger.info("area code：{}", subdivision.getIsoCode());
                    logger.info("country code：{}", country.getIsoCode());
                    node.setIntro(country.getName().replace(" ",""));
                    if(StringUtils.isNotBlank(city.getName())){
                        node.setIntro(node.getIntro()+"."+city.getName().replace(" ",""));
                    }
                    node.setIp(ip);
                    break;
                }catch (Exception e){}
            }
            node.setChainId("1");
            node.setCreateTime(new Date());
            node.setElectionStatus(1);
            node.setAddress("0x493301712671ada506ba6ca7891f436d2918582"+i);
            node.setNodeId("0x493301712671ada506ba6ca7891f436d2918582"+i);
            node.setUpdateTime(new Date());
            node.setOrgName("platon");
            node.setOrgWebsite("https://www.platon.network/");
            node.setDeposit("87854");
            node.setPort(808+i);
            node.setJoinTime(new Date());
            node.setType(1);
            node.setRanking(i);
            node.setIsValid(1);
            node.setBeginNumber(1l);
            node.setEndNumber(199l);
            node.setBlockCount(55l);
            node.setUrl("https://www.platon.network/");

            nodes.add(node);
        }
        return nodes;
    }
}
