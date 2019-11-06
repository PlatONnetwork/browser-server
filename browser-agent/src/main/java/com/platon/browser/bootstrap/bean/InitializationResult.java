package com.platon.browser.bootstrap.bean;

import lombok.Data;

/**
 * @description: 初始化结果
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:28:48
 **/
@Data
public class InitializationResult {
    // 已经采集过的区块号
    private Long collectedBlockNumber;
}
