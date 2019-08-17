package com.platon.browser.engine;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.*;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.*;
import jdk.nashorn.internal.ir.UnaryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
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

    /**
     * 根据节点ID获取节点
     * @param nodeId
     * @return
     * @throws NoSuchBeanException
     */
    public CustomNode getNode(String nodeId) throws NoSuchBeanException {
        CustomNode node = nodes.get(nodeId);
        if(node==null) throw new NoSuchBeanException("节点(id="+nodeId+")的节点不存在");
        return node;
    }

    private StakingExecuteResult executeResult= BlockChain.STAGE_BIZ_DATA.getStakingExecuteResult();

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
        List<CustomNodeOpt> nodeOpts = customNodeOptMapper.selectByNodeIdList(nodeIds);
        nodeOpts.forEach(opt->nodes.get(opt.getNodeId()).getNodeOpts().add(opt));
        // |-加载节点惩罚记录
        List<CustomSlash> slashes = customSlashMapper.selectByNodeIdList(nodeIds);
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
                break;
            case UN_DELEGATE://减持/撤销委托(赎回委托)
                execute1005(tx,bc);
                break;
            case REPORT_VALIDATOR://举报多签(举报验证人)
                execute3000(tx,bc);
                break;
        }

        updateTxInfo(tx,bc);
    }

    /**
     * 进行验证人选举时触发
     */
    public void onElectionDistance(CustomBlock block){
        logger.debug("进行验证人选举:{}", block.getNumber());

    }

    /**
     * 进入新的共识周期变更
     */
    public void onNewConsEpoch(CustomBlock block){
        logger.debug("进入新的共识周期:{}", block.getNumber());

    }

    /**
     * 进入新的结算周期
     */
    public void  onNewSettingEpoch(CustomBlock block){
        logger.debug("进入新的结算周期:{}", block.getNumber());
        /**
         * 进入新的结算周期后需要变更的数据列表
         * 1.质押信息
         * 2.委托信息
         * 3.委托赎回信息
         */
        //委托信息变更
        modifyDelegationInfoOnNewSettingEpoch();
        //赎回委托信息变更
        modifyUnDelegationInfoOnNewSettingEpoch();
    }

    private void updateTxInfo(CustomTransaction tx, BlockChain bc){

    }

    //发起质押(创建验证人)
    private void execute1000(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        CreateValidatorParam param = tx.getTxParam(CreateValidatorParam.class);
        logger.debug("发起质押(创建验证人):{}", JSON.toJSONString(param));
        try {
            CustomNode node = getNode(param.getNodeId());
            /** 业务逻辑说明：
             *  1、如果当前质押交易质押的是已经质押过的节点，则:
             *     a、查询节点的有效质押记录（即staking表中status=1的记录），如果存在则不做任何处理（因为链上不允许对已质押的节点再做质押，即使重复质押交易成功，也不会对已质押节点造成任何影响）；
             *     b、如果节点没有有效质押记录（即staking表中status!=1），则插入一条新的质押记录；
             */
            logger.debug("节点(id={})已经被质押！",param.getNodeId());
            // 取当前节点最新质押信息
            try {
                CustomStaking latestStaking = node.getLatestStaking();
                if(latestStaking.getStatus()!= CustomStaking.StatusEnum.CANDIDATE.code){
                    // 如果当前节点最新质押信息无效，则添加一条质押信息
                    CustomStaking newStaking = new CustomStaking();
                    // 使用最新的质押交易更新相关信息
                    newStaking.updateWithCustomTransaction(tx);
                    // 把最新质押信息添加至缓存
                    node.getStakings().put(tx.getBlockNumber(),newStaking);
                    // 把最新质押信息添加至待入库列表
                    executeResult.stageAddStaking(newStaking,tx);
                }
            } catch (NoSuchBeanException e) {
                logger.error("{}",e.getMessage());
            }
        } catch (NoSuchBeanException e) {
            logger.debug("节点(id={})尚未被质押！",param.getNodeId());
            /** 业务逻辑说明：
             * 2、如果当前质押交易质押的是新节点，则在把新节点添加到缓存中，并放入待入库列表；
             */
            logger.error("节点(id={})未被质押！");
            CustomStaking staking = new CustomStaking();
            staking.updateWithCustomTransaction(tx);
            CustomNode node = new CustomNode();
            node.updateWithCustomStaking(staking);
            executeResult.stageAddNode(node);
            executeResult.stageAddStaking(staking,tx);
        }
    }
    //修改质押信息(编辑验证人)
    private void execute1001(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        EditValidatorParam param = tx.getTxParam(EditValidatorParam.class);
        logger.debug("修改质押信息(编辑验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithEditValidatorParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
    //增持质押(增加自有质押)
    private void execute1002(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        IncreaseStakingParam param = tx.getTxParam(IncreaseStakingParam.class);
        logger.debug("增持质押(增加自有质押):{}", JSON.toJSONString(param));
        try{
            CustomNode node = getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithIncreaseStakingParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
    //撤销质押(退出验证人)
    private void execute1003(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        ExitValidatorParam param = tx.getTxParam(ExitValidatorParam.class);
        logger.debug("撤销质押(退出验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithExitValidatorParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }

    // 发起委托(委托)
    private void execute1004 ( CustomTransaction tx, BlockChain bc ) {
        logger.debug("发起委托(委托)");
        DelegateParam param = tx.getTxParam(DelegateParam.class);

        try {
            CustomNode node = getNode(param.getNodeId());

            //获取treemap中最新一条质押数据数据
            //CustomStaking customStaking = node.getStakings().get(Long.valueOf(param.getStakingBlockNum()));
            try {
                CustomStaking latestStaking = node.getLatestStaking();

                //交易数据tx_info补全
                param.setNodeName(latestStaking.getStakingName());
                param.setStakingBlockNum(latestStaking.getStakingBlockNum().toString());
                //todo：交易数据回填
                tx.setTxInfo(JSON.toJSONString(param));


                //通过委托地址+nodeId+质押块高获取委托对象
                CustomDelegation customDelegation = latestStaking.getDelegations().get(tx.getFrom());

                //若已存在同地址，同节点，同块高的目标委托对象，则说明该地址对此节点没有做过委托
                //更新犹豫期金额
                if (customDelegation != null) {
                    customDelegation.setDelegateHas(new BigInteger(customDelegation.getDelegateHas()).add(new BigInteger(param.getAmount())).toString());
                    customDelegation.setIsHistory(CustomDelegation.YesNoEnum.NO.code);
                    //更新分析结果UpdateSet
                    executeResult.getUpdateDelegations().add(customDelegation);
                }

                //若不存在，则说明该地址有对此节点做过委托
                if (customDelegation == null) {
                    CustomDelegation newCustomDelegation = new CustomDelegation();
                    newCustomDelegation.updateWithDelegateParam(param, tx);
                    newCustomDelegation.setStakingBlockNum(latestStaking.getStakingBlockNum());
                    latestStaking.getDelegations().put(tx.getFrom(), newCustomDelegation);
                    //新增分析结果AddSet
                    executeResult.getAddDelegations().add(newCustomDelegation);
                }
            } catch (NoSuchBeanException e) {
                logger.error("{}", e.getMessage());
            }
        } catch (NoSuchBeanException e) {
            logger.error("无法获取节点信息: {}", e.getMessage());
        }
    }

    //减持/撤销委托(赎回委托)
    private void execute1005 ( CustomTransaction tx, BlockChain bc ) {
        logger.debug("减持/撤销委托(赎回委托)");
        UnDelegateParam param = tx.getTxParam(UnDelegateParam.class);
        try {
            CustomNode node = getNode(param.getNodeId());

            //根据委托赎回参数blockNumber找到对应当时委托的质押信息
            CustomStaking customStaking = node.getStakings().get(Long.valueOf(param.getStakingBlockNum()));

            //获取到对应质押节点的委托信息，key为委托地址（赎回委托交易发送地址）
            CustomDelegation customDelegation = customStaking.getDelegations().get(tx.getFrom());
            CustomUnDelegation customUnDelegation = new CustomUnDelegation();
            customUnDelegation.updateWithUnDelegateParam(param, tx);
            /*
             *  1.获取到对应的委托信息
             *  2.根据委托信息，判断，余额
             *  3.判断是否是全部退出
             *   a.yes
             *       委托的犹豫期金额 + 锁定期金额 - 赎回委托的金额 < 最小委托金额，则全部退出，并创建赎回委托结构
             *   b.no
             *       b1.若委托犹豫期金额 >= 本次赎回委托的金额，则直接扣去相应的金额
             *       b2.若委托犹豫期金额 < 本次赎回委托的金额，优先扣除犹豫期所剩的金额
             * */

            BigInteger delegationSum = new BigInteger(customDelegation.getDelegateHas()).add(new BigInteger(customDelegation.getDelegateHas()));
            if (delegationSum.compareTo(new BigInteger(bc.getChainConfig().getMinimumThreshold())) == -1) {
                //委托赎回金额为 =  原赎回金额 + 锁仓金额
                customDelegation.setDelegateReduction(new BigInteger(customDelegation.getDelegateReduction()).add(new BigInteger(customDelegation.getDelegateLocked())).toString());
                customDelegation.setDelegateHas("0");
                customDelegation.setDelegateLocked("0");
                //设置赎回委托结构中的赎回锁定金额
                customUnDelegation.setRedeemLocked(customDelegation.getDelegateReduction());
            } else {

                if (new BigInteger(customDelegation.getDelegateHas()).compareTo(new BigInteger(param.getAmount())) == 1) {
                    //犹豫期的金额 > 赎回委托金额，直接扣除犹豫期金额
                    //该委托的变更犹豫期金额 = 委托原本的犹豫期金额 - 委托赎回的金额
                    customDelegation.setDelegateHas(new BigInteger(customDelegation.getDelegateHas()).subtract(new BigInteger(param.getAmount())).toString());
                } else {
                    //犹豫期金额 < 赎回委托金额，优先扣除所剩的犹豫期金额，不足的从锁定期金额中扣除
                    customDelegation.setDelegateLocked(
                            new BigInteger(customDelegation.getDelegateLocked()).add
                                    (new BigInteger(customDelegation.getDelegateHas())).subtract
                                    (new BigInteger(param.getAmount())).toString());
                    //优先扣除所剩的犹豫期的金额，剩余委托赎回金额 = 原本需要赎回的金额 - 委托的犹豫期的金额
                    customUnDelegation.setRedeemLocked(new BigInteger(param.getAmount()).subtract(new BigInteger(customDelegation.getDelegateHas())).toString());
                    //设置委托中的赎回金额，经过分析后的委托赎回金额 = 委托赎回金额 + 委托锁定期金额
                    customDelegation.setDelegateReduction(new BigInteger(customDelegation.getDelegateReduction()).add(new BigInteger(customUnDelegation.getRedeemLocked())).toString());
                }
            }
            //判断此赎回委托的交易对应的委托交易是否完成，若完成则将更新委托交易，设置成委托历史；委托犹豫期金额 + 委托锁定期金额 + 委托赎回金额，是否等于0
            if (new BigInteger(customDelegation.getDelegateHas()).add(new BigInteger(customDelegation.getDelegateLocked())).add(new BigInteger(customDelegation.getDelegateReduction())).equals(BigInteger.ZERO)) {
                customDelegation.setIsHistory(CustomDelegation.YesNoEnum.YES.code);
            }
            //判断此委托赎回是否已经完成
            if (new BigInteger(customUnDelegation.getRedeemLocked()) == BigInteger.ZERO) {
                //锁定期赎回金额为0则表示：本次赎回的金额在犹豫期金额足够，全部扣除，本次委托赎回已经完成
                customUnDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.code);
            } else
                customUnDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITING.code);
            //更新分析委托结果
            executeResult.getUpdateDelegations().add(customDelegation);
            //新增分析委托赎回结果
            executeResult.getAddUnDelegations().add(customUnDelegation);
        } catch (NoSuchBeanException e) {
            logger.error("{}", e.getMessage());
        }
    }

    //举报多签(举报验证人)
    private void execute3000 ( CustomTransaction tx, BlockChain bc ) {
        logger.debug("举报多签(举报验证人)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }

    //结算周期变更导致的委托数据的变更
    private void modifyDelegationInfoOnNewSettingEpoch () {
        //由于结算周期的变更，对所有的节点下的质押的委托更新
        nodes.forEach(( nodeId, node ) -> {
            node.getStakings().forEach((( aLong, customStaking ) -> {
                customStaking.getDelegations().forEach(( k, v ) -> {
                    //只需变更不为历史节点的委托数据(isHistory=NO(2))
                    if (v.getIsHistory().equals(CustomDelegation.YesNoEnum.NO.code)) {
                        //经过结算周期的变更，上个周期的犹豫期金额累加到锁定期的金额
                        v.setDelegateLocked(new BigInteger(v.getDelegateLocked()).add(new BigInteger(v.getDelegateHas())).toString());
                        //累加后的犹豫期金额至为0
                        v.setDelegateHas("0");
                        v.setDelegateReduction("0");
                        //并判断经过一个结算周期后该委托的对应赎回是否全部完成，若完成则将委托设置为历史节点
                        //判断条件委托的犹豫期金额 + 委托的锁定期金额 + 委托的赎回金额是否等于0
                        if (new BigInteger(v.getDelegateHas()).add(new BigInteger(v.getDelegateLocked())).add(new BigInteger(v.getDelegateReduction())) == BigInteger.ZERO) {
                            v.setIsHistory(CustomDelegation.YesNoEnum.YES.code);
                        }
                        //添加需要更新的委托的信息到委托更新列表
                        executeResult.getUpdateDelegations().add(v);
                    }
                });
            }));
        });
    }

    //结算周期变更导致的委托赎回的变更
    private void modifyUnDelegationInfoOnNewSettingEpoch () {
        //由于结算周期的变更，对所有的节点下的质押的委托的委托赎回更新
        nodes.forEach(( nodeId, node ) -> {
            node.getStakings().forEach((( aLong, customStaking ) -> {
                customStaking.getDelegations().forEach(( k, v ) -> {
                    v.getUnDelegations().forEach(unDelegation -> {
                        //经过结算周期的变更，判断赎回委托的结果
                        if(unDelegation.getStatus().equals(CustomUnDelegation.StatusEnum.EXITING.code)){
                            //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
                            unDelegation.setRedeemLocked("0");
                            //当锁定期金额为零时，认为此笔赎回委托交易已经完成
                            unDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.code);
                        }
                        //添加需要更新的赎回委托信息到赎回委托更新列表
                        executeResult.getUpdateUnDelegations().add(unDelegation);
                    });
                });
            }));
        });
    }


    /*************************由外部调用的方法*************************/
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
        executeResult.stageUpdateNode(node);
    }
}
