package com.platon.browser.task;

import com.platon.browser.util.AppStatusEnum;
import com.platon.browser.util.GracefullyUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 应用状态监控任务
 */
@Component
public class AppMonitorTask {
    private static Logger logger = LoggerFactory.getLogger(AppMonitorTask.class);

    @Scheduled(cron = "0/10  * * * * ?")
    private void cron(){start();}

    protected void start () {
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
            logger.error("无效状态,状态可取值:{}", Arrays.asList(AppStatusEnum.values()));
        }
    }
}
