package com.platon.browser.bootstrap.bean;

/**
 * @description: 初始化结果
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-06 10:28:48
 **/

public class InitializationResult {
    // 已经采集过的区块号
    private Long collectedBlockNumber;

    public Long getCollectedBlockNumber () {
        return collectedBlockNumber;
    }

    public void setCollectedBlockNumber ( Long collectedBlockNumber ) {
        this.collectedBlockNumber = collectedBlockNumber;
    }
}
