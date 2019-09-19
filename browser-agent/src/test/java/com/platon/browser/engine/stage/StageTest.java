package com.platon.browser.engine.stage;

import com.platon.browser.TestBase;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @Auther: dongqile
 * @Date: 2019/9/5
 * @Description: 委托处理业务测试类
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class StageTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(StageTest.class);
    @Spy
    private AddressStage addressStage;
    @Spy
    private BlockChainStage blockChainStage;
    @Spy
    private NetworkStatStage networkStatStage;
    @Spy
    private ProposalStage proposalStage;
    @Spy
    private RestrictingStage restrictingStage;
    @Spy
    private StakingStage stakingStage;

    private List<Element> stages = new ArrayList<>();

    static class Element{
        Class<?> clazz;
        Object stage;
        public Element(Class<?> clazz, Object stage) {
            this.clazz = clazz;
            this.stage = stage;
        }
    }
    /**
     * 测试开始前，设置相关行为属性
     * @throws IOException
     * @throws BeanCreateOrUpdateException
     */
    @Before
    public void setup() {
        stages.add(new Element(AddressStage.class,addressStage));
        stages.add(new Element(BlockChainStage.class,blockChainStage));
        stages.add(new Element(NetworkStatStage.class,networkStatStage));
        stages.add(new Element(ProposalStage.class,proposalStage));
        stages.add(new Element(RestrictingStage.class,restrictingStage));
        stages.add(new Element(StakingStage.class,stakingStage));
    }
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException {
        for (Element ele:stages){
            Method[] methods = ele.clazz.getDeclaredMethods();
            for(Method method:methods){
                if(Modifier.isStatic(method.getModifiers())) continue;
                Class<?>[] types = method.getParameterTypes();
                if(types.length!=0){
                    Object[] args = new Object[types.length];
                    for (int i=0;i<types.length;i++) args[i]=mock(types[i]);
                    method.invoke(ele.stage,args);
                }else{
                    method.invoke(ele.stage);
                }
            }
        }
    }
}
