package com.platon.browser.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.bean.AnnualizedRateInfo;
import com.platon.browser.engine.bean.PeriodValueElement;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.exception.SettleEpochChangeException;
import com.platon.browser.service.CandidateService;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 结算周期变更事件处理类
 */
@Component
public class NewSettleEpochHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NewSettleEpochHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatOnClient client;
    private StakingStage stakingStage;
    @Autowired
    private SpecialContractApi sca;

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CacheHolder cacheHolder;

    private NodeCache nodeCache;

    @Override
    public void handle(EventContext context) throws CandidateException, SettleEpochChangeException, InterruptedException {
        nodeCache = cacheHolder.getNodeCache();
        stakingStage = cacheHolder.getStageData().getStakingStage();
        updateVerifier(); // 更新缓存中的辅助结算周期验证人信息
        settleStaking(); // 结算
        updateDelegation(); // 更新委托信息
        updateUnDelegation(); // 更新解委托信息
    }


    /**
     * 更新结算周期验证人
     * // 假设当前链上最高区块号为750
     * 1         250        500        750
     * |----------|----------|----------|
     * A B C      A C D       B C D
     * 结算周期的临界块号分别是：1,250,500,750
     * 使用临界块号查到的验证人：1=>"A,B,C",250=>"A,B,C",500=>"A,C,D",750=>"B,C,D"
     * 如果当前区块号为753，由于未达到
     */
    private void updateVerifier () throws CandidateException, InterruptedException {
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        List <Node> preVerifier;
        // ==================================更新前一周期验证人列表=======================================
        BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
        while (true){
            try {
                // 使用当前区块号查询前一结算周期验证人
                preVerifier = sca.getHistoryVerifierList(client.getWeb3j(),prevEpochLastBlockNumber);
                bc.getPreVerifier().clear();
                preVerifier.stream().filter(Objects::nonNull).forEach(node -> bc.getPreVerifier().put(HexTool.prefix(node.getNodeId()), node));
                String msg = JSON.toJSONString(preVerifier,true);
                logger.debug("前一轮结算周期(未块:{})验证人:{}",blockNumber,msg);
                break;
            } catch (Exception e) {
                logger.error("【查询前轮结算验证人-底层出错】使用块号【{}】查询结算周期验证人出错,将重试:{}",prevEpochLastBlockNumber,e.getMessage());
                client.updateCurrentValidWeb3j();
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        // 把前一结算周期验证人状态置否
        updateIsSetting(preVerifier, CustomStaking.YesNoEnum.NO);

        // ==================================更新下一轮结算周期验证人列表=======================================
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
        List<Node> curVerifier;
        try {
            curVerifier = sca.getHistoryVerifierList(client.getWeb3j(),nextEpochFirstBlockNumber);
            String msg = JSON.toJSONString(curVerifier,true);
            logger.debug("下一轮结算周期验证人(始块:{}):{}",nextEpochFirstBlockNumber,msg);
        } catch (Exception e) {
            logger.info("使用指定块号查询下一轮结算周期验证人出错,将调用实时查询接口:{}",e.getMessage());
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            curVerifier=candidateService.getCurVerifiers();
            logger.debug("下一轮结算周期验证人(实时):{}",JSON.toJSONString(curVerifier,true));
        }
        bc.getCurVerifier().clear();
        curVerifier.stream().filter(Objects::nonNull).forEach(node -> bc.getCurVerifier().put(HexTool.prefix(node.getNodeId()), node));

        if(bc.getCurVerifier().isEmpty()){
            throw new CandidateException("查询不到下一轮结算周期验证人(当前块号="+blockNumber+",当前结算轮数="+bc.getCurSettingEpoch()+")");
        }

        // 把下一结算周期验证人状态置是
        updateIsSetting(curVerifier, CustomStaking.YesNoEnum.YES);

        String msg = JSON.toJSONString(bc.getCurVerifier(),true);
        logger.debug("下一轮结算周期验证人:{}",msg);
    }

    private void updateIsSetting(List<Node> nodes, CustomStaking.YesNoEnum isSetting){
        nodes.forEach(v->{
            String nodeId = HexTool.prefix(v.getNodeId());
            try {
                CustomNode node = nodeCache.getNode(nodeId);
                CustomStaking staking = node.getLatestStaking();
                staking.setIsSetting(isSetting.getCode());
                stakingStage.updateStaking(staking);
            } catch (NoSuchBeanException e) {
                logger.error("找不到[{}]对应的节点信息!",nodeId);
            }
        });
    }

    //结算周期变更导致的委托数据的变更
    private void updateDelegation () {
        //由于结算周期的变更，对所有的节点下的质押的委托更新
        //只需变更不为历史节点的委托数据(isHistory=NO(2))
        List<CustomDelegation> delegations = nodeCache.getDelegationByIsHistory(CustomDelegation.YesNoEnum.NO);
        delegations.forEach(delegation->{
            //经过结算周期的变更，上个周期的犹豫期金额累加到锁定期的金额
            delegation.setDelegateLocked(delegation.integerDelegateLocked().add(delegation.integerDelegateHas()).toString());
            //累加后的犹豫期金额至为0
            delegation.setDelegateHas("0");
            delegation.setDelegateReduction("0");
            //并判断经过一个结算周期后该委托的对应赎回是否全部完成，若完成则将委托设置为历史节点
            //判断条件委托的犹豫期金额 + 委托的锁定期金额 + 委托的赎回金额是否等于0
            BigInteger sumAmount = delegation.integerDelegateHas().add(delegation.integerDelegateLocked()).add(delegation.integerDelegateReduction());
            if (sumAmount.compareTo(BigInteger.ZERO) == 0) {
                delegation.setIsHistory(CustomDelegation.YesNoEnum.YES.getCode());
            }
            //添加需要更新的委托的信息到委托更新列表
            stakingStage.updateDelegation(delegation);
        });
    }

    //结算周期变更导致的委托赎回的变更
    private void updateUnDelegation() {
        //由于结算周期的变更，对所有的节点下的质押的委托的委托赎回更新
        //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
        List<CustomUnDelegation> unDelegations = nodeCache.getUnDelegationByStatus(CustomUnDelegation.StatusEnum.EXITING);
        unDelegations.forEach(unDelegation -> {
            //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
            unDelegation.setRedeemLocked("0");
            //当锁定期金额为零时，认为此笔赎回委托交易已经完成
            unDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.getCode());
            //添加需要更新的赎回委托信息到赎回委托更新列表
            stakingStage.updateUnDelegation(unDelegation);
        });
    }

    /**
     * 对上一结算周期的质押节点结算
     * 对所有候选中和退出中的节点进行结算
     */
    private void settleStaking() throws SettleEpochChangeException {
        if(bc.getPreVerifier().size()==0){
            throw new SettleEpochChangeException("上一结算周期取到的验证人列表为空，无法执行质押结算操作！");
        }
        if(bc.getCurVerifier().size()==0){
            throw new SettleEpochChangeException("下一结算周期取到的验证人列表为空，无法执行质押结算操作！");
        }
        BigInteger preVerifierStakingReward = new BigInteger(bc.getSettleReward().divide(BigDecimal.valueOf(bc.getPreVerifier().size()),0,RoundingMode.FLOOR).toString());
        logger.debug("上一结算周期验证人平均质押奖励:{}",preVerifierStakingReward);
        // 结算周期切换时对所有候选中和退出中状态的节点进行结算
        List<CustomStaking> stakingList = nodeCache.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE,CustomStaking.StatusEnum.EXITING);
        for(CustomStaking curStaking:stakingList){
            // 调整金额状态
            BigInteger stakingLocked = curStaking.integerStakingLocked().add(curStaking.integerStakingHas());
            curStaking.setStakingLocked(stakingLocked.toString()); // 把犹豫期金额挪到锁定字段
            curStaking.setStakingHas(BigInteger.ZERO.toString()); // 犹豫期金额置0
            // 当前结算周期轮数-减持质押时的结算轮数>=指定的质押退回所要经过的结算周期轮数，则质押退回成功，退回金额字段置0
            if((bc.getCurSettingEpoch().longValue() - curStaking.getStakingReductionEpoch()) >= chainConfig.getUnStakeRefundSettlePeriodCount().longValue()){
                curStaking.setStakingReduction("0");
            }
            // 犹豫期+锁定期+退回中==0, 则节点退出
            BigInteger stakingReduction = curStaking.integerStakingReduction();
            if(stakingLocked.add(stakingReduction).compareTo(BigInteger.ZERO)==0){
                curStaking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
            }
            // 年化率信息
            AnnualizedRateInfo ari = JSON.parseObject(curStaking.getAnnualizedRateInfo(),AnnualizedRateInfo.class);


            Node preNode = bc.getPreVerifier().get(curStaking.getNodeId());
            if(preNode!=null){
                // 当前质押节点在前一轮验证人中，则累加质押奖励
                BigInteger stakingRewardValue = curStaking.integerStakingRewardValue().add(preVerifierStakingReward);
                curStaking.setStakingRewardValue(stakingRewardValue.toString());
                try {
                    CustomNode customNode = nodeCache.getNode(curStaking.getNodeId());
                    // 更新节点的奖励累计字段
                    customNode.setStatRewardValue(curStaking.integerStakingRewardValue().add(curStaking.integerBlockRewardValue()).toString());
                    // 将改动的内存暂存至待更新缓存
                    stakingStage.updateNode(customNode);
                } catch (NoSuchBeanException e) {
                    throw new SettleEpochChangeException("获取节点错误:"+e.getMessage());
                }

                // 计算年化率
                if(curStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()){
                    // 只有候选中的记录才需要计算年化率：(((前4个结算周期内验证人所获得的平均质押奖励+前4结算周期出块奖励)/前四个结算周期质押成本)/1466)*100%
                    // 记录前一结算周期利润
                    rotateProfit(curStaking,ari);
                    // 计算最新年化率
                    calculateAnnualizedRate(curStaking,ari);
                }
            }

            // 如果在下一轮结算周期验证人中有当前节点，则轮换成本信息
            Node nextNode = bc.getCurVerifier().get(curStaking.getNodeId());
            if(nextNode!=null)  {
                rotateCost(curStaking,ari);
                // 是否下一轮结算验证人
                curStaking.setIsSetting(CustomStaking.YesNoEnum.YES.getCode());
            }else{
                // 不是下一轮结算验证人
                curStaking.setIsSetting(CustomStaking.YesNoEnum.NO.getCode());
            }
            // 更新年化率信息
            curStaking.setAnnualizedRateInfo(JSON.toJSONString(ari));
            // 将改动的内存暂存至待更新缓存
            stakingStage.updateStaking(curStaking);
        }
    }

    /**
     * 记录成本 轮换
     * @param curStaking
     */
    private void rotateCost(CustomStaking curStaking,AnnualizedRateInfo ari) throws SettleEpochChangeException {
        if(ari==null){
            throw new SettleEpochChangeException("年化率信息为空，无法计算!");
        }
        // 添加下一周期的质押成本
        BigInteger cost = curStaking.integerStakingLocked().add(curStaking.integerStakingHas());
        ari.getCost().add(new PeriodValueElement(bc.getCurSettingEpoch().add(BigInteger.ONE),cost));
        // 保留指定数量最新的记录
        if(ari.getCost().size()>chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1){
            // 按结算周期由大到小排序
            ari.getCost().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
            // 删除多余的元素
            for (int i=ari.getCost().size()-1;i>=chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1;i--) ari.getCost().remove(i);
        }
    }

    /**
     * 记录利润
     * @param curStaking
     */
    private void rotateProfit(CustomStaking curStaking,AnnualizedRateInfo ari) throws SettleEpochChangeException {
        if(ari==null){
            throw new SettleEpochChangeException("年化率信息为空，无法计算!");
        }
        // 如果年化率推算信息不为空，则证明当前质押信息已经连续了几个结算周期，做以下操作：
        // 添加上一周期的收益
        BigInteger profit = curStaking.integerStakingRewardValue().add(curStaking.integerBlockRewardValue());
        ari.getProfit().add(new PeriodValueElement(bc.getCurSettingEpoch(),profit));
        if(ari.getProfit().size()>chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()){
            // 按结算周期由大到小排序
            ari.getProfit().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
            // 删除多余的元素
            for (int i=ari.getProfit().size()-1;i>=chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue();i--) ari.getProfit().remove(i);
        }
    }

    private class AnnualizedSum{
        private BigInteger profitSum=BigInteger.ZERO;
        private BigInteger costSum=BigInteger.ZERO;
        private BigDecimal getAnnualizedRate(){
            if(costSum.compareTo(BigInteger.ZERO)==0) {
                return BigDecimal.ZERO;
            }
            BigDecimal rate = new BigDecimal(profitSum)
                    .divide(new BigDecimal(costSum),16,RoundingMode.FLOOR) // 除总成本
                    .multiply(new BigDecimal(chainConfig.getSettlePeriodCountPerIssue())) // 乘每个增发周期的结算周期数
                    .multiply(BigDecimal.valueOf(100));
            return rate.setScale(2,RoundingMode.FLOOR);
        }
    }
    /**
     * 计算年化率
     * @param curStaking
     */
    private void calculateAnnualizedRate(CustomStaking curStaking,AnnualizedRateInfo ari){
        // 年化率推算信息
        AnnualizedSum as = new AnnualizedSum();
        // 利润=最新的收益累计-最旧的收益收益累计
        if(ari.getProfit().isEmpty()) {
            as.profitSum=BigInteger.ZERO;
        } else {
            PeriodValueElement latest=ari.getProfit().get(0);
            PeriodValueElement oldest=latest;
            for (PeriodValueElement pve:ari.getProfit()){
                if(latest==null||latest.getPeriod().compareTo(pve.getPeriod())<0) latest=pve;
                if(oldest==null||latest.getPeriod().compareTo(pve.getPeriod())>0) oldest=pve;
            }
            if (latest==oldest) as.profitSum=latest.getValue();
            if (latest!=oldest) as.profitSum=latest.getValue().subtract(oldest.getValue());
        }

        // 按周期从小到大排序
        ari.getCost().sort((c1,c2)-> Integer.compare(c1.getPeriod().compareTo(c2.getPeriod()), 0));
        int count = 0;
        for (PeriodValueElement pve:ari.getCost()){
            if(count==4) break;
            as.costSum=as.costSum.add(pve.getValue());
            count++;
        }
        curStaking.setExpectedIncome(as.getAnnualizedRate().toString());
    }
}
