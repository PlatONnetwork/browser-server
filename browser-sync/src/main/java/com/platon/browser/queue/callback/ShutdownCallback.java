package com.platon.browser.queue.callback;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 关闭队列回调
 * @author: chendongming@juzix.net
 * @create: 2019-11-07 15:07:57
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class ShutdownCallback implements Callback {
    private boolean isDone;
    private long endBlockNum;
    public void call(long handledBlockNum){
        log.info("区块({})同步完成!",handledBlockNum);
        if(handledBlockNum==endBlockNum) {
            isDone=true;
            log.info("所有区块同步完成!");
        }
    }
}
