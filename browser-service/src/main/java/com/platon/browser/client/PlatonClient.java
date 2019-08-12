package com.platon.browser.client;

import com.platon.browser.job.Web3DetectJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 14:42
 */
@Component
public class PlatonClient {
    private static Logger logger = LoggerFactory.getLogger(Web3DetectJob.class);
    private static final ReentrantReadWriteLock WEB3J_CONFIG_LOCK = new ReentrantReadWriteLock();

    private List<Web3j> allWeb3jList=new ArrayList<>();
    private Web3j currentValidWeb3j;

    @Value("${platon.web3j.addresses}")
    private List<String> web3jAddresses;

    @PostConstruct
    public void init(){
        // 初始化所有web3j实例
        try {
            WEB3J_CONFIG_LOCK.writeLock().lock();
            web3jAddresses.forEach(address->{
                Web3j web3j = Web3j.build(new HttpService(address));
                allWeb3jList.add(web3j);
                if(currentValidWeb3j==null) currentValidWeb3j=web3j;
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
        // 更新有效web3j实例列表
        updateCurrentValidWeb3j();
    }

    public Web3j getWeb3j(){
        WEB3J_CONFIG_LOCK.readLock().lock();
        try{
            return currentValidWeb3j;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WEB3J_CONFIG_LOCK.readLock().unlock();
        }
        return null;
    }

    public void updateCurrentValidWeb3j(){
        WEB3J_CONFIG_LOCK.writeLock().lock();
        Web3j originWeb3j = currentValidWeb3j;
        try {
            // 检查currentValidWeb3j连通性
            try {
                if(currentValidWeb3j==null) throw new RuntimeException("currentValidWeb3j需要初始化！");
                currentValidWeb3j.platonBlockNumber().send();
            } catch (Exception e1) {
                logger.info("当前Web3j实例({})无效，重新选举Web3j实例！",currentValidWeb3j);
                // 连不通，则需要更新
                for(Web3j web3j:allWeb3jList){
                    try {
                        web3j.platonBlockNumber().send();
                        currentValidWeb3j=web3j;
                    } catch (IOException e2) {
                        logger.info("候选Web3j实例({})无效！",web3j);
                    }
                }
                if(originWeb3j==currentValidWeb3j){
                    logger.info("当前所有候选Web3j实例均无效！");
                }
            }
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
    }

    /**
     * web3j实例保活
     */
    @Scheduled(cron = "0/30 * * * * ?")
    protected void keepAlive () {
        logger.debug("*** In the detect task *** ");
        try {
            updateCurrentValidWeb3j();
        } catch (Exception e) {
            logger.error("detect exception:{}", e.getMessage());
            e.printStackTrace();
        }
        logger.debug("*** End the detect task *** ");
    }
}
