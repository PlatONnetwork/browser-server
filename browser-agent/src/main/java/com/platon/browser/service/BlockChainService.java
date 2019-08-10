package com.platon.browser.service;

import org.springframework.stereotype.Component;


/**
 * User: dongqile
 * Date: 2019/8/10
 * Time: 16:57
 */
@Component
public class BlockChainService {
    /**
     * 1.加载配置
     * 2.提供function()
     *  a.并行采集到的区块、交易、交易回执
     *  b.通过区块高度分析当前区块所在位置（结算周期轮数/共识周期轮数）
     *  c.对传入的区块和交易的数据进行初步处理
     *  d.分析区块交易的txinfo
     *  e.新开线程查询验证人列表
     *  f.确定区块以及交易的下一步处理调用引擎
     */
}