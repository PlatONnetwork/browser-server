package com.platon.browser.client;

import com.platon.browser.job.Web3DetectJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private List<Web3j> validWeb3jList=new ArrayList <>();

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
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
        // 更新有效web3j实例列表
        updateValidWeb3jList();
    }

    public Web3j getWeb3j(){
        try{
            WEB3J_CONFIG_LOCK.readLock().lock();
            Random random = new Random();
            int index = random.nextInt(validWeb3jList.size());
            return validWeb3jList.get(index);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WEB3J_CONFIG_LOCK.readLock().unlock();
        }
        return null;
    }

    public void updateValidWeb3jList(){
        try {
            WEB3J_CONFIG_LOCK.writeLock().lock();
            validWeb3jList.clear();
            allWeb3jList.forEach((web3j)->{
                try {
                    EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
                    validWeb3jList.add(web3j);
                } catch (IOException e) {
                    e.getMessage();
                }
            });
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
    }

    /**
     * web3j实例活性探测及更新
     */
    @Scheduled(cron = "0/30 * * * * ?")
    protected void detect () {
        logger.debug("*** In the detect task *** ");
        try {
            updateValidWeb3jList();
        } catch (Exception e) {
            logger.error("detect exception:{}", e.getMessage());
            e.printStackTrace();
        }
        logger.debug("*** End the detect task *** ");
    }

    public static void main(String[] args) throws IOException {
        String url = "http://192.168.120.76:6789";
        Web3j web3j = Web3j.build(new HttpService(url));
        EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ONE),true).send();
        logger.info("{}",block);
    }
}
