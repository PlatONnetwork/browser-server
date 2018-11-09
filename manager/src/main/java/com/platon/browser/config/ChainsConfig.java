package com.platon.browser.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class ChainsConfig {

    private static final String CHAIN_CONFIG_SPLITER = ",";
    private static final String NAME_ID_SPLITER = ":";

    private String configStr;

    private Map<String,String> chainsConfig = new HashMap<>();

    public ChainsConfig(String configStr){
        this.configStr = configStr;
        if(StringUtils.isNotBlank(configStr)){
            String [] confsArr = configStr.split(CHAIN_CONFIG_SPLITER);
            if(confsArr!=null){
                Arrays.asList(confsArr).forEach(config->{
                    String [] nameId = config.split(NAME_ID_SPLITER);
                    if(nameId!=null&&nameId.length==2){
                        chainsConfig.put(nameId[0],nameId[1]);
                    }
                });
            }
        }
    }

    public Collection<String> getChainIds(){
       return chainsConfig.values();
    }

    public boolean isValid(String chainId){
        return chainsConfig.values().contains(chainId);
    }
}
