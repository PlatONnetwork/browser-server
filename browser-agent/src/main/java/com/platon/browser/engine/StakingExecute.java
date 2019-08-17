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
    private NodeCache nodeCache = new NodeCache();

    private StakingExecuteResult executeResult= BlockChain.STAGE_BIZ_DATA.getStakingExecuteResult();

    public void loadNodes(){
        List<CustomNode> nodeList = customNodeMapper.selectAll();
        List<String> nodeIds = new ArrayList<>();
        nodeList.forEach(node -> {
            nodeIds.add(node.getNodeId());
            nodeCache.add(node);
        });
        if(nodeIds.size()==0) return;
        // |-加载质押记录
        List<CustomStaking> stakings = customStakingMapper.selectByNodeIdList(nodeIds);
        // <节点ID+质押块号 - 质押记录> 映射, 方便【委托记录】的添加
        Map<String, CustomStaking> stakingMap = new HashMap<>();
        stakings.forEach(staking->{
            try {
                nodeCache.getNode(staking.getNodeId()).getStakings().put(staking.getStakingBlockNum(),staking);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联质押(stakingBlockNumber={})",e.getMessage(),staking.getStakingBlockNum());
            }
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
        nodeOpts.forEach(opt-> {
            try {
                nodeCache.getNode(opt.getNodeId()).getNodeOpts().add(opt);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联节点操作日志(id={})",e.getMessage(),opt.getId());
            }
        });
        // |-加载节点惩罚记录
        List<CustomSlash> slashes = customSlashMapper.selectByNodeIdList(nodeIds);
        slashes.forEach(slash -> {
            try {
                nodeCache.getNode(slash.getNodeId()).getSlashes().add(slash);
            } catch (NoSuchBeanException e) {
                logger.error("构造缓存错误:{}, 无法向其关联节点惩罚记录(slashTxHash={})",e.getMessage(),slash.getHash());
            }
        });
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
            case CREATE_VALIDATOR: createValidator(tx,bc); break; //发起质押(创建验证人)
            case EDIT_VALIDATOR: editValidator(tx,bc);break; //修改质押信息(编辑验证人)
            case INCREASE_STAKING: increaseStaking(tx,bc);break; //增持质押(增加自有质押)
            case EXIT_VALIDATOR: exitValidator(tx,bc);break; //撤销质押(退出验证人)
            case DELEGATE: delegate(tx,bc);break; //发起委托(委托)
            case UN_DELEGATE: unDelegate(tx,bc);break; //减持/撤销委托(赎回委托)
            case REPORT_VALIDATOR: reportValidator(tx,bc);break; //举报多签(举报验证人)
        }
        updateTxInfo(tx,bc);
    }

    /**
     * 进行验证人选举时触发
     */
    public void onElectionDistance(CustomBlock block,BlockChain bc){
        logger.debug("进行验证人选举:{}", block.getNumber());

    }

    /**
     * 进入新的共识周期变更
     */
    public void onNewConsEpoch(CustomBlock block,BlockChain bc){
        logger.debug("进入新的共识周期:{}", block.getNumber());

    }

    /**
     * 进入新的结算周期
     */
    public void  onNewSettingEpoch(CustomBlock block,BlockChain bc){
        logger.debug("进入新的结算周期:{}", block.getNumber());

        /**
         * 质押结算
         */
        stakingSettle(block,bc);

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

    /**
     * 质押结算
     * 对所有候选中和退出中的节点进行结算
     * @param block 结算
     */
    private void stakingSettle(CustomBlock block,BlockChain bc) {
        // 结算周期切换时对所有候选中和退出中状态的节点进行结算
        List<CustomStaking> stakings = nodeCache.getStakingByStatus(Arrays.asList(CustomStaking.StatusEnum.CANDIDATE,CustomStaking.StatusEnum.EXITING));
        stakings.forEach(staking -> {
            // 调整金额状态
            BigInteger stakingLocked = new BigInteger(staking.getStakingLocked()).add(new BigInteger(staking.getStakingHas()));
            staking.setStakingLocked(stakingLocked.toString());
            staking.setStakingHas("0");
            if(bc.getCurSettingEpoch() > staking.getStakingReductionEpoch()+1){
                //
                staking.setStakingReduction("0");
            }
            BigInteger stakingReduction = new BigInteger(staking.getStakingReduction());
            if(stakingLocked.add(stakingReduction).compareTo(BigInteger.ZERO)==0){
                staking.setStatus(CustomStaking.StatusEnum.EXITED.code);
            }
            // 计算质押激励和年化率
                    });

    }

    private void updateTxInfo(CustomTransaction tx, BlockChain bc){

    }

    //发起质押(创建验证人)
    private void createValidator(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        CreateValidatorParam param = tx.getTxParam(CreateValidatorParam.class);
        logger.debug("发起质押(创建验证人):{}", JSON.toJSONString(param));
        try {
            CustomNode node = nodeCache.getNode(param.getNodeId());
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
    private void editValidator(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        EditValidatorParam param = tx.getTxParam(EditValidatorParam.class);
        logger.debug("修改质押信息(编辑验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithEditValidatorParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
    //增持质押(增加自有质押)
    private void increaseStaking(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        IncreaseStakingParam param = tx.getTxParam(IncreaseStakingParam.class);
        logger.debug("增持质押(增加自有质押):{}", JSON.toJSONString(param));
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithIncreaseStakingParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }
    //撤销质押(退出验证人)
    private void exitValidator(CustomTransaction tx, BlockChain bc){
        // 获取交易入参
        ExitValidatorParam param = tx.getTxParam(ExitValidatorParam.class);
        logger.debug("撤销质押(退出验证人):{}", JSON.toJSONString(param));
        try{
            CustomNode node = nodeCache.getNode(param.getNodeId());
            // 取当前节点最新质押信息来修改
            CustomStaking latestStaking = node.getLatestStaking();
            latestStaking.updateWithExitValidatorParam(param);
            executeResult.stageUpdateStaking(latestStaking,tx);
        } catch (NoSuchBeanException e) {
            logger.error("无法修改质押信息: {}",e.getMessage());
        }
    }

    // 发起委托(委托)
    private void delegate ( CustomTransaction tx, BlockChain bc ) {
        logger.debug("发起委托(委托)");
        DelegateParam param = tx.getTxParam(DelegateParam.class);

        try {
            CustomNode node = nodeCache.getNode(param.getNodeId());

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
                    executeResult.stageUpdateDelegation(customDelegation);
                }

                //若不存在，则说明该地址有对此节点做过委托
                if (customDelegation == null) {
                    CustomDelegation newCustomDelegation = new CustomDelegation();
                    newCustomDelegation.updateWithDelegateParam(param, tx);
                    newCustomDelegation.setStakingBlockNum(latestStaking.getStakingBlockNum());
                    latestStaking.getDelegations().put(tx.getFrom(), newCustomDelegation);
                    //新增分析结果AddSet
                    executeResult.stageAddDelegation(newCustomDelegation);
                }
            } catch (NoSuchBeanException e) {
                logger.error("{}", e.getMessage());
            }
        } catch (NoSuchBeanException e) {
            logger.error("无法获取节点信息: {}", e.getMessage());
        }
    }

    //减持/撤销委托(赎回委托)
    private void unDelegate ( CustomTransaction tx, BlockChain bc ) {
        logger.debug("减持/撤销委托(赎回委托)");
        UnDelegateParam param = tx.getTxParam(UnDelegateParam.class);
        try {
            CustomNode node = nodeCache.getNode(param.getNodeId());

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
            executeResult.stageUpdateDelegation(customDelegation);
            //新增分析委托赎回结果
            executeResult.stageAddUnDelegation(customUnDelegation);
        } catch (NoSuchBeanException e) {
            logger.error("{}", e.getMessage());
        }
    }

    //举报多签(举报验证人)
    private void reportValidator ( CustomTransaction tx, BlockChain bc ) {
        logger.debug("举报多签(举报验证人)");
        // TODO: 修改验证人列表
        // 修改验证人列表
    }

    //结算周期变更导致的委托数据的变更
    private void modifyDelegationInfoOnNewSettingEpoch () {
        //由于结算周期的变更，对所有的节点下的质押的委托更新
        //只需变更不为历史节点的委托数据(isHistory=NO(2))
        List<CustomDelegation> delegations = nodeCache.getDelegationByIsHistory(Collections.singletonList(CustomDelegation.YesNoEnum.NO));
        delegations.forEach(delegation->{
            //经过结算周期的变更，上个周期的犹豫期金额累加到锁定期的金额
            delegation.setDelegateLocked(new BigInteger(delegation.getDelegateLocked()).add(new BigInteger(delegation.getDelegateHas())).toString());
            //累加后的犹豫期金额至为0
            delegation.setDelegateHas("0");
            delegation.setDelegateReduction("0");
            //并判断经过一个结算周期后该委托的对应赎回是否全部完成，若完成则将委托设置为历史节点
            //判断条件委托的犹豫期金额 + 委托的锁定期金额 + 委托的赎回金额是否等于0
            if (new BigInteger(delegation.getDelegateHas()).add(new BigInteger(delegation.getDelegateLocked())).add(new BigInteger(delegation.getDelegateReduction())) == BigInteger.ZERO) {
                delegation.setIsHistory(CustomDelegation.YesNoEnum.YES.code);
            }
            //添加需要更新的委托的信息到委托更新列表
            executeResult.stageUpdateDelegation(delegation);
        });
    }

    //结算周期变更导致的委托赎回的变更
    private void modifyUnDelegationInfoOnNewSettingEpoch () {
        //由于结算周期的变更，对所有的节点下的质押的委托的委托赎回更新
        //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
        List<CustomUnDelegation> unDelegations = nodeCache.getUnDelegationByStatus(Collections.singletonList(CustomUnDelegation.StatusEnum.EXITING));
        unDelegations.forEach(unDelegation -> {
            //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
            unDelegation.setRedeemLocked("0");
            //当锁定期金额为零时，认为此笔赎回委托交易已经完成
            unDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.code);
            //添加需要更新的赎回委托信息到赎回委托更新列表
            executeResult.stageUpdateUnDelegation(unDelegation);
        });
    }


    /*************************由外部调用的方法*************************/
    /**
     * 更新node表中的节点出块数信息: stat_block_qty, 由blockChain.execute()调用
     * @param nodeId
     */
    public void updateNodeStatBlockQty(String nodeId){
        try {
            CustomNode node = nodeCache.getNode(nodeId);
            node.setStatBlockQty(node.getStatBlockQty()+1);
            executeResult.stageUpdateNode(node);
        } catch (NoSuchBeanException e) {
            logger.error("{}",e.getMessage());
        }
    }
}
