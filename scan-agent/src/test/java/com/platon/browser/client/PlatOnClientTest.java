package com.platon.browser.client;

import com.platon.browser.AgentApplication;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.methods.response.PlatonGetCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@SpringBootTest(classes = { AgentApplication.class })
@ActiveProfiles("platon")
public class PlatOnClientTest {

    @Resource
    PlatOnClient platOnClient;

    @Test
    public void getCode() throws IOException {
        StopWatch watch = new StopWatch("获取合约code");
        watch.start("getCode111");
        PlatonGetCode platonGetCode = platOnClient.getWeb3jWrapper().getWeb3j().platonGetCode("lat1fus638skn3mark8gdjerqkd0euyrg4jysxy5vp", DefaultBlockParameter.valueOf(BigInteger.valueOf(40306033))).send();
        System.out.println("PlatonGetCode111.length:" + platonGetCode.getCode().length());
        watch.stop();
        watch.start("getCode222");
        platonGetCode = platOnClient.getWeb3jWrapper().getWeb3j().platonGetCode("lat1rm9z0s3c6pvsa8jj7w8t38uf6z63nwdenxd4cf", DefaultBlockParameter.valueOf(BigInteger.valueOf(40306033))).send();
        System.out.println("PlatonGetCode222.length:" + platonGetCode.getCode().length());
        watch.stop();
        System.out.println("耗时统计:" + watch.prettyPrint());

    }
}
