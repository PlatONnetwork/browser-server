package com.platon.browser.common.collection;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.service.epoch.EpochRetryService;
import com.platon.browser.common.service.epoch.EpochService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochMessageTest extends AgentTestBase {

    @Test
    public void test() {
        EpochMessage epochMessage = EpochMessage.newInstance();
        EpochRetryService epochRetryService = new EpochRetryService();
        EpochService epochService = new EpochService();
        epochMessage.updateWithEpochRetryService(epochRetryService).updateWithEpochService(epochService);
    }
}
