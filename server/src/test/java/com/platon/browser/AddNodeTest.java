package com.platon.browser;

import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.util.GeoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServiceApplication.class, value = "spring.profiles.active=dev")
public class AddNodeTest {
    private static final Logger logger = LoggerFactory.getLogger(AddNodeTest.class);

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Test
    public void insertNode(){

        int a = 108 * 256 * 256 + 1 * 256 + 5;
        int b = 145 * 256 * 256 + 110 * 256 + 35;

        for(int i=1;i<100;i++){
            int c = new Random().nextInt(b - a) + a;
            String ip = "192." + (c / (256 * 256)) + "." + ((c / 256) % 256) + "." + (c % 256);
            logger.info("IP: {}",ip);

            Node node = new Node();
            NodeRanking nodeRanking = new NodeRanking();
            while (true){
                try {
                    Location location = GeoUtil.getLocation(ip);
                    logger.info("国家：{}", location.countryName);
                    logger.info("城市：{}", location.city);
                    logger.info("纬度：{}", location.latitude);
                    logger.info("经度：{}", location.longitude);
                    logger.info("dma code：{}", location.dma_code);
                    logger.info("area code：{}", location.area_code);
                    logger.info("country code：{}", location.countryCode);
                    node.setIp(ip);
                    nodeRanking.setIp(ip);
                    String intro = location.countryName;
                    if(location.city!=null){
                        intro+=" "+location.city;
                    }
                    nodeRanking.setIntro(intro);
                    break;
                }catch (Exception e){}
            }

            nodeRanking.setRanking(i);
            node.setId(""+i);
            nodeRanking.setId(""+i);
            node.setAddress(""+i);
            nodeRanking.setAddress(""+i);
            node.setChainId("1");
            nodeRanking.setChainId("1");
            node.setNodeStatus(1);
            if(i%2==0){
                nodeRanking.setType(2);
            }else{
                nodeRanking.setType(1);
            }
            nodeRanking.setCreateTime(new Date());
            nodeRanking.setDeposit("3343543.6454534"+i);

            nodeRanking.setJoinTime(new Date());
            nodeRanking.setName("node-"+i);
            node.setPort(8800+i);
            nodeRanking.setPort(8800+i);
            nodeRanking.setOrgName("platon");
            nodeRanking.setOrgWebsite("https://www.platon.network");
            nodeRanking.setRewardRatio(0.01);
            nodeRanking.setUpdateTime(new Date());
            nodeMapper.insert(node);
            nodeRankingMapper.insert(nodeRanking);
        }
    }



}
