package com.platon.browser.task;

import com.platon.browser.engine.BlockChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/8/20
 * Time: 10:08
 */
@Component
public class CirculationAndTurnoverSyn {

    @Autowired
    private BlockChain blockChain;

    //@PostConstruct
    private void ini(){
        //从配置文件中获取到每个增发周期对应的基金会补充金额
        Map <String,String> foundationSubsidiesMap = new HashMap <>();
        //判断当前为哪一个增发周期，获取当前增发周期基金会补充的金额
        String foundationValue = foundationSubsidiesMap.get(blockChain.getAddIssueEpoch().toString());
        //获取初始发行金额
        BigDecimal iniValue = blockChain.getChainConfig().getInitIssueAmount();
        //获取增发比例
       // blockChain.getChainConfig().get
    }

    @Scheduled(cron = "0/1 * * * * ?")
    protected void syn () {
    }

}
