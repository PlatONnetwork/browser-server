package com.platon.browser.handler.proposal;

import com.platon.browser.TestBase;
import com.platon.browser.bean.TransactionBean;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.proposal.ProposalTextHandler;
import com.platon.browser.engine.handler.proposal.VotingProposalHandler;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
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
 * @Description: 提案投票业务类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class VotingProposalHandlerTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(VotingProposalHandlerTest.class);
    @Spy
    private VotingProposalHandler votingProposalHandler;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() throws  BeanCreateOrUpdateException {

    }

    /**
     *  文本提案测试方法
     */
    @Test
    public void testHandler () {
        EventContext eventContext = new EventContext();
        transactions.stream()
            .filter(tx->CustomTransaction.TxTypeEnum.VOTING_PROPOSAL.code.equals(tx.getTxType()))
            .forEach(tx->{
                try {
                    votingProposalHandler.handle(eventContext);
                    assertTrue(true);
                    logger.info("[VotingProposalHandlerTest] Test Pass");
                } catch (Exception e) {
                    assertTrue(false);
                    logger.info("[VotingProposalHandlerTest] Test Fail");
                }
            });
    }
}
