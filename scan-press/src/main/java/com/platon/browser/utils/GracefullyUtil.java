package com.platon.browser.utils;

import com.platon.browser.exception.GracefullyShutdownException;
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
import java.util.concurrent.TimeUnit;

@Component
public class GracefullyUtil {

    @Scheduled(cron = "0/5  * * * * ?")
    private void cron() {
        updateStatus();
    }

    private static final Logger logger = LoggerFactory.getLogger(GracefullyUtil.class);

    public static AppStatusEnum status = AppStatusEnum.RUNNING;

    public static void monitor() throws GracefullyShutdownException, InterruptedException {
        switch (status) {
            case PAUSE:
                logger.warn("线程已暂停");
                while (status == AppStatusEnum.PAUSE) {
                    TimeUnit.SECONDS.sleep(2);
                }
                logger.warn("线程恢复执行...");
                break;
            case SHUTDOWN:
                logger.warn("线程停止中...");
                throw new GracefullyShutdownException("线程已停止!");
            default:
        }
    }

    public static void updateStatus() {
        String path = CommonUtil.isWin() ? "/browser-press/status.hook" : "status.hook";
        File statusFile = FileUtils.getFile(System.getProperty("user.dir"), path);
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(statusFile)) {
            properties.load(in);
            String status = properties.getProperty("status");
            status = status.trim();
            AppStatusEnum statusEnum = AppStatusEnum.valueOf(status.toUpperCase());
            GracefullyUtil.status = statusEnum;
            logger.info("Update app status:{}", statusEnum.name());
        } catch (Exception e) {
            logger.error("无效状态,状态可取值:{}", Arrays.asList(AppStatusEnum.values()));
        }
    }

}
