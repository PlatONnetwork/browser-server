package com.platon.browser.task;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.bean.keybase.Completion;
import com.platon.browser.engine.bean.keybase.Components;
import com.platon.browser.engine.bean.keybase.KeyBaseUser;
import com.platon.browser.engine.bean.keybase.ValueScore;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.task.bean.TaskStaking;
import com.platon.browser.task.cache.StakingTaskCache;
import com.platon.browser.util.GracefullyUtil;
import com.platon.browser.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 质押信息更新任务
 */
@Component
public class StakingUpdateTask extends BaseTask{
    private static Logger logger = LoggerFactory.getLogger(StakingUpdateTask.class);
    @Autowired
    private BlockChainConfig chainConfig;
    private static final String URI_PATH = "_/api/1.0/user/autocomplete.json?q=";
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private StakingTaskCache taskCache;

    @Scheduled(cron = "0/3  * * * * ?")
    private void cron(){start();}

    public void start () {

        try {
            // 监控应用状态
            GracefullyUtil.monitor(this);
        } catch (GracefullyShutdownException e) {
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<CustomStaking> stakingSet = getAllStaking();
        if (stakingSet.isEmpty()) return;
        String keyStoreUrl = chainConfig.getKeyBase();
        stakingSet.forEach(staking -> {
            if(StringUtils.isNotBlank(staking.getExternalId())){
                TaskStaking cache = new TaskStaking();
                // 设置缓存主键
                cache.setNodeId(staking.getNodeId());
                cache.setStakingBlockNum(staking.getStakingBlockNum());
                // 如果外部ID不为空，则发送获取请求
                String url = keyStoreUrl.concat(URI_PATH.concat(staking.getExternalId()));
                try {
                    KeyBaseUser keyBaseUser = HttpUtil.get(url,KeyBaseUser.class);
                    List <Completion> completions = keyBaseUser.getCompletions();
                    if (completions == null || completions.isEmpty()) return;
                    // 取最新一条
                    Completion completion = completions.get(0);
                    // 取缩略图
                    String icon = completion.getThumbnail();
                    if(StringUtils.isNotBlank(icon)&&!icon.equals(staking.getStakingIcon())){
                        cache.setStakingIcon(icon);
                        taskCache.update(cache);
                    }
                    Components components = completion.getComponents();
                    ValueScore vs = components.getUsername();
                    if(vs==null) return;
                    String username = vs.getVal();
                    if(StringUtils.isNotBlank(username)&&!username.equals(staking.getExternalName())){
                        cache.setExternalName(username);
                        taskCache.update(cache);
                    }
                } catch (HttpRequestException e) {
                    logger.error("更新质押(nodeId = {}, blockNumber = {})keybase信息出错:{}",staking.getNodeId(),staking.getStakingBlockNum(), e.getMessage());
                }
            }
        });
        // 清除已合并的任务缓存
        taskCache.sweep();
        // 节点缓存整理
        NodeCache nodeCache = cacheHolder.getNodeCache();
        nodeCache.sweep();
    }

    /**
     * 取所有质押
     * @return
     */
    public Set<CustomStaking> getAllStaking() {
        return cacheHolder.getNodeCache().getAllStaking();
    }
}
