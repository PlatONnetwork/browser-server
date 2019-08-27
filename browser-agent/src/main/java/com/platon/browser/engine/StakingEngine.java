package com.platon.browser.engine;

import com.platon.browser.dao.mapper.CustomDelegationMapper;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.CustomUnDelegationMapper;
import com.platon.browser.dto.*;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.*;
import com.platon.browser.engine.handler.delegation.DelegateHandler;
import com.platon.browser.engine.handler.delegation.UnDelegateHandler;
import com.platon.browser.engine.handler.epoch.NewConsensusEpochHandler;
import com.platon.browser.engine.handler.epoch.NewElectionEpochHandler;
import com.platon.browser.engine.handler.epoch.NewSettleEpochHandler;
import com.platon.browser.engine.handler.slash.ReportValidatorHandler;
import com.platon.browser.engine.handler.staking.CreateValidatorHandler;
import com.platon.browser.engine.handler.staking.EditValidatorHandler;
import com.platon.browser.engine.handler.staking.ExitValidatorHandler;
import com.platon.browser.engine.handler.staking.IncreaseStakingHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class StakingEngine {
    private static Logger logger = LoggerFactory.getLogger(StakingEngine.class);

    @Autowired
    private CustomNodeMapper customNodeMapper;
    @Autowired
    private CustomStakingMapper customStakingMapper;
    @Autowired
    private CustomDelegationMapper customDelegationMapper;
    @Autowired
    private CustomUnDelegationMapper customUnDelegationMapper;

    /*********************业务事件处理器*********************/
    @Autowired
    private CreateValidatorHandler createValidatorHandler;
    @Autowired
    private EditValidatorHandler editValidatorHandler;
    @Autowired
    private IncreaseStakingHandler increaseStakingHandler;
    @Autowired
    private ExitValidatorHandler exitValidatorHandler;
    @Autowired
    private DelegateHandler delegateHandler;
    @Autowired
    private UnDelegateHandler unDelegateHandler;
    @Autowired
    private ReportValidatorHandler reportValidatorHandler;
    @Autowired
    private NewSettleEpochHandler newSettleEpochHandler;
    @Autowired
    private NewConsensusEpochHandler newConsensusEpochHandler;
    @Autowired
    private NewElectionEpochHandler newElectionEpochHandler;

    // 全量数据，需要根据业务变化，保持与数据库一致
    private NodeCache nodeCache = BlockChain.NODE_CACHE;

    private StakingStage stakingStage= BlockChain.STAGE_DATA.getStakingStage();

    private EventContext context = new EventContext();

    /**
     * 加载并构造节点缓存结构
     */
    public void loadNodes() throws CacheConstructException {
        List<CustomNode> nodeList = customNodeMapper.selectAll();
        List<String> nodeIds = new ArrayList<>();
        nodeList.forEach(node -> nodeIds.add(node.getNodeId()));
        if(nodeIds.size()==0) return;
        // |-加载质押记录
        List<CustomStaking> stakingList = customStakingMapper.selectByNodeIdList(nodeIds);
        // |-加载委托记录
        List<CustomDelegation> delegationList = customDelegationMapper.selectByNodeIdList(nodeIds);
        // |-加载撤销委托记录
        List<CustomUnDelegation> unDelegationList = customUnDelegationMapper.selectByNodeIdList(nodeIds);
        nodeCache.init(nodeList,stakingList,delegationList,unDelegationList);
    }

    @PostConstruct
    private void init() throws CacheConstructException {
        // 加载并构造节点缓存结构
        loadNodes();

        context.setStakingStage(stakingStage);
    }

    /**
     * 执行交易
     * @param tx 交易
     * @param bc BlockChain
     */
    void execute(CustomTransaction tx, BlockChain bc) throws NoSuchBeanException, BlockChainException {
        // 事件上下文
        context.setTransaction(tx);
        switch (tx.getTypeEnum()){
            case CREATE_VALIDATOR: createValidatorHandler.handle(context); break; //发起质押(创建验证人)
            case EDIT_VALIDATOR: editValidatorHandler.handle(context);break; //修改质押信息(编辑验证人)
            case INCREASE_STAKING: increaseStakingHandler.handle(context);break; //增持质押(增加自有质押)
            case EXIT_VALIDATOR: exitValidatorHandler.handle(context);break; //撤销质押(退出验证人)
            case DELEGATE: delegateHandler.handle(context);break; //发起委托(委托)
            case UN_DELEGATE: unDelegateHandler.handle(context);break; //减持/撤销委托(赎回委托)
            case REPORT_VALIDATOR: reportValidatorHandler.handle(context);break; //举报多签(举报验证人)
        }
        updateTxInfo(tx,bc);
    }

    /**
     * 进行验证人选举时触发
     */
    void onElectionDistance(CustomBlock block, BlockChain bc) throws ElectionEpochChangeException {
        logger.debug("进行验证人选举:{}", block.getNumber());
        // 事件上下文
        newElectionEpochHandler.handle(context);
    }

    /**
     * 进入新的共识周期变更
     */
    void onNewConsEpoch(CustomBlock block, BlockChain bc) throws ConsensusEpochChangeException, CandidateException {
        logger.debug("进入新的共识周期:{}", block.getNumber());
        // 事件上下文
        newConsensusEpochHandler.handle(context);
    }

    /**
     * 进入新的结算周期
     */
    void  onNewSettingEpoch(CustomBlock block, BlockChain bc) throws SettleEpochChangeException, CandidateException {
        logger.debug("进入新的结算周期:{}", block.getNumber());
        /*
         * 进入新的结算周期后需要变更的数据列表
         * 1.质押信息
         * 2.委托信息
         * 3.委托赎回信息
         */
        newSettleEpochHandler.handle(context);
    }

    private void updateTxInfo(CustomTransaction tx, BlockChain bc){

    }
}
