package com.platon.browser.engine.stage;

import com.platon.browser.TestBase;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 委托处理业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakingStageTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(StakingStageTest.class);
    @Spy
    private StakingStage stakingStage;

    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {


    }
    @Test
    public void test(){
        stakingStage.clear();
        stakingStage.exportDelegation();
        stakingStage.exportNode();
        stakingStage.exportNodeOpt();
        stakingStage.exportSlash();
        stakingStage.exportUnDelegation();

        stakingStage.insertDelegation(mock(CustomDelegation.class));
        stakingStage.insertNode(mock(CustomNode.class));
        stakingStage.insertNodeOpt(mock(CustomNodeOpt.class));
    }

}
