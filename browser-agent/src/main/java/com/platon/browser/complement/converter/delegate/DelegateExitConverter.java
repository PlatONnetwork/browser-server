package com.platon.browser.complement.converter.delegate;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationKey;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.DelegateExitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class DelegateExitConverter extends BusinessParamConverter<DelegateExit> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
    @Autowired
    private DelegationMapper delegationMapper;
	
    @Override
    public DelegateExit convert(CollectionEvent event, Transaction tx) {
        long startTime = System.currentTimeMillis();

    	DelegateExitParam txParam = tx.getTxParam(DelegateExitParam.class);

        // 查询出撤销委托交易对应的委托信息
        DelegationKey delegationKey = new DelegationKey();
        delegationKey.setDelegateAddr(tx.getFrom());
        delegationKey.setNodeId(txParam.getNodeId());
        delegationKey.setStakingBlockNum(txParam.getStakingBlockNum().longValue());
        Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);

        if(delegation==null) throw new Error("找不到对应的委托信息:[delegateAddr="+tx.getFrom()+",nodeId="+txParam.getNodeId()+",stakingBlockNum="+txParam.getStakingBlockNum()+"]");

        boolean isRefundAll = delegation.getDelegateHes()
                .add(delegation.getDelegateLocked())
                .add(delegation.getDelegateReleased())
                .subtract(txParam.getAmount()).compareTo(chainConfig.getDelegateThreshold())<0;

        /**
         * -- 2.程序计算逻辑
         * set code_delegate_has;        --当前犹豫金额
         * set code_rm_delegate_has;     --扣减犹豫金额
         * set code_delegate_locked;     --当前锁定金额
         * set code_rm_delegate_locked;  --扣减锁定金额
         * set code_delegate_released;   --当前待赎回金额
         * set code_rm_delegate_released;--扣减待赎回金额
         * set code_is_history;          --当前是否为历史
         * set code_real_amount;   	  --真正退款金额
         * set code_node_is_leave=false; --节点是否退出
         *
         * isRefundAll = delegate_has + delegate_locked + delegate_released - @amount < @MinimumThreshold
         * if(delegate_released > 0 ){
         * 	code_node_is_leave = true;
         * }
         * if (isRefundAll = true){
         * 	code_is_history = 1
         * 	code_real_amount = delegate_has + delegate_locked + delegate_released;
         * 	code_delegate_has = 0;
         * 	code_delegate_locked = 0;
         * 	code_delegate_released = 0;
         * }else {
         * 	code_is_history = 2;
         * 	code_real_amount = @amount;
         * 	if(delegate_released > 0){
         * 		code_delegate_released = delegate_released - @amount;
         *        }else if(delegate_has >=  @amount){
         * 		code_delegate_has = delegate_has - @amount;
         * 		code_delegate_locked = delegate_locked;
         *    }else{
         * 	 	code_delegate_has = 0;
         * 	  	code_delegate_locked = delegate_locked + delegate_has - @amount;
         *    }
         * }
         * code_rm_delegate_has = delegate_has - code_delegate_has;
         * code_rm_delegate_locked = delegate_locked - code_delegate_locked;
         * code_rm_delegate_released = delegate_released - code_delegate_released;
         */



        DelegateExit businessParam= DelegateExit.builder()
        		.nodeId(txParam.getNodeId())
        		.amount(txParam.getAmount())
        		.blockNumber(BigInteger.valueOf(tx.getNum()))
        		.txFrom(tx.getFrom())
        		.stakingBlockNumber(txParam.getStakingBlockNum())
        		.minimumThreshold(chainConfig.getDelegateThreshold())
                .build();
        
        delegateBusinessMapper.exit(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
