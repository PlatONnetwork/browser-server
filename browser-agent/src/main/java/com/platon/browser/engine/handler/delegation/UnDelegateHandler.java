package com.platon.browser.engine.handler.delegation;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
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

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 减持/撤销委托(赎回委托)事件处理类
 */
@Component
public class UnDelegateHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(UnDelegateHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Override
    public void handle(EventContext context) throws NoSuchBeanException{
        CustomTransaction tx = context.getTransaction();
        StakingStage stakingStage = context.getStakingStage();

        UnDelegateParam param = tx.getTxParam(UnDelegateParam.class);
        try {
            CustomNode node = NODE_CACHE.getNode(param.getNodeId());
            logger.debug("减持/撤销委托(赎回委托):{}", JSON.toJSONString(param));

            //根据委托赎回参数blockNumber找到对应当时委托的质押信息
            CustomStaking customStaking = node.getStakings().get(Long.valueOf(param.getStakingBlockNum()));
            //交易数据回填
            param.setNodeName(customStaking.getStakingName());
            tx.setTxInfo(JSON.toJSONString(param));

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

            BigDecimal delegationSum = new BigDecimal(customDelegation.getDelegateHas()).add(new BigDecimal(customDelegation.getDelegateLocked()));
            if (delegationSum.compareTo(bc.getChainConfig().getDelegateThreshold()) == -1) {
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
            } else {
                customUnDelegation.setStatus(CustomUnDelegation.StatusEnum.EXITING.code);
            }

            // 添加至解委托缓存
            NODE_CACHE.addUnDelegation(customUnDelegation);

            //更新分析委托结果
            stakingStage.updateDelegation(customDelegation);
            //新增分析委托赎回结果
            stakingStage.insertUnDelegation(customUnDelegation);

        } catch (NoSuchBeanException e) {
            logger.error("{}", e.getMessage());
            throw  new NoSuchBeanException("缓存中找不到对应的接触质押信息:");
        }
    }
}
