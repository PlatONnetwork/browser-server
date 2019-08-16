package com.platon.browser.engine;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.*;
import com.platon.browser.param.CreateValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    private Map<String, CustomNode> nodes = new TreeMap<>(); // 节点统计表

    private StakingExecuteResult executeResult= BlockChain.BIZ_DATA.getStakingExecuteResult();


    public void loadNodes(){
        List<CustomNode> nodeList = customNodeMapper.selectAll();
        List<String> nodeIds = new ArrayList<>();
        nodeList.forEach(node -> {
            nodeIds.add(node.getNodeId());
            nodes.put(node.getNodeId(),node);
        });
        if(nodeIds.size()==0) return;
        // |-加载质押记录
        List<CustomStaking> stakings = customStakingMapper.selectByNodeIdList(nodeIds);
        // <节点ID+质押块号 - 质押记录> 映射, 方便【委托记录】的添加
        Map<String, CustomStaking> stakingMap = new HashMap<>();
        stakings.forEach(staking->{
            nodes.get(staking.getNodeId()).getStakings().put(staking.getStakingBlockNum(),staking);
            stakingMap.put(staking.getStakingMapKey(),staking);
        });
        // |-加载委托记录
        List<CustomDelegation> delegations = customDelegationMapper.selectByNodeIdList(nodeIds);
        // <节点ID+质押块号 - 质押记录> 映射, 方便【撤销委托记录】的添加
        Map<String, CustomDelegation> delegationMap = new HashMap<>();
        delegations.forEach(delegation->{
            CustomStaking staking = stakingMap.get(delegation.getStakingMapKey());
            if(staking!=null) {
                staking.getDelegations().put(delegation.getDelegateAddr(),delegation);
                delegationMap.put(delegation.getDelegationMapKey(),delegation);
            }
        });
        // |-加载撤销委托记录
        List<CustomUnDelegation> unDelegations = customUnDelegationMapper.selectByNodeIdList(nodeIds);
        unDelegations.forEach(unDelegation->{
            CustomDelegation delegation = delegationMap.get(unDelegation.getDelegationMapKey());
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
    private void init(){
        /***把当前库中的验证人列表加载到内存中**/
        // 初始化当前结算周期验证人列表
        loadNodes();
    }

    /**
     * 执行交易
     * @param tx
     * @param bc
     */
    public void execute(CustomTransaction tx, BlockChain bc){
        switch (tx.getTypeEnum()){
            case CREATE_VALIDATOR: //发起质押(创建验证人)
                execute1000(tx,bc);
                break;
            case EDIT_VALIDATOR: //修改质押信息(编辑验证人)
                execute1001(tx,bc);
                break;
            case INCREASE_STAKING: //增持质押(增加自有质押)
                execute1002(tx,bc);
                break;
            case EXIT_VALIDATOR://撤销质押(退出验证人)
                execute1003(tx,bc);
                break;
            case DELEGATE://发起委托(委托)
                execute1004(tx,bc);
            case UN_DELEGATE://减持/撤销委托(赎回委托)
                execute1005(tx,bc);
                break;
            case REPORT_VALIDATOR://举报多签(举报验证人)
                execute3000(tx,bc);
                break;
        }

        updateTxInfo(tx,bc);
    }

    public StakingExecuteResult exportResult(){
        return executeResult;
    }

    /**
     * 清除待入库或待更新缓存，需要在入库后调用
     */
    public void commitResult(){
        executeResult.clear();
    }

    /**
     * 进入新的结算周期
     */
    public void  onNewSettingEpoch(){

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

    /**
     * 更新node表中的节点出块数信息: stat_block_qty, 由blockChain.execute()调用
     * @param nodeId
     */
    public void updateNodeStatBlockQty(String nodeId){
        CustomNode node = nodes.get(nodeId);
        if(node==null){
            logger.error("节点(ID={})未存入质押节点列表，无法统计出块数！",nodeId);
            return;
        }
        node.setStatBlockQty(node.getStatBlockQty()+1);
        executeResult.getUpdateNodes().add(node);
    }

    private void updateTxInfo(CustomTransaction tx, BlockChain bc){

    }

    //发起质押(创建验证人)
    private void execute1000(CustomTransaction tx, BlockChain bc){
        logger.debug("发起质押(创建验证人)");

        /** 业务逻辑说明：
         *  1、如果当前质押交易质押的是已经质押过的节点，则:
         *     a、查询节点的有效质押记录（即staking表中status=1的记录），如果存在则不做任何处理（因为链上不允许对已质押的节点再做质押，即使重复质押交易成功，也不会对已质押节点造成任何影响）；
         *     b、如果节点没有有效质押记录（即staking表中status!=1），则插入一条新的质押记录；
         *  2、如果当前质押交易质押的是新节点，则在把新节点添加到缓存中，并放入待入库列表；
         */
        // 获取交易入参
        CreateValidatorParam param = tx.getTxParam(CreateValidatorParam.class);
        CustomNode node = nodes.get(param.getNodeId());
        if(node!=null){
            logger.error("节点(id={})已经被质押！");
            // 取最近一条质押信息
            Map.Entry<Long, CustomStaking> lastEntry = node.getStakings().lastEntry();
            CustomStaking latestStaking = lastEntry.getValue();
            if(latestStaking.getStatus()!= CustomStaking.StatusEnum.CANDIDATE.code){
                // 如果当前节点最新质押信息无效，则添加一条质押信息
                CustomStaking newStaking = new CustomStaking();
                // 把旧质押信息复制至新质押
                BeanUtils.copyProperties(latestStaking,newStaking);
                // 使用最新的质押交易信息覆盖相关信息
                newStaking.initWithTransactionBean(tx);
                // 把最新质押信息添加至缓存
                node.getStakings().put(tx.getBlockNumber(),newStaking);
                // 把最新质押信息添加至待入库列表
                executeResult.getAddStakings().add(newStaking);
            }
        }

        if(node==null){
            logger.error("节点(id={})未被质押！");

        }

    }
    //修改质押信息(编辑验证人)
    private void execute1001(CustomTransaction tx, BlockChain bc){
        logger.debug("修改质押信息(编辑验证人)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //增持质押(增加自有质押)
    private void execute1002(CustomTransaction tx, BlockChain bc){
        logger.debug("增持质押(增加自有质押)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //撤销质押(退出验证人)
    private void execute1003(CustomTransaction tx, BlockChain bc){
        logger.debug("撤销质押(退出验证人)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    // 发起委托(委托)
    private void execute1004(CustomTransaction tx, BlockChain bc){
        logger.debug("撤销质押(退出验证人)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //减持/撤销委托(赎回委托)
    private void execute1005(CustomTransaction tx, BlockChain bc){
        logger.debug("减持/撤销委托(赎回委托)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }
    //举报多签(举报验证人)
    private void execute3000(CustomTransaction tx, BlockChain bc){
        logger.debug("举报多签(举报验证人)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }

    public TreeMap<String, Staking> getStakingCache () {
        TreeMap<String, Staking> stakingCache = new TreeMap <>();
        nodes.forEach((nodeId,node)->node.getStakings().forEach((stakingBlockNumber,staking)->stakingCache.put(staking.getStakingAddr(),staking)));
        return stakingCache;
    }

    public TreeMap<String, Delegation> getDelegationCache () {
        TreeMap<String, Delegation> delegationCache = new TreeMap <>();
        nodes.forEach((nodeId,node)->node.getStakings().forEach((stakingBlockNumber,staking)->staking.getDelegations().forEach((key,delegation)->delegationCache.put(delegation.getDelegateAddr(),delegation))));
        return delegationCache;
    }
}
