package com.platon.browser.engine.handler.delegation;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.UnDelegateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 减持/撤销委托(赎回委托)事件处理类
 */
@Component
public class UnDelegateHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(UnDelegateHandler.class);
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private CacheHolder cacheHolder;
    @Override
    public void handle(EventContext context) throws NoSuchBeanException{
        NodeCache nodeCache = cacheHolder.getNodeCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();

        CustomTransaction tx = context.getTransaction();
        UnDelegateParam param = tx.getTxParam(UnDelegateParam.class);
        try {
            // 通过解委托参数中的nodeId获取节点信息
            CustomNode node = nodeCache.getNode(param.getNodeId());
            String msg = JSON.toJSONString(param);
            logger.debug("减持/撤销委托(赎回委托):{}", msg);

            // 通过解委托参数中的stakingBlockNum获取节点质押信息
            CustomStaking customStaking = node.getStakings().get(param.getStakingBlockNum());

            // 通过解委托交易的from地址获对应的委托信息
            CustomDelegation delegation = customStaking.getDelegations().get(tx.getFrom());

            // 创建解委托记录
            CustomUnDelegation unDelegation = new CustomUnDelegation();
            unDelegation.updateWithUnDelegateParam(param, tx);
            /*
             *  1.获取到对应的委托信息
             *  2.根据委托信息，判断，余额
             *  3.判断是否是全部退出
             *   a.yes
             *       委托的犹豫期金额 + 锁定期金额 - 赎回委托的金额 < 最小委托金额，则全部退出
             *   b.no
             *       b1.若委托犹豫期金额 >= 本次赎回委托的金额，则直接扣去相应的金额
             *       b2.若委托犹豫期金额 < 本次赎回委托的金额，优先扣除犹豫期所剩的金额
             * */

            // 当前委托总额=犹豫期金额+锁定期金额
            BigInteger delegationSum = delegation.integerDelegateHas().add(delegation.integerDelegateLocked());
            // 委托门槛（配置文件中委托门槛单位是LAT）
            BigDecimal delegateThresholdVon = Convert.toVon(chainConfig.getDelegateThreshold(), Convert.Unit.LAT);
            if (delegationSum.subtract(param.integerAmount()).compareTo(new BigInteger(delegateThresholdVon.toString())) < 0) {
                // **************** 如果（当前委托总额-解委托中指定的金额）<委托门槛， 则此委托不再符合委托条件，做如下处理：****************
                // 1、犹豫期金额全部退回
                delegation.setDelegateHas("0");
                // 2、锁定期金额全部退回
                delegation.setDelegateLocked("0");
                // 3、赎回中金额置零（当前实现是实时退回，此字段相当于作废了）
                delegation.setDelegateReduction("0");
                // 4、解委托记录中的赎回被锁定的金额置零（当前实现是实时退回，此字段相当于作废了）
                unDelegation.setRedeemLocked("0");
                // 5、设置解委托真实的退回金额
                unDelegation.setRealAmount(delegationSum.toString());
            } else {
                // **************** 如果（当前委托总额-解委托中指定的金额）>=委托门槛， 则此委托依然符合委托条件，做如下处理：****************
                if (delegation.integerDelegateHas().compareTo(param.integerAmount()) > 0) {
                    //犹豫期金额>赎回金额，则从犹豫期金额中扣除
                    delegation.setDelegateHas(delegation.integerDelegateHas().subtract(param.integerAmount()).toString());
                    // 4、解委托记录中的赎回被锁定的金额置零（当前实现是实时退回，此字段相当于作废了）
                    unDelegation.setRedeemLocked("0");
                } else {
                    //犹豫期金额<赎回金额，优先扣除所有犹豫期金额，不足的从锁定期金额中扣除
                    //差值 = 赎回金额 - 犹豫期金额
                    BigInteger subHas = param.integerAmount().subtract(delegation.integerDelegateHas());
                    //犹豫期设置为零
                    delegation.setDelegateHas("0");
                    //解委托后锁定期金额= 解委托前锁定期 - 差值
                    delegation.setDelegateLocked(delegation.integerDelegateLocked().subtract(subHas).toString());
                    //解委托记录中的赎回被锁定的金额置零（当前实现是实时退回，此字段相当于作废了）
                    unDelegation.setRedeemLocked("0");
                }
                // 设置解委托真实的退回金额
                unDelegation.setRealAmount(param.getAmount());
            }
            // 如果（委托犹豫期金额+委托锁定期金额+委托赎回金额）==0，则把委托状态设置为历史
            BigInteger sumAmount = delegation.integerDelegateHas() // 委托犹豫期金额
                    .add(delegation.integerDelegateLocked()) // +委托锁定期金额
                    .add(delegation.integerDelegateReduction());
            if (sumAmount.compareTo(BigInteger.ZERO)==0) {
                delegation.setIsHistory(CustomDelegation.YesNoEnum.YES.getCode());
            }
            // 解委托实时生效
            unDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITED.getCode());

            //交易数据回填
            param.setNodeName(customStaking.getStakingName());
            tx.setTxInfo(JSON.toJSONString(param));

            // 添加至解委托缓存
            nodeCache.addUnDelegation(unDelegation);
            // 将新的解委托记录放入待入库暂存区
            stakingStage.insertUnDelegation(unDelegation);
            // 将委托记录放入待更新暂存区
            stakingStage.updateDelegation(delegation);
        } catch (NoSuchBeanException e) {
            logger.error("{}", e.getMessage());
            throw  new NoSuchBeanException("缓存中找不到对应的解除质押信息:");
        }
    }
}
