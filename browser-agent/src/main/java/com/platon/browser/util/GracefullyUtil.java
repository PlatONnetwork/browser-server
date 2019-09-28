package com.platon.browser.util;

import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GracefullyUtil {
    private static final Logger logger = LoggerFactory.getLogger(GracefullyUtil.class);
    public static AppStatusEnum status=AppStatusEnum.RUNNING;
    public static void monitor(BaseTask task) throws GracefullyShutdownException, InterruptedException {
        switch (status){
            case PAUSE:
                task.setRunning(false);
                logger.warn("线程已暂停");
                while (status== AppStatusEnum.PAUSE){
                    TimeUnit.SECONDS.sleep(2);
                }
                task.setRunning(true);
                logger.warn("线程恢复执行...");
                break;
            case SHUTDOWN:
                task.setRunning(false);
                logger.warn("线程停止中...");
                throw new GracefullyShutdownException("线程已停止!");
            default:
        }
    }
}
