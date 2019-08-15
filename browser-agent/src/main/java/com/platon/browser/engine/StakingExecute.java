package com.platon.browser.engine;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

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

    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String, NodeBean> nodes = new TreeMap<>(); // 节点统计表

    private StakingExecuteResult executeResult= new StakingExecuteResult();


    private void loadNodes(){
        List<NodeBean> nodeList = customNodeMapper.selectAll();
        List<String> nodeIds = new ArrayList<>();
        nodeList.forEach(node -> {
            nodeIds.add(node.getNodeId());
            nodes.put(node.getNodeId(),node);
        });
        if(nodeIds.size()==0) return;
        // |-加载质押记录
        List<StakingBean> stakings = customStakingMapper.selectByNodeIdList(nodeIds);
        // <节点ID+质押块号 - 质押记录> 映射, 方便【委托记录】的添加
        Map<String,StakingBean> nodeIdStakingNum_Staking_Map = new HashMap<>();
        stakings.forEach(staking->{
            nodes.get(staking.getNodeId()).getStakings().put(staking.getStakingBlockNum(),staking);
            nodeIdStakingNum_Staking_Map.put(staking.getMapKey(),staking);
        });
        // |-加载委托记录
        List<DelegationBean> delegations = customDelegationMapper.selectByNodeIdList(nodeIds);
        // <节点ID+质押块号 - 质押记录> 映射, 方便【撤销委托记录】的添加
        Map<String,DelegationBean> delegateAddrNodeIdStakingNum_Delegation_Map = new HashMap<>();
        delegations.forEach(delegation->{
            StakingBean staking = nodeIdStakingNum_Staking_Map.get(delegation.getStakingMapKey());
            if(staking!=null) {
                staking.getDelegations().put(delegation.getDelegateAddr(),delegation);
                delegateAddrNodeIdStakingNum_Delegation_Map.put(delegation.getDelegationMapKey(),delegation);
            }
        });
        // |-加载撤销委托记录
        List<UnDelegationBean> unDelegations = customUnDelegationMapper.selectByNodeIdList(nodeIds);
        unDelegations.forEach(unDelegation->{
            DelegationBean delegation = delegateAddrNodeIdStakingNum_Delegation_Map.get(unDelegation.getDelegationMapKey());
            if(delegation!=null){
                delegation.getUnDelegations().add(unDelegation);
            }
        });
        // |-加载节点操作记录
        List<NodeOpt> nodeOpts = customNodeOptMapper.selectByNodeIdList(nodeIds);
        nodeOpts.forEach(opt->nodes.get(opt.getNodeId()).getNodeOpts().add(opt));
        // |-加载节点惩罚记录
        List<Slash> slashes = customSlashMapper.selectByNodeIdList(nodeIds);
        slashes.forEach(slash -> nodes.get(slash.getNodeId()).getSlashes().add(slash));
    }

    @PostConstruct
    public void init(){
        /***把当前库中的验证人列表加载到内存中**/
        // 初始化当前结算周期验证人列表
        loadNodes();

        logger.debug("{}", JSON.toJSONString(nodes,true));
    }

    /**
     * 执行交易
     * @param tx
     * @param bc
     */
    public void execute(TransactionBean tx, BlockChain bc){
        switch (tx.getTypeEnum()){
            case CREATEVALIDATOR:
                execute1000(tx,bc);
                break;
            case EDITVALIDATOR:
                execute1001(tx,bc);
                break;
            case INCREASESTAKING:
                execute1002(tx,bc);
                break;
            case EXITVALIDATOR:
                execute1003(tx,bc);
                break;
            case UNDELEGATE:
                execute1005(tx,bc);
                break;
            case REPORTVALIDATOR:
                execute3000(tx,bc);
                break;
        }

        updateTxInfo(tx,bc);
    }

    public StakingExecuteResult exportResult(){
        return executeResult;
    }

    public void commitResult(){
        executeResult.getAddDelegations().clear();

    }

    /**
     * 进入新的结算周期
     */
    public void onNewSettingEpoch(){

    }

    /**
     * 进入新的共识周期变更
     */
    public void onNewConsEpoch(){

    }

    /**
     * 进行选择验证人时触发
     */
    public void onElectionDistance(){

    }

    private void updateTxInfo(TransactionBean tx, BlockChain bc){

    }

    //发起质押(创建验证人)
    private void execute1000(TransactionBean tx, BlockChain bc){
        StakingBean staking = new StakingBean();
        staking.initWithCreateValidatorDto(tx);

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //修改质押信息(编辑验证人)
    private void execute1001(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //增持质押(增加自有质押)
    private void execute1002(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //撤销质押(退出验证人)
    private void execute1003(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //减持/撤销委托(赎回委托)
    private void execute1005(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //举报多签(举报验证人)
    private void execute3000(TransactionBean tx, BlockChain bc){

        // TODO: 修改验证人列表
        // 修改验证人列表
    }
}
