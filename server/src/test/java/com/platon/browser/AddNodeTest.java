package com.platon.browser;

import com.github.pagehelper.PageHelper;
import com.maxmind.geoip.Location;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.service.BlockService;
import com.platon.browser.util.GeoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServiceApplication.class, value = "spring.profiles.active=dev")
public class AddNodeTest {
    private static final Logger logger = LoggerFactory.getLogger(AddNodeTest.class);

    @Autowired
    private NodeMapper nodeMapper;

    @Test
    public void insertNode(){

        int a = 108 * 256 * 256 + 1 * 256 + 5;
        int b = 145 * 256 * 256 + 110 * 256 + 35;

        for(int i=10;i<30;i++){
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

           /* node.setId("0000"+i);
            node.setAddress("00011"+i);
            node.setBlockCount(456+i);
            node.setChainId("100");
            if(i%2==0){
                node.setType(2);
            }else{
                node.setType(1);
            }
            node.setCreateTime(new Date());
            node.setDeposit("3343543.6454534"+i);
            node.setElectionStatus(1);
            node.setNodeStatus(1);
            node.setIntro("my node "+i);

            node.setJoinTime(new Date());
            node.setName("node-00"+i);
            node.setPort(8800+i);
            node.setOrgName("platon");
            node.setOrgWebsite("https://www.platon.network");
            node.setProfitAmount("3343.2323"+i);
            node.setRewardAmount("33.334434"+i);
            node.setPublicKey("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4dE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4dE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4dcc8DEf"+i);
            node.setRanking(i);
            node.setRewardRatio(0.01);
            node.setVerifyCount(6433l+i);
            node.setWallet("0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf"+i);
            node.setUpdateTime(new Date());*/
            nodeMapper.insert(node);
        }
    }



}
