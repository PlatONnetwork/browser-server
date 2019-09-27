package com.platon.browser.util;

import com.platon.browser.exception.GracefullyShutdownException;
import com.platon.browser.task.BlockSyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GracefullyUtil {
    private static final Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);
    public static AppStatusEnum status=AppStatusEnum.RUNNING;
    public static void monitor() throws GracefullyShutdownException, InterruptedException {
        switch (status){
            case PAUSE:
                logger.warn("线程已暂停");
                while (status== AppStatusEnum.PAUSE){
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
}
