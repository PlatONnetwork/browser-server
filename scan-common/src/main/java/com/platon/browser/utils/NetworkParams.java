package com.platon.browser.utils;

import com.platon.parameters.NetworkParameters;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
public class NetworkParams {

    @Value("${platon.chainId}")
    private long chainId;

    @Value("${platon.addressPrefix:lat}")
    private String hrp;

    @Value("${platon.valueUnit:LAT}")
    private String unit;

    @PostConstruct
    public void init() {
        NetworkParameters.init(chainId, hrp);
    }

}
