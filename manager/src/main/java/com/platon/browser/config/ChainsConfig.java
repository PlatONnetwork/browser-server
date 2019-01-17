package com.platon.browser.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ChainsConfig {

    public static class ChainInfo{
        public String chainName;
        public String chainId;
        private Web3j web3j;
        public ChainInfo(String chainName,String chainId,Web3j web3j){
            this.chainName = chainName;
            this.chainId = chainId;
            this.web3j = web3j;
        }
    }

    private static final String CHAIN_CONFIG_SPLITTER = ",";
    private static final String PART_SPLITTER = "-";

    @Value("${platon.chains}")
    private String configStr;

    private Map<String,ChainInfo> chainsConfig = new HashMap<>();

    @PostConstruct
    public void init(){
        if(StringUtils.isNotBlank(configStr)){
            String [] configs = configStr.split(CHAIN_CONFIG_SPLITTER);
            if(configs!=null){
                Arrays.asList(configs).forEach(config->{
                    String [] parts = config.split(PART_SPLITTER);
                    if(parts!=null&&parts.length==3){
                        chainsConfig.put(parts[1],new ChainInfo(parts[0],parts[1],Web3j.build(new HttpService(parts[2]))));
                    }
                });
            }
        }
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
        return chainsConfig.get(chainId).web3j;
    }

    public boolean isValid(String chainId){
        return getChainIds().contains(chainId);
    }
}
