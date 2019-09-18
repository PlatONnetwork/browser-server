package com.platon.browser.handler.proposal;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.proposal.ProposalCancelHandler;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 提案取消业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalCancelHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(ProposalCancelHandlerTest.class);
    @Spy
    private ProposalCancelHandler proposalCancelHandlerl;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() throws  BeanCreateOrUpdateException {

    }

    /**
     *  取消提案测试方法
     */
    @Test
    public void testHandler () {
        EventContext eventContext = new EventContext();
        transactions.stream()
            .filter(tx->CustomTransaction.TxTypeEnum.CANCEL_PROPOSAL.code.equals(tx.getTxType()))
            .forEach(tx->{
                try {
                    proposalCancelHandlerl.handle(eventContext);
                    assertTrue(true);
                    logger.info("[ProposalCancelHandlerTest] Test Pass");
                } catch (Exception e) {
                    assertTrue(false);
                    logger.info("[ProposalCancelHandlerTest] Test Fail");
                }
            });
    }

}
