package com.platon.browser.task;

import com.platon.browser.util.AppStatusEnum;
import com.platon.browser.util.GracefullyUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Component
public class TaskManager {
    private static Logger logger = LoggerFactory.getLogger(TaskManager.class);
    @Autowired
    private AddressUpdateTask addressUpdateTask;
    @Autowired
    private NetworkStatUpdateTask networkStatUpdateTask;
    @Autowired
    private ProposalUpdateTask proposalUpdateTask;
    @Autowired
    private StakingUpdateTask stakingUpdateTask;

    private final static Set<BaseTask> tasks = new HashSet<>();
    @PostConstruct
    private void init(){
        tasks.add(addressUpdateTask);
        tasks.add(networkStatUpdateTask);
        tasks.add(proposalUpdateTask);
        tasks.add(stakingUpdateTask);
    }

    /**
     * 是否有任务在运行中
     * @return
     */
    public boolean isRunning(){
        for (BaseTask task:tasks) if(task.isRunning()) return true;
        return false;
    }

    /**
     * 定时检测状态钩子文件
     */
    @Scheduled(cron = "0/10  * * * * ?")
    protected void updateAppStatus () {
        File statusFile = FileUtils.getFile(System.getProperty("user.dir"), "status.hook");
        Properties properties = new Properties();
        try(InputStream in = new FileInputStream(statusFile)) {
            properties.load(in);
            String status=properties.getProperty("status");
            status=status.trim();
            AppStatusEnum statusEnum = AppStatusEnum.valueOf(status.toUpperCase());
            GracefullyUtil.status=statusEnum;
            logger.info("Update app status:{}",statusEnum.name());
        } catch (Exception e) {
            logger.error("无效状态,有效值:[{}]", Arrays.asList(AppStatusEnum.values()));
        }
    }
}
