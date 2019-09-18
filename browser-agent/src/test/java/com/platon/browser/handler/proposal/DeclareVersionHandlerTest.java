package com.platon.browser.handler.proposal;

import com.platon.browser.TestBase;
import com.platon.browser.bean.TransactionBean;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.proposal.DeclareVersionHandler;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 版本声明业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class DeclareVersionHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(DeclareVersionHandlerTest.class);
    @Spy
    private DeclareVersionHandler declareVersionHandler;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() throws  BeanCreateOrUpdateException {

    }

    /**
     *  版本声明测试方法
     */
    @Test
    public void testHandler () {
        EventContext eventContext = new EventContext();
        transactions.stream()
            .filter(tx->CustomTransaction.TxTypeEnum.DECLARE_VERSION.code.equals(tx.getTxType()))
            .forEach(tx->{
                try {
                    declareVersionHandler.handle(eventContext);
                    assertTrue(true);
                    logger.info("[DeclareVersionHandlerTest] Test Pass");
                } catch (Exception e) {
                    assertTrue(false);
                    logger.info("[DeclareVersionHandlerTest] Test Fail");
                }
            });
    }

}
