package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.bean.keybase.Completion;
import com.platon.browser.engine.bean.keybase.Components;
import com.platon.browser.engine.bean.keybase.KeyBaseUser;
import com.platon.browser.util.MarkDownParserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 质押信息更新任务
 */
@Component
public class StakingUpdateTask {

    private static Logger logger = LoggerFactory.getLogger(StakingUpdateTask.class);

    @Autowired
    private BlockChain blockChain;

    private static final String fingerprintpPer = "_/api/1.0/user/autocomplete.json?q=";


    @Scheduled(cron = "0/3  * * * * ?")
    protected void start () {
        String keyStoreUrl = blockChain.getChainConfig().getKeyBase();
        try {
            Set <CustomStaking> customStakingSet = NODE_CACHE.getAllStaking();
            if (customStakingSet.size() == 0) return;
            customStakingSet.forEach(customStaking -> {
                if(StringUtils.isBlank(customStaking.getExternalName()) || StringUtils.isBlank(customStaking.getStakingIcon())){
                    if (StringUtils.isNotBlank(customStaking.getExternalId())) {
                        String queryUrl = keyStoreUrl.concat(fingerprintpPer.concat(customStaking.getExternalId()));
                        try {
                            String queryResult = MarkDownParserUtil.httpGet(queryUrl);
                            if(null==queryResult)return;
                            KeyBaseUser keyBaseUser = JSON.parseObject(queryResult, KeyBaseUser.class);
                            if(keyBaseUser==null) return;
                            List <Completion> completions = keyBaseUser.getCompletions();
                            if (completions == null || completions.size() == 0) return;
                            // 取最新一条
                            Completion completion = completions.get(0);
                            // 取缩略图
                            String icon = completion.getThumbnail();
                            customStaking.setStakingIcon(icon);

                            Components components = completion.getComponents();
                            String username = components.getUsername().getVal();
                            customStaking.setExternalName(username);
                            // 把改动后的内容暂存至待更新列表
                            STAGE_DATA.getStakingStage().updateStaking(customStaking);
                        } catch (IOException e) {
                            logger.error("更新质押(nodeId = {}, blockNumber = {})keybase信息出错:{}",customStaking.getNodeId(),customStaking.getStakingBlockNum(), e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error("[StakingUpdateTask] Exception {}", e.getMessage());
        }
    }
}
