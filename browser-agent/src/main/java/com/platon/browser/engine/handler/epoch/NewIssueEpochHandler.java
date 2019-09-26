package com.platon.browser.engine.handler.epoch;

import com.platon.browser.dto.CustomBlock;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.exception.IssueEpochChangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 增发周期变更事件处理类
 */
@Component
public class NewIssueEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewIssueEpochHandler.class);
    @Autowired
    private BlockChain bc;
    @Override
    public void handle ( EventContext context ) {
        updateReward(); // 更新区块奖励和质押奖励
    }

    /**
     * 在增发周期切换时更新区块奖励和质押奖励
     * @throws IssueEpochChangeException
     */
    private void updateReward() {
    	logger.debug("NewIssueEpochHandler updateReward");
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        bc.updateReward(blockNumber);
    }
}
