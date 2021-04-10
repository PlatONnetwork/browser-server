package com.platon.browser.utils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description: 备份数据删除工具
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-09 18:15:21
 **/
public class BakDataDeleteUtil {
    private BakDataDeleteUtil(){}
    private static final ReentrantReadWriteLock N_OPT_BAK_LOCK = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock TX_BAK_LOK = new ReentrantReadWriteLock();
    private static volatile long nOptBakMaxId = 0L;
    private static volatile long txBakMaxId = 0L;

    public static void updateNOptBakMaxId(Long maxId){
        N_OPT_BAK_LOCK.writeLock().lock();
        try {
            nOptBakMaxId = maxId;
        }finally {
            N_OPT_BAK_LOCK.writeLock().unlock();
        }
    }

    public static Long getNOptBakMaxId(){
        N_OPT_BAK_LOCK.readLock().lock();
        try {
            return nOptBakMaxId;
        }finally {
            N_OPT_BAK_LOCK.readLock().unlock();
        }
    }

    public static void updateTxBakMaxId(Long maxId){
        TX_BAK_LOK.writeLock().lock();
        try {
            txBakMaxId = maxId;
        }finally {
            TX_BAK_LOK.writeLock().unlock();
        }
    }

    public static Long getTxBakMaxId(){
        TX_BAK_LOK.readLock().lock();
        try {
            return txBakMaxId;
        }finally {
            TX_BAK_LOK.readLock().unlock();
        }
    }
}
