package com.platon.browser.engine;

import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.*;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.*;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.engine.result.StakingExecuteResult;
import com.platon.browser.exception.ConsensusEpochChangeException;
import com.platon.browser.exception.ElectionEpochChangeException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.exception.SettleEpochChangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class StakingExecute {
    private static Logger logger = LoggerFactory.getLogger(StakingExecute.class);

    @Autowired
    private CustomNodeMapper customNodeMapper;
    @Autowired
    private CustomStakingMapper customStakingMapper;
    @Autowired
    private CustomNodeOptMapper customNodeOptMapper;
    @Autowired
    private CustomSlashMapper customSlashMapper;
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

    private StakingExecuteResult executeResult= BlockChain.STAGE_BIZ_DATA.getStakingExecuteResult();

    /**
     * 加载并构造节点缓存结构
     */
    public void loadNodes(){
        List<CustomNode> nodeList = customNodeMapper.selectAll();
        List<String> nodeIds = new ArrayList<>();
        nodeList.forEach(node -> {
            nodeIds.add(node.getNodeId());
            nodeCache.addNode(node);
        });
        if(nodeIds.size()==0) return;
        // |-加载质押记录
        List<CustomStaking> stakings = customStakingMapper.selectByNodeIdList(nodeIds);
        stakings.forEach(staking->{
            try {
                nodeCache.addStaking(staking);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联质押(stakingBlockNumber={})",e.getMessage(),staking.getStakingBlockNum());
            }
        });
        // |-加载委托记录
        List<CustomDelegation> delegations = customDelegationMapper.selectByNodeIdList(nodeIds);
        delegations.forEach(delegation->{
            try {
                nodeCache.addDelegation(delegation);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联委托(stakingBlockNumber={})",e.getMessage(),delegation.getStakingBlockNum());
            }
        });
        // |-加载撤销委托记录
        List<CustomUnDelegation> unDelegations = customUnDelegationMapper.selectByNodeIdList(nodeIds);
        unDelegations.forEach(unDelegation->{
            try {
                nodeCache.addUnDelegation(unDelegation);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联解委托(stakingBlockNumber={})",e.getMessage(),unDelegation.getStakingBlockNum());
            }
        });
        // |-加载节点操作记录
        List<CustomNodeOpt> nodeOpts = customNodeOptMapper.selectByNodeIdList(nodeIds);
        nodeOpts.forEach(opt-> {
            try {
                nodeCache.addNodeOpt(opt);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联节点操作日志(id={})",e.getMessage(),opt.getId());
            }
        });
        // |-加载节点惩罚记录
        List<CustomSlash> slashes = customSlashMapper.selectByNodeIdList(nodeIds);
        slashes.forEach(slash -> {
            try {
                nodeCache.addSlash(slash);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联节点惩罚记录(slashTxHash={})",e.getMessage(),slash.getHash());
            }
        });
    }

    @PostConstruct
    private void init(){
        /***把当前库中的验证人列表加载到内存中**/
        // 加载并构造节点缓存结构
        loadNodes();
    }

    /**
     * 执行交易
     * @param tx
     * @param bc
     */
    public void execute(CustomTransaction tx, BlockChain bc){
        // 事件上下文
        EventContext context = new EventContext(tx,bc,nodeCache,executeResult,null);
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
    public void onElectionDistance(CustomBlock block,BlockChain bc) throws ElectionEpochChangeException {
        logger.debug("进行验证人选举:{}", block.getNumber());
        // 事件上下文
        EventContext context = new EventContext(null,bc,nodeCache,executeResult,null);
        newElectionEpochHandler.handle(context);
    }

    /**
     * 进入新的共识周期变更
     */
    public void onNewConsEpoch(CustomBlock block,BlockChain bc) throws ConsensusEpochChangeException {
        logger.debug("进入新的共识周期:{}", block.getNumber());
        // 事件上下文
        EventContext context = new EventContext(null,bc,nodeCache,executeResult,null);
        newConsensusEpochHandler.handle(context);
    }

    /**
     * 进入新的结算周期
     */
    public void  onNewSettingEpoch(CustomBlock block,BlockChain bc) throws SettleEpochChangeException {
        logger.debug("进入新的结算周期:{}", block.getNumber());
        // 事件上下文
        EventContext context = new EventContext(null,bc,nodeCache,executeResult,null);

        /**
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
