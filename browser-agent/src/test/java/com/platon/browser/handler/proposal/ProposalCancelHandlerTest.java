package com.platon.browser.handler.proposal;

import com.platon.browser.TestBase;
import com.platon.browser.bean.TransactionBean;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.proposal.ProposalCancelHandler;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 提案取消业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalCancelHandlerTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(ProposalCancelHandlerTest.class);

    @Autowired
    private ProposalCancelHandler proposalCancelHandlerl;

    @Mock
    private TransactionService transactionService;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() throws  BeanCreateOrUpdateException {
        when(transactionService.analyze(anyList())).thenCallRealMethod();
        when(transactionService.updateTransaction(any(CustomTransaction.class))).thenCallRealMethod();
        when(transactionService.getReceipt(any(TransactionBean.class))).thenAnswer((Answer <Optional <TransactionReceipt>>) invocation->{
            TransactionBean tx = invocation.getArgument(0);
            TransactionReceipt receipt = new TransactionReceipt();
            BeanUtils.copyProperties(tx,receipt);
            receipt.setBlockNumber(tx.getBlockNumber().toString());
            receipt.setTransactionHash(tx.getHash());
            receipt.setTransactionIndex(tx.getTransactionIndex().toString());
            Optional<TransactionReceipt> optional = Optional.ofNullable(receipt);
            return optional;
        });
    }

    /**
     *  取消提案测试方法
     */
    @Test
    public void testHandler () {
        EventContext eventContext = new EventContext();
        List <CustomBlock> result = transactionService.analyze(blocks);
        transactions.forEach(transactionBean -> {
            if (transactionBean.getTxType().equals(CustomTransaction.TxTypeEnum.CANCEL_PROPOSAL.code)) {
                try {
                    proposalCancelHandlerl.handle(eventContext);
                    assertTrue(true);
                    logger.info("[ProposalCancelHandlerTest] Test Pass");
                } catch (Exception e) {
                    assertTrue(false);
                    logger.info("[ProposalCancelHandlerTest] Test Fail");
                }
            }
        });
    }

}