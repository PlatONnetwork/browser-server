package com.platon.browser.amqp;

import com.alibaba.fastjson.JSON;
import com.platon.browser.ServiceApplication;
import com.platon.browser.common.dto.mq.Message;
import com.platon.browser.common.spring.MQSender;
import com.platon.browser.dao.entity.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServiceApplication.class, value = "spring.profiles.active=dev")
public class RabbitmqTest {

    @Autowired
    private MQSender sender;

    @Test
    public void sendAndReceive(){
        Random r = new Random();
        while (true){
            int i = r.nextInt(10);
            Node node = new Node();
            node.setChainId("chainId");
            node.setCreateTime(new Date());
            node.setIp("192.168.1.1"+i);
            node.setNodeName("dddddd");

            sender.send("chain-0001","block",node);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}