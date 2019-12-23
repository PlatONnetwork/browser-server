package com.platon.browser;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.queue.publisher.*;
import com.platon.browser.service.BlockResult;
import com.platon.browser.service.DataGenService;
import com.platon.browser.service.NodeGenService;
import com.platon.browser.service.StakeGenService;
import com.platon.browser.utils.CounterBean;
import com.platon.browser.utils.GracefullyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@EnableRetry
@EnableScheduling
@MapperScan(basePackages = {"com.platon.browser.dao.mapper"})
@SpringBootApplication
public class PressApplication implements ApplicationRunner {

    @Autowired
    private BlockPublisher blockPublisher;
    @Autowired
    private TransactionPublisher transactionPublisher;
    @Autowired
    private NodeOptPublisher nodeOptPublisher;
    @Autowired
    private AddressPublisher addressPublisher;
    @Autowired
    private NodePublisher nodePublisher;

    @Autowired
    private DataGenService dataGenService;
    @Autowired
    private NodeGenService nodeGenService;
    @Autowired
    private StakeGenService stakeGenService;

    private static Long nodeQty = 0L;
    private static Long stakeQty = 0L;
    public static void main ( String[] args ) {
        SpringApplication.run(PressApplication.class, args);
    }
    @Override
    public void run ( ApplicationArguments args ) throws IOException {
        BigInteger blockNumber = BigInteger.ZERO;
        GracefullyUtil.updateStatus();
        log.warn("加载状态文件:counter.json");
        File counterFile = FileUtils.getFile(System.getProperty("user.dir"), "counter.json");
        CounterBean counterBean = new CounterBean();
        try {
            String status = FileUtils.readFileToString(counterFile,"UTF8");
            counterBean = JSON.parseObject(status,CounterBean.class);
            blockPublisher.setTotalCount(counterBean.getBlockCount());
            transactionPublisher.setTotalCount(counterBean.getTransactionCount());
            nodeOptPublisher.setTotalCount(counterBean.getNodeOptCount());
            dataGenService.setNodeOptMaxId(counterBean.getNodeOptMaxId());
            addressPublisher.setTotalCount(counterBean.getAddressCount());
        } catch (IOException e) {
            log.warn("没有状态文件,创建一个!");
        }
        log.warn("状态加载完成:{}",JSON.toJSONString(counterBean,true));

        while (true){
            try {
                GracefullyUtil.monitor();
            } catch (GracefullyShutdownException | InterruptedException e) {
                log.warn("检测到SHUTDOWN钩子,放弃执行业务逻辑,写入当前状态...");
                CounterBean counter = new CounterBean();
                counter.setBlockCount(blockPublisher.getTotalCount());
                counter.setTransactionCount(transactionPublisher.getTotalCount());
                counter.setNodeOptCount(nodeOptPublisher.getTotalCount());
                counter.setNodeOptMaxId(dataGenService.getNodeOptMaxId());
                counter.setAddressCount(addressPublisher.getTotalCount());
                String status = JSON.toJSONString(counter,true);
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(counterFile))) {
                    bw.write(status);
                }
                log.warn("状态写入完成,可安全停机:{}",status);
                System.exit(0);
            }

            BlockResult blockResult = dataGenService.get(blockNumber);

            blockPublisher.publish(Arrays.asList(blockResult.getBlock()));
            transactionPublisher.publish(blockResult.getTransactionList());
            nodeOptPublisher.publish(blockResult.getNodeOptList());

            List <Node> nodeList = nodeGenService.buildNodeInfo(blockResult.getTransactionList(),nodeQty);
            nodeQty = nodeQty + nodeList.size();
            if(nodeList.size()>0){
                nodePublisher.publish(nodeList);
                List<Staking> stakings = stakeGenService.buildStakeInfo(nodeList,stakeQty);
                stakeQty = stakeQty + stakings.size();

            }
            blockNumber=blockNumber.add(BigInteger.ONE);


        }


    }
}
