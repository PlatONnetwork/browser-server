package com.platon.browser.engine.handler.staking;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.BlockChainException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateValidatorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.NODE_NAME_MAP;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description: 发起质押(创建验证人)事件处理类
 */
@Component
public class CreateValidatorHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(CreateValidatorHandler.class);
    @Autowired
    private BlockChain bc;
    @Override
    public void handle(EventContext context) throws BlockChainException {
        CustomTransaction tx = context.getTransaction();
        StakingStage stakingStage = context.getStakingStage();
        // 获取交易入参
        CreateValidatorParam param = tx.getTxParam(CreateValidatorParam.class);
        logger.debug("发起质押(创建验证人):{}", JSON.toJSONString(param));
        try {
            CustomNode node = NODE_CACHE.getNode(param.getNodeId());
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
                    stakingStage.insertStaking(newStaking,tx);

                    // 更新节点名称映射缓存
                    NODE_NAME_MAP.put(newStaking.getNodeId(),newStaking.getStakingName());
                }
                if(latestStaking.getStatus()== CustomStaking.StatusEnum.CANDIDATE.code){
                    // 如果最新质押状态为选中，且另有新的创建质押请求，则证明链上出错
                    logger.error("[DuplicateStakingError]链上重复质押同一节点(nodeId={})",node.getNodeId());
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
            node.setStatExpectBlockQty(bc.getChainConfig().getExpectBlockCount().longValue());
            node.updateWithCustomStaking(staking);
            // 把质押记录添加到节点质押记录列表中
            node.getStakings().put(staking.getStakingBlockNum(),staking);
            // 节点添加到缓存中
            NODE_CACHE.addNode(node);
            // 新节点和新质押记录暂存到待入库列表中
            stakingStage.insertNode(node);
            stakingStage.insertStaking(staking,tx);

            // 更新节点名称映射缓存
            NODE_NAME_MAP.put(staking.getNodeId(),staking.getStakingName());
        }
    }
}
