package com.platon.browser.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.util.MarkDownParserUtil;
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
            customStakingSet.forEach(customStaking -> {
                String seachInfo = keyStoreUrl.concat(fingerprintpPer.concat(customStaking.getExternalId()));
                String keyStoreInfo = null;
                try {
                    keyStoreInfo = MarkDownParserUtil.httpGet(seachInfo);
                    String userName = getUserName(keyStoreInfo);
                    String income = getIncome(keyStoreInfo);
                    customStaking.setExpectedIncome(income);
                    customStaking.setExternalName(userName);
                    //TODO:数据回填username
                } catch (IOException e) {
                    logger.error("");
                    e.printStackTrace();
                } catch (Exception e){
                    logger.error("");
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
    }

/*    public static void main ( String[] args )throws Exception {
        String a = "{\"status\":{\"code\":0,\"name\":\"OK\"},\"completions\":[{\"total_score\":0.0006097560975609757,\"components\":{\"username\":{\"val\":\"bonybee\",\"score\":0},\"key_fingerprint\":{\"val\":\"df6fbe3ced59e75bced77cfa5fd68b690010632b\",\"algo\":1,\"nbits\":4096,\"score\":0.0006097560975609757},\"full_name\":{\"val\":\"eeyun\",\"score\":0}},\"uid\":\"feb5d34d4b613be05286c85a33773619\",\"thumbnail\":\"https://s3.amazonaws.com/keybase_processed_uploads/e6ca9b440bc2bf1ce5f20c0f52689405_200_200.jpg\",\"is_followee\":false}]}";
        JSONObject jsonobject = JSONObject.parseObject(a);
        JSONArray b = jsonobject.getJSONArray("completions");
        JSONObject c = b.getJSONObject(0);
        JSONObject d = c.getJSONObject("components");
        JSONObject e= d.getJSONObject("username");

        //
        String cc  = c.getString("thumbnail");
        System.out.println(e.getString("val"));
        System.out.println(cc);
        StakingInfoUpdateTask stakingInfoUpdateTask = new StakingInfoUpdateTask();
        String userName = stakingInfoUpdateTask.getUserName(a);
        System.out.println(userName);
        String income = stakingInfoUpdateTask.getIncome(a);
        System.out.println(income);
    }*/

    private String getUserName(String seachInfo)throws  Exception{
        JSONObject jsonObject = JSONObject.parseObject(seachInfo);
        JSONArray completionsArray = jsonObject.getJSONArray("completions");
        JSONObject fristJson = completionsArray.getJSONObject(0);
        JSONObject componentsObject = fristJson.getJSONObject("components");
        JSONObject userNameJson = componentsObject.getJSONObject("username");
        return userNameJson.getString("val");
    }

    private String getIncome(String seachInfo)throws Exception{
        JSONObject jsonObject = JSONObject.parseObject(seachInfo);
        JSONArray completionsArray = jsonObject.getJSONArray("completions");
        JSONObject fristJson = completionsArray.getJSONObject(0);
        return  fristJson.getString("thumbnail");
    }
}