package com.platon.browser.config;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class ChainsConfig {

    private static final ReentrantReadWriteLock WEB3J_CONFIG_LOCK = new ReentrantReadWriteLock();

    public static class ChainInfo{
        public String chainName;
        public String chainId;
        private List<Web3j> allWeb3jList;
        private List<Web3j> validWeb3jList=new ArrayList <>();
        public ChainInfo(String chainName,String chainId,List<Web3j> web3jList){
            this.chainName = chainName;
            this.chainId = chainId;
            this.allWeb3jList = web3jList;
        }
    }

    private static final String CHAIN_CONFIG_SPLITTER = ",";
    private static final String PART_SPLITTER = "-";
    private static final String IP_BEGIN_SPLITTER = "[";
    private static final String IP_END_SPLITTER =  "]";
    private static final String SPACING_SPLITTER =" ";


    @Value("${platon.chain.config}")
    private String configStr;

    private Map<String,ChainInfo> chainsConfig = new HashMap<>();

    @PostConstruct
    public void init(){
        try {
            WEB3J_CONFIG_LOCK.writeLock().lock();
            if(StringUtils.isNotBlank(configStr)){
                String [] configs = configStr.split(SPACING_SPLITTER);
                if(configs!=null){
                    Arrays.asList(configs).forEach(config->{
                        String [] parts = config.split(PART_SPLITTER);
                        if(parts!=null&&parts.length==3){
                            List<String> ipStr = JSON.parseArray(parts[2],String.class);
                            List<Web3j> web3js = new ArrayList <>();
                            ipStr.forEach(ip->{
                                Web3j web3j = Web3j.build(new HttpService(ip));
                                web3js.add(web3j);
                            });
                            chainsConfig.put(parts[1],new ChainInfo(parts[0],parts[1],web3js));
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }

        updateValidWeb3jList();
    }

    public Collection<String> getChainIds(){
        List<String> chainIds = new ArrayList<>();
        chainsConfig.values().forEach(e->chainIds.add(e.chainId));
       return chainIds;
    }

    public ChainInfo getChainInfo(String chainId){
        return chainsConfig.get(chainId);
    }
    public Web3j getWeb3j(String chainId){
        try{
            WEB3J_CONFIG_LOCK.readLock().lock();
            List<Web3j> web3jList = chainsConfig.get(chainId).validWeb3jList;
            Random random = new Random();
            int index = random.nextInt(web3jList.size());
            return web3jList.get(index);
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
            chainsConfig.forEach((chainId,nodeInfo)->{
                nodeInfo.validWeb3jList.clear();
                nodeInfo.allWeb3jList.forEach(web3j -> {
                    try {
/*                    Thread t = new Thread(){
                        @Override
                        public void run () {
                                EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
                        }
                    };
                    t.start();
                    TimeUnit.SECONDS.sleep(10);
                        if (!t.isAlive()){*/
                            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
                            nodeInfo.validWeb3jList.add(web3j);
                      /* }*/
                    } catch (IOException e) {
                        e.getMessage();
                    }
                });
            });
        }finally {
            WEB3J_CONFIG_LOCK.writeLock().unlock();
        }
    }

    public boolean isValid(String chainId){
        return getChainIds().contains(chainId);
    }
}
