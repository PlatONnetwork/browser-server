package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.bean.keystore.Completion;
import com.platon.browser.engine.bean.keystore.Components;
import com.platon.browser.engine.bean.keystore.KeystoreUser;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.util.MarkDownParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

/**
 * User: dongqile
 * Date: 2019/8/29
 * Time: 15:53
 */
@Component
public class StakingInfoUpdateTask {

    private static Logger logger = LoggerFactory.getLogger(StakingInfoUpdateTask.class);

    @Autowired
    private BlockChain blockChain;

    private static final String fingerprintpPer = "_/api/1.0/user/autocomplete.json?q=";


    @Scheduled(cron = "0/3  * * * * ?")
    protected void start () {
        String keyStoreUrl = blockChain.getChainConfig().getKeyStore();
        try {
            Set <CustomStaking> customStakingSet = NODE_CACHE.getAllStaking();
            if(customStakingSet.size() == 0)return;
            customStakingSet.forEach(customStaking -> {
                if(StringUtils.isNotBlank(customStaking.getExternalId())){
                    String queryUrl = keyStoreUrl.concat(fingerprintpPer.concat(customStaking.getExternalId()));
                    try {
                        String queryResult = MarkDownParserUtil.httpGet(queryUrl);
                        KeystoreUser keystoreUser = JSON.parseObject(queryResult,KeystoreUser.class);
                        List<Completion> completions = keystoreUser.getCompletions();
                        if(completions==null||completions.size()==0) return;
                        // 取最新一条
                        Completion completion = completions.get(0);
                        // 取缩略图
                        String icon = completion.getThumbnail();
                        customStaking.setStakingIcon(icon);

                        Components components = completion.getComponents();
                        String username = components.getUsername().getVal();
                        customStaking.setExternalName(username);

                    } catch (IOException e) {
                        logger.error("[StakingInfoUpdateTask] IOException {}",e.getMessage());
                    } catch (Exception e){
                        logger.error("[StakingInfoUpdateTask] Exception {}",e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
    }

    private String getUserName(String seachInfo)throws  Exception{
        JSONObject jsonObject = JSONObject.parseObject(seachInfo);
        JSONArray completionsArray = jsonObject.getJSONArray("completions");
        JSONObject fristJson = completionsArray.getJSONObject(0);
        JSONObject componentsObject = fristJson.getJSONObject("components");
        JSONObject userNameJson = componentsObject.getJSONObject("username");
        return userNameJson.getString("val");
    }

    private String getIcon(String seachInfo)throws Exception{
        JSONObject jsonObject = JSONObject.parseObject(seachInfo);
        JSONArray completionsArray = jsonObject.getJSONArray("completions");
        JSONObject fristJson = completionsArray.getJSONObject(0);
        return  fristJson.getString("thumbnail");
    }
}
