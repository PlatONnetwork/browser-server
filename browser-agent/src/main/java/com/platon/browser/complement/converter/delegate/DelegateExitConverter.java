package com.platon.browser.complement.converter.delegate;

import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.complement.error.BusinessError;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationKey;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DelegateExitParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 撤销委托业务参数转换器
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
        // 退出委托
        DelegateExitParam txParam = tx.getTxParam(DelegateExitParam.class);
        // 补充节点名称
        String nodeId=txParam.getNodeId();
        try {
            NodeItem nodeItem = nodeCache.getNode(nodeId);
            txParam.setNodeName(nodeItem.getNodeName());
            txParam.setStakingBlockNum(nodeItem.getStakingBlockNum());
            tx.setInfo(txParam.toJSONString());
        } catch (NoSuchBeanException e) {
            log.warn("缓存中找不到节点[{}]信息,无法补节点名称和质押区块号",nodeId);
        }
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return null;

        long startTime = System.currentTimeMillis();


        // 查询出撤销委托交易对应的委托信息
        DelegationKey delegationKey = new DelegationKey();
        delegationKey.setDelegateAddr(tx.getFrom());
        delegationKey.setNodeId(txParam.getNodeId());
        delegationKey.setStakingBlockNum(txParam.getStakingBlockNum().longValue());
        Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);

        if(delegation==null) throw new BusinessError("找不到对应的委托信息:[delegateAddr="+tx.getFrom()+",nodeId="+txParam.getNodeId()+",stakingBlockNum="+txParam.getStakingBlockNum()+"]");
        DelegateExit businessParam= DelegateExit.builder()
                .nodeId(txParam.getNodeId())
                .amount(txParam.getAmount())
                .blockNumber(BigInteger.valueOf(tx.getNum()))
                .txFrom(tx.getFrom())
                .stakingBlockNumber(txParam.getStakingBlockNum())
                .minimumThreshold(chainConfig.getDelegateThreshold())
                .build();

        boolean isRefundAll = delegation.getDelegateHes()
                .add(delegation.getDelegateLocked())
                .add(delegation.getDelegateReleased())
                .subtract(txParam.getAmount()).compareTo(chainConfig.getDelegateThreshold())<0;
        if(delegation.getDelegateReleased().compareTo(BigDecimal.ONE)>0){
            // 如果待提取金额大于0,则把节点置为已退出
            businessParam.setCodeNodeIsLeave(true);
        }
        if(isRefundAll){
            // 如果全部退回
            businessParam.setCodeIsHistory(BusinessParam.YesNoEnum.YES.getCode())
                .setCodeRealAmount(delegation.getDelegateHes().add(delegation.getDelegateLocked()).add(delegation.getDelegateReleased()))
                .setCodeDelegateHes(BigDecimal.ZERO)
                .setCodeDelegateLocked(BigDecimal.ZERO)
                .setCodeDelegateReleased(BigDecimal.ZERO);
        }else{
            // 如果不是全部退回
            businessParam.setCodeIsHistory(BusinessParam.YesNoEnum.NO.getCode())
                .setCodeRealAmount(txParam.getAmount());
            if(delegation.getDelegateReleased().compareTo(BigDecimal.ZERO)>0){
                businessParam.setCodeDelegateReleased(delegation.getDelegateReleased().subtract(txParam.getAmount()));
            }else if(delegation.getDelegateHes().compareTo(txParam.getAmount())>=0) {
                businessParam.setCodeDelegateHes(delegation.getDelegateHes().subtract(txParam.getAmount()))
                    .setCodeDelegateLocked(delegation.getDelegateLocked());
            }else {
                businessParam.setCodeDelegateHes(BigDecimal.ZERO)
                    .setCodeDelegateLocked(delegation.getDelegateLocked().add(delegation.getDelegateHes()).subtract(txParam.getAmount()));
            }
        }

        businessParam.setCodeRmdelegateHes(delegation.getDelegateHes().subtract(businessParam.getCodeDelegateHes()))
                .setCodeRmDelegateLocked(delegation.getDelegateLocked().subtract(businessParam.getCodeDelegateLocked()))
                .setCodeRmDelegateReleased(delegation.getDelegateReleased().subtract(businessParam.getCodeDelegateReleased()));

        // 补充真实退款金额
        txParam.setRealAmount(businessParam.getCodeRealAmount());
        tx.setInfo(txParam.toJSONString());

        delegateBusinessMapper.exit(businessParam);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return businessParam;
    }
}
