package com.platon.browser.node;

import com.maxmind.geoip.Location;
import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
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
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class NodeTest {
    private static final Logger logger = LoggerFactory.getLogger(NodeTest.class);

    @Autowired
    private NodeMapper nodeMapper;

    @Test
    public void insertNode(){

        int a = 108 * 256 * 256 + 1 * 256 + 5;
        int b = 145 * 256 * 256 + 110 * 256 + 35;

        for(int i=0;i<4;i++){
            int c = new Random().nextInt(b - a) + a;
            String ip = "192." + (c / (256 * 256)) + "." + ((c / 256) % 256) + "." + (c % 256);
            logger.info("IP: {}",ip);

            Node node = new Node();
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
                    break;
                }catch (Exception e){

                }
            }
            node.setChainId("1");
            node.setCreateTime(new Date());
            node.setNetState(1);
            node.setNodeAddress("0x493301712671ada506ba6ca7891f436d2918582"+i);
            node.setNodeId("0x493301712671ada506ba6ca7891f436d2918582"+i);
            node.setNodeName("node-"+i);
            node.setUpdateTime(new Date());
            node.setNodeType(1);
            nodeMapper.insert(node);
        }
    }



}
