package com.platon.browser.old.util;

import com.platon.browser.old.exception.GracefullyShutdownException;
import com.platon.browser.old.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GracefullyUtil {
    private static final Logger logger = LoggerFactory.getLogger(GracefullyUtil.class);
    private GracefullyUtil(){}
    private static AppStatusEnum status=AppStatusEnum.RUNNING;
    public static void monitor(BaseTask task) throws GracefullyShutdownException, InterruptedException {
        switch (getStatus()){
            case PAUSE:
                task.setRunning(false);
                logger.warn("线程已暂停");
                while (getStatus() == AppStatusEnum.PAUSE){
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

    public static AppStatusEnum getStatus() {
        return status;
    }

    public static void setStatus(AppStatusEnum status) {
        GracefullyUtil.status = status;
    }
}
