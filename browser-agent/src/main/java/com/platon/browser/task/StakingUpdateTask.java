package com.platon.browser.task;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.keybase.KeyBaseUser;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.task.bean.TaskStaking;
import com.platon.browser.task.cache.StakingTaskCache;
import com.platon.browser.util.GracefullyUtil;
import com.platon.browser.util.HttpUtil;
import com.platon.browser.util.KeyBaseAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    @Autowired
    private CacheHolder cacheHolder;
    @Autowired
    private StakingTaskCache taskCache;

    @Scheduled(cron = "0/3  * * * * ?")
    private void cron() throws InterruptedException {start();}

    public void start () throws InterruptedException {

        try {
            // 监控应用状态
            GracefullyUtil.monitor(this);
        } catch (GracefullyShutdownException e) {
            logger.warn("检测到SHUTDOWN钩子,放弃执行业务逻辑!");
            return;
        }

        Set<CustomStaking> stakingSet = getAllStaking();
        if (stakingSet.isEmpty()) return;
        String keyBaseUrl = chainConfig.getKeyBase();
        String keyBaseApi = chainConfig.getKeyBaseApi();
        stakingSet.forEach(staking -> {
            if(StringUtils.isNotBlank(staking.getExternalId())){
                TaskStaking cache = new TaskStaking();
                // 设置缓存主键
                cache.setNodeId(staking.getNodeId());
                cache.setStakingBlockNum(staking.getStakingBlockNum());
                // 如果外部ID不为空，则发送获取请求
                String url = keyBaseUrl.concat(keyBaseApi.concat(staking.getExternalId()));
                try {
                    KeyBaseUser keyBaseUser = HttpUtil.get(url,KeyBaseUser.class);
                    String userName = KeyBaseAnalysis.getKeyBaseUseName(keyBaseUser);
                    String icon = KeyBaseAnalysis.getKeyBaseIcon(keyBaseUser);
                    if(StringUtils.isNotBlank(icon)&&!icon.equals(staking.getStakingIcon())){
                        cache.setStakingIcon(icon);
                        taskCache.update(cache);
                    }
                    if(StringUtils.isNotBlank(userName)&&!userName.equals(staking.getExternalName())){
                        cache.setExternalName(userName);
                        taskCache.update(cache);
                    }
                } catch (HttpRequestException e) {
                    logger.error("更新质押(nodeId = {}, blockNumber = {})keybase信息出错:{}",staking.getNodeId(),staking.getStakingBlockNum(), e.getMessage());
                } catch (Exception e){
                    logger.error("解析KeyBase结构异常 {}",e.getMessage());
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
