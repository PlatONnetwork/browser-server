package com.platon.browser.engine.handler.epoch;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.bean.AnnualizedRateInfo;
import com.platon.browser.engine.bean.PeriodValueElement;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.exception.SettleEpochChangeException;
import com.platon.browser.utils.HexTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static java.lang.String.format;

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
    private PlatonClient client;
    private StakingStage stakingStage;

    @Override
    public void handle(EventContext context) throws Exception {
        stakingStage = context.getStakingStage();
        updateVerifier(); // 更新缓存中的辅助结算周期验证人信息
        settle(); // 结算
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
    private void updateVerifier () throws CandidateException {
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        BaseResponse<List <Node>> result;

        // ==================================更新前一周期验证人列表=======================================

        // 入参区块号属于前一结算周期，因此可以通过它查询前一结算周期验证人历史列表
        BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
        try {
            result = SpecialContractApi.getHistoryVerifierList(client.getWeb3j(),prevEpochLastBlockNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询前轮结算验证人-底层出错】查询块号在【%s】的结算周期验证人历史出错:%s]",prevEpochLastBlockNumber,e.getMessage()));
        }
        if (!result.isStatusOk()) {
            throw new CandidateException(format("【查询前轮结算验证人-底层出错】查询块号在【%s】的结算周期验证人历史出错:%s]",prevEpochLastBlockNumber,result.errMsg));
        }else{
            bc.getPreVerifier().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getPreVerifier().put(HexTool.prefix(node.getNodeId()), node));
            logger.debug("前一轮结算周期(最后块号{})验证人(查{}):{}",blockNumber,blockNumber,JSON.toJSONString(bc.getCurValidator(),true));
        }


        // ==================================更新当前周期验证人列表=======================================
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
        try {
            result = SpecialContractApi.getHistoryVerifierList(client.getWeb3j(),nextEpochFirstBlockNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询当前结算验证人-底层出错】查询块号在【%s】的结算周期验证人历史出错:%s]",nextEpochFirstBlockNumber,nextEpochFirstBlockNumber,e.getMessage()));
        }
        if(result.isStatusOk()){
            bc.getCurVerifier().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurVerifier().put(HexTool.prefix(node.getNodeId()), node));
            logger.debug("当前轮结算周期验证人(查{}):{}",nextEpochFirstBlockNumber,JSON.toJSONString(bc.getCurValidator(),true));
        }
        if (!result.isStatusOk()) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                result = client.getNodeContract().getVerifierList().send();
                bc.getCurVerifier().clear();
                result.data.stream().filter(Objects::nonNull).forEach(node -> bc.getCurVerifier().put(HexTool.prefix(node.getNodeId()), node));
                logger.debug("当前轮结算周期验证人(实时):{}",JSON.toJSONString(bc.getCurValidator(),true));
            } catch (Exception e) {
                throw new CandidateException(format("【查询当前结算验证人-底层出错】查询实时结算周期验证人出错:%s",e.getMessage()));
            }
            if(!result.isStatusOk()){
                throw new CandidateException(format("【查询当前结算验证人-底层出错】查询实时结算周期验证人出错:%s",result.errMsg));
            }
        }

        if(bc.getCurVerifier().size()==0){
            throw new CandidateException("查询不到结算周期验证人(当前块号="+blockNumber+",当前结算轮数="+bc.getCurSettingEpoch()+")");
        }


        logger.debug("当前轮结算周期验证人:{}",JSON.toJSONString(bc.getCurVerifier(),true));
    }


    //结算周期变更导致的委托数据的变更
    private void updateDelegation () {
        //由于结算周期的变更，对所有的节点下的质押的委托更新
        //只需变更不为历史节点的委托数据(isHistory=NO(2))
        List<CustomDelegation> delegations = NODE_CACHE.getDelegationByIsHistory(CustomDelegation.YesNoEnum.NO);
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
                delegation.setIsHistory(CustomDelegation.YesNoEnum.YES.code);
            }
            //添加需要更新的委托的信息到委托更新列表
            stakingStage.updateDelegation(delegation);
        });
    }

    //结算周期变更导致的委托赎回的变更
    private void updateUnDelegation() {
        //由于结算周期的变更，对所有的节点下的质押的委托的委托赎回更新
        //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
        List<CustomUnDelegation> unDelegations = NODE_CACHE.getUnDelegationByStatus(CustomUnDelegation.StatusEnum.EXITING);
        unDelegations.forEach(unDelegation -> {
            //更新赎回委托的锁定中的金额：赎回锁定金额，在一个结算周期后到账，修改锁定期金额
            unDelegation.setRedeemLocked("0");
            //当锁定期金额为零时，认为此笔赎回委托交易已经完成
            unDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.code);
            //添加需要更新的赎回委托信息到赎回委托更新列表
            stakingStage.updateUnDelegation(unDelegation);
        });
    }

    /**
     * 对上一结算周期的质押节点结算
     * 对所有候选中和退出中的节点进行结算
     */
    private void settle() throws SettleEpochChangeException, CandidateException {

        // 结算周期切换时对所有候选中和退出中状态的节点进行结算

        // 前一结算周期内每个验证人所获得的平均质押奖励
        // 计算结算周期每个验证人所获得的平均质押奖励：((前一增发周期末激励池账户余额/(每个增发周期内的结算周期数))/上一结算周期验证人数)*质押激励比例
        if(bc.getPreVerifier().size()==0){
            throw new SettleEpochChangeException("上一结算周期取到的验证人列表为空，无法执行质押结算操作！");
        }
        BigInteger preVerifierStakingReward = new BigInteger(bc.getSettleReward().divide(BigDecimal.valueOf(bc.getCurVerifier().size()),0,RoundingMode.FLOOR).toString());
        logger.debug("上一结算周期验证人平均质押奖励:{}",preVerifierStakingReward.longValue());

        List<CustomStaking> stakingList = NODE_CACHE.getStakingByStatus(CustomStaking.StatusEnum.CANDIDATE,CustomStaking.StatusEnum.EXITING);
        for(CustomStaking curStaking:stakingList){
            // 调整金额状态
            BigInteger stakingLocked = curStaking.integerStakingLocked().add(curStaking.integerStakingHas());
            curStaking.setStakingLocked(stakingLocked.toString());
            curStaking.setStakingHas(BigInteger.ZERO.toString());
            // 当前结算周期轮数-减持质押时的结算轮数>=指定的质押退回所要经过的结算周期轮数
            if((bc.getCurSettingEpoch().longValue() - curStaking.getStakingReductionEpoch()) >= chainConfig.getUnstakeRefundSettlePeriodCount().longValue()){
                curStaking.setStakingReduction("0");
            }
            // 犹豫期+锁定期+退回中==0
            BigInteger stakingReduction = curStaking.integerStakingReduction();
            if(stakingLocked.add(stakingReduction).compareTo(BigInteger.ZERO)==0){
                curStaking.setStatus(CustomStaking.StatusEnum.EXITED.code);
            }


            // 计算质押激励和年化率
            Node node = bc.getPreVerifier().get(curStaking.getNodeId());
            if(node!=null){
                // 质押记录所属节点在前一轮结算周期的验证人列表中，则对其执行结算操作
                // 累加质押奖励
                BigInteger stakingRewardValue = curStaking.integerStakingRewardValue().add(preVerifierStakingReward);
                curStaking.setStakingRewardValue(stakingRewardValue.toString());

                CustomNode customNode;
                try {
                    customNode = NODE_CACHE.getNode(curStaking.getNodeId());
                } catch (NoSuchBeanException e) {
                    throw new SettleEpochChangeException("获取节点错误:"+e.getMessage());
                }

                if(curStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.code){
                    // 只有候选中的记录才需要计算年化率：(((前4个结算周期内验证人所获得的平均质押奖励+前4结算周期出块奖励)/前四个结算周期质押成本)/1466)*100%

                    /**
                     * 业务处理逻辑：
                     * 每4个结算周期计算一次年化率
                     * 收益:     W0    W1    W2    W3    W4
                     *          |-----|-----|-----|-----|
                     * 结算周期： S0    S1    S2    S3    S4
                     * 成本:     C1    C2    C3    C4    C5
                     *
                     * 年化率计算：
                     * W1 + W2 + W3 + W4
                     * ------------------ x 一个增发周期内的结算周期总数 x 100%
                     * C1 + C2 + C3 + C4
                     */
                    // 年化率推算信息
                    String annualizedRateInfo = curStaking.getAnnualizedRateInfo();
                    AnnualizedRateInfo ari;
                    if(StringUtils.isBlank(annualizedRateInfo)){
                        // 如果年化率推算信息为空，则证明是新质押，只需要把成本记录下来即可
                        ari = new AnnualizedRateInfo();
                        // 下一周期的质押成本：锁定+犹豫
                        BigInteger cost = curStaking.integerStakingLocked().add(curStaking.integerStakingHas());
                        ari.getCost().add(new PeriodValueElement(bc.getCurSettingEpoch().add(BigInteger.ONE),cost));
                    }else{
                        ari = JSON.parseObject(annualizedRateInfo,AnnualizedRateInfo.class);
                        // 如果年化率推算信息不为空，则证明当前质押信息已经连续了几个结算周期，做以下操作：
                        // 1、添加上一周期的收益
                        BigInteger profit = curStaking.integerStakingRewardValue().add(curStaking.integerBlockRewardValue());
                        ari.getProfit().add(new PeriodValueElement(bc.getCurSettingEpoch(),profit));
                        // 2、添加下一周期的质押成本
                        BigInteger cost = curStaking.integerStakingLocked().add(curStaking.integerStakingHas());
                        ari.getCost().add(new PeriodValueElement(bc.getCurSettingEpoch().add(BigInteger.ONE),cost));
                        // 保留指定数量最新的记录
                        if(ari.getProfit().size()>bc.getChainConfig().getMaxSettlePeriodCount4AnnualizedRateStat().longValue()){
                            // 按结算周期由大到小排序
                            ari.getProfit().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
                            // 删除多余的元素
                            for (int i=ari.getProfit().size()-1;i>=bc.getChainConfig().getMaxSettlePeriodCount4AnnualizedRateStat().longValue();i--) ari.getProfit().remove(i);
                        }

                        // 保留指定数量最新的记录
                        if(ari.getCost().size()>bc.getChainConfig().getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1){
                            // 按结算周期由大到小排序
                            ari.getCost().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
                            // 删除多余的元素
                            for (int i=ari.getCost().size()-1;i>=bc.getChainConfig().getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1;i--) ari.getCost().remove(i);
                        }
                    }
                    class AnnualizedSum{
                        private BigInteger profitSum=BigInteger.ZERO,costSum=BigInteger.ZERO;
                        private BigDecimal getAnnualizedRate(){
                            if(costSum.compareTo(BigInteger.ZERO)==0) return BigDecimal.ZERO;
                            BigDecimal rate = new BigDecimal(profitSum)
                                    .divide(new BigDecimal(costSum),16,RoundingMode.FLOOR)
                                    .multiply(new BigDecimal(bc.getSettleEpochCountPerIssueEpoch()))
                                    .multiply(BigDecimal.valueOf(100));
                            return rate.setScale(2,RoundingMode.FLOOR);
                        }
                    }
                    AnnualizedSum as = new AnnualizedSum();
                    ari.getProfit().forEach(ele->as.profitSum=as.profitSum.add(ele.getValue()));

                    // 按周期从大到小排序
                    ari.getCost().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
                    // 从索引1开始，忽略最大的周期
                    for (int i=1;i<ari.getCost().size();i++){
                        PeriodValueElement pve = ari.getCost().get(i);
                        as.costSum=as.costSum.add(pve.getValue());
                    }
                    curStaking.setExpectedIncome(as.getAnnualizedRate().toString());
                    curStaking.setAnnualizedRateInfo(JSON.toJSONString(ari));
                }

                // 结算状态设置为已结算
                curStaking.setIsSetting(CustomStaking.YesNoEnum.YES.code);
                // 更新节点的质押金累计字段
                customNode.setStatRewardValue(curStaking.getStakingRewardValue());
                // 将改动的内存暂存至待更新缓存
                stakingStage.updateNode(customNode);
            }else{
                // 年化率设置为0
                curStaking.setExpectedIncome(BigInteger.ZERO.toString());
                // 结算状态设置为未结算
                curStaking.setIsSetting(CustomStaking.YesNoEnum.NO.code);
            }
            // 将改动的内存暂存至待更新缓存
            stakingStage.updateStaking(curStaking);
        }
    }
}
