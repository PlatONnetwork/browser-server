package com.platon.browser.utils;


import com.platon.browser.enums.AppStatus;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description: 程序状态管理工具
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-09 18:15:21
 **/
public class AppStatusUtil {

    private static final ReentrantReadWriteLock APP_STATUS_LOCK = new ReentrantReadWriteLock();

    private AppStatusUtil() {
    }

    private static AppStatus appStatus;

    /**
     * 设置应用状态
     *
     * @param status
     */
    public static void setStatus(AppStatus status) {
        APP_STATUS_LOCK.writeLock().lock();
        try {
            appStatus = status;
        } finally {
            APP_STATUS_LOCK.writeLock().unlock();
        }
    }

    /**
     * 获取应用状态
     *
     * @return
     */
    public static AppStatus getStatus() {
        APP_STATUS_LOCK.readLock().lock();
        try {
            return appStatus;
        } finally {
            APP_STATUS_LOCK.readLock().unlock();
        }
    }

    /**
     * 应用是否在启动过程中
     *
     * @return
     */
    public static boolean isBooting() {
        APP_STATUS_LOCK.readLock().lock();
        try {
            return appStatus == AppStatus.BOOTING;
        } finally {
            APP_STATUS_LOCK.readLock().unlock();
        }
    }

    /**
     * 应用是否在正常运行中
     *
     * @return
     */
    public static boolean isRunning() {
        APP_STATUS_LOCK.readLock().lock();
        try {
            return appStatus == AppStatus.RUNNING;
        } finally {
            APP_STATUS_LOCK.readLock().unlock();
        }
    }

    /**
     * 应用是否已停止
     *
     * @return
     */
    public static boolean isStopped() {
        APP_STATUS_LOCK.readLock().lock();
        try {
            return appStatus == AppStatus.STOPPED;
        } finally {
            APP_STATUS_LOCK.readLock().unlock();
        }
    }


    public static boolean isRunningJUnitTest(){
        return JUnitTestUtils.IsRunningTest();
    }
}
