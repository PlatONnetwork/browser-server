package com.platon.browser.task;

import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.common.utils.BakDataDeleteUtil;
import com.platon.browser.dao.entity.NOptBakExample;
import com.platon.browser.dao.entity.TxBakExample;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.TxBakMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: 备份数据删除任务
 * @author: chendongming@juzix.net
 * @create: 2019-11-09 17:58:27
 **/
@Component
@Slf4j
public class BakDataDeleteTask {

    @Autowired
    private NOptBakMapper nOptBakMapper;
    @Autowired
    private TxBakMapper txBakMapper;

    @Scheduled(cron = "0/5  * * * * ?")
    private void cron () {
        // 只有程序正常运行才执行任务
        if(!AppStatusUtil.isRunning()) return;
        start();
    }

    protected void start () {

        try {
            // 删除小于最高ID的交易备份
            TxBakExample txBakExample = new TxBakExample();
            txBakExample.createCriteria().andIdLessThanOrEqualTo(BakDataDeleteUtil.getTxBakMaxId());
            int txCount = txBakMapper.deleteByExample(txBakExample);
            log.debug("清除交易备份记录({})条",txCount);
            // 删除小于最高ID的操作记录备份
            NOptBakExample nOptBakExample = new NOptBakExample();
            nOptBakExample.createCriteria().andIdLessThanOrEqualTo(BakDataDeleteUtil.getNOptBakMaxId());
            int optCount = nOptBakMapper.deleteByExample(nOptBakExample);
            log.debug("清除操作备份记录({})条",optCount);
        } catch (Exception e) {
            log.error("",e);
        }
    }
}
