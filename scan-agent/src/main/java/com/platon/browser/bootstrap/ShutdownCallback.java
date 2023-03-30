package com.platon.browser.bootstrap;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 关闭队列回调
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-07 15:07:57
 **/
@Slf4j
public class ShutdownCallback implements Callback {
    private boolean isDone;
    private long endBlockNum;
    public void call(long handledBlockNum){
        log.debug("区块({})同步完成!",handledBlockNum);
        if(handledBlockNum==endBlockNum) {
            isDone=true;
            log.debug("所有区块同步完成!");
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getEndBlockNum() {
        return endBlockNum;
    }

    public void setEndBlockNum(long endBlockNum) {
        this.endBlockNum = endBlockNum;
    }
}
