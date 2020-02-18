package com.platon.browser.complement.converter.delegate;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.bean.DelegateExitResult;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.DelegateBusinessMapper;
import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.complement.dao.param.delegate.DelegateExit;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationKey;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.entity.GasEstimateKey;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.CustomGasEstimateMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.GasEstimateMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DelegateExitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 撤销委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 *
 *
 **/
@Slf4j
@Service
public class DelegateExitConverter extends BusinessParamConverter<DelegateExitResult> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private DelegateBusinessMapper delegateBusinessMapper;
    @Autowired
    private DelegationMapper delegationMapper;
    @Autowired
    private AddressCache addressCache;
    @Autowired
    private CustomGasEstimateMapper customGasEstimateMapper;
    @Autowired
    private GasEstimateMapper gasEstimateMapper;
    @Autowired
    private NodeMapper nodeMapper;
	
    @Override
    public DelegateExitResult convert(CollectionEvent event, Transaction tx) {
        DelegateExitResult der = DelegateExitResult.builder().build();
        // 退出委托
        DelegateExitParam txParam = tx.getTxParam(DelegateExitParam.class);
        // 补充节点名称
        updateTxInfo(txParam,tx);
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return der;

        long startTime = System.currentTimeMillis();

        // 查询出撤销委托交易对应的委托信息
        DelegationKey delegationKey = new DelegationKey();
        delegationKey.setDelegateAddr(tx.getFrom());
        delegationKey.setNodeId(txParam.getNodeId());
        delegationKey.setStakingBlockNum(txParam.getStakingBlockNum().longValue());
        Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);

        if(delegation==null) return der;
        DelegateExit businessParam= DelegateExit.builder()
                .nodeId(txParam.getNodeId())
                .amount(txParam.getAmount())
                .blockNumber(BigInteger.valueOf(tx.getNum()))
                .txFrom(tx.getFrom())
                .stakingBlockNumber(txParam.getStakingBlockNum())
                .minimumThreshold(chainConfig.getDelegateThreshold())
                .codeRmDelegateHes(BigDecimal.ZERO)
                .codeRmDelegateLocked(BigDecimal.ZERO)
                .codeRmDelegateReleased(BigDecimal.ZERO)
                .codeDelegateHes(BigDecimal.ZERO)
                .codeDelegateLocked(BigDecimal.ZERO)
                .codeDelegateReleased(BigDecimal.ZERO)
                .delegateReward(txParam.getReward())
                .build();

        boolean isRefundAll = delegation.getDelegateHes() // 犹豫期金额
                .add(delegation.getDelegateLocked()) // +锁定期金额
                .add(delegation.getDelegateReleased()) // +待提取金额
                .subtract(txParam.getAmount()).compareTo(chainConfig.getDelegateThreshold())<0; // 小于委托门槛
        if(delegation.getDelegateReleased().compareTo(BigDecimal.ONE)>0){
            // 如果待提取金额大于0,则把节点置为已退出
            // 如果delegateReleased>0, 证明对应的质押已经退出，同时对应的delegateHes和delegateLocked字段的金额都会被挪到delegateReleased字段
            // 对应金额移动的逻辑请参考质押退出部分代码
            businessParam.setCodeNodeIsLeave(true);
        }

        boolean needDeleteGasEstimate = false;

        if(isRefundAll){
            needDeleteGasEstimate = true;
            // 如果全部退回
            businessParam.setCodeIsHistory(BusinessParam.YesNoEnum.YES.getCode()) // 委托状态置为历史
                .setCodeRealAmount(delegation.getDelegateHes().add(delegation.getDelegateLocked()).add(delegation.getDelegateReleased())) // 真实退回金额=犹豫+锁定+已解锁
                .setCodeDelegateHes(BigDecimal.ZERO) // 犹豫期金额置零
                .setCodeDelegateLocked(BigDecimal.ZERO) // 锁定期金额置零
                .setCodeDelegateReleased(BigDecimal.ZERO); // 已解锁金额置零
        }
        if(!isRefundAll){
            // 如果不是全部退回
            businessParam.setCodeIsHistory(BusinessParam.YesNoEnum.NO.getCode()) // 委托状态置为非历史
                    .setCodeRealAmount(txParam.getAmount()); // 真实退回金额=参数申请的金额
            if(businessParam.isCodeNodeIsLeave()){
                // 如果节点已经退出，则从此字段扣除真实扣除金额
                // 设置扣除当前申请金额后，剩余的委托待赎回金额
                businessParam.setCodeDelegateReleased(delegation.getDelegateReleased().subtract(businessParam.getCodeRealAmount()));
            }else if(delegation.getDelegateHes().compareTo(txParam.getAmount())>=0) {
                // 如果犹豫期金额大于申请的退出金额，则从犹豫期金额扣除申请金额，且锁定委托金额不变
                businessParam.setCodeDelegateHes(delegation.getDelegateHes().subtract(txParam.getAmount())) // 从犹豫期金额扣除申请金额
                    .setCodeDelegateLocked(delegation.getDelegateLocked()); // 锁定委托金额保持不变
            }else {
                // 如果犹豫期金额比申请金额小，则需要扣干净犹豫期金额，再从锁定金额中扣除
                businessParam.setCodeDelegateHes(BigDecimal.ZERO) // 犹豫期金额置0
                    .setCodeDelegateLocked(delegation.getDelegateLocked().add(delegation.getDelegateHes()).subtract(txParam.getAmount())); // 锁定金额+犹豫金额-申请金额
            }
        }

        // 计算数据库中需要减除的金额 = 数据库空的金额-程序计算后应该剩余的金额
        businessParam.setCodeRmDelegateHes(delegation.getDelegateHes().subtract(businessParam.getCodeDelegateHes()))
                .setCodeRmDelegateLocked(delegation.getDelegateLocked().subtract(businessParam.getCodeDelegateLocked()));
        Node node = nodeMapper.selectByPrimaryKey(txParam.getNodeId());
        if (txParam.getStakingBlockNum().compareTo(BigInteger.valueOf(node.getStakingBlockNum())) != 0
        		|| StatusEnum.CANDIDATE.getCode() != node.getStatus().intValue()) {
        	// 只有不是同一个质押区块的节点的委托下或者状态不为候选中的时候，节点、质押中的待赎回委托需要扣减的金额：真实扣除金额
        	businessParam.setCodeRmDelegateReleased(businessParam.getCodeRealAmount());
		}

        // 补充真实退款金额
        txParam.setRealAmount(businessParam.getCodeRealAmount());
        tx.setInfo(txParam.toJSONString());

        delegateBusinessMapper.exit(businessParam);

        der.setDelegateExit(businessParam);

        if(txParam.getReward().compareTo(BigDecimal.ZERO)>0){
            // 如果委托奖励为0，则无需记录领取记录
            DelegationReward delegationReward = new DelegationReward();
            delegationReward.setHash(tx.getHash());
            delegationReward.setAddr(tx.getFrom());
            delegationReward.setTime(tx.getTime());
            delegationReward.setCreTime(new Date());
            delegationReward.setUpdTime(new Date());

            List<DelegationReward.Extra> extraList = new ArrayList<>();
            DelegationReward.Extra extra = new DelegationReward.Extra();
            extra.setNodeId(businessParam.getNodeId());
            String nodeName = "Unknown";
            try {
                nodeName = nodeCache.getNode(businessParam.getNodeId()).getNodeName();
            } catch (NoSuchBeanException e) {
                log.error("{}",e.getMessage());
            }
            extra.setNodeName(nodeName);
            extra.setReward(txParam.getReward().toString());
            extraList.add(extra);
            delegationReward.setExtra(JSON.toJSONString(extraList));

            der.setDelegationReward(delegationReward);
        }

        addressCache.update(businessParam);

        if(needDeleteGasEstimate){
            // 1. 全部赎回： 删除对应记录
            GasEstimateKey gek = new GasEstimateKey();
            gek.setNodeId(txParam.getNodeId());
            gek.setAddr(tx.getFrom());
            gek.setSbn(txParam.getStakingBlockNum().longValue());
            gasEstimateMapper.deleteByPrimaryKey(gek);
        }else{
            // 2. 部分赎回：1. 新增 委托未计算周期  记录， epoch = 0
            List<GasEstimate> estimates = new ArrayList<>();
            GasEstimate estimate = new GasEstimate();
            estimate.setNodeId(txParam.getNodeId());
            estimate.setSbn(txParam.getStakingBlockNum().longValue());
            estimate.setAddr(tx.getFrom());
            estimate.setEpoch(0L);
            estimates.add(estimate);
            customGasEstimateMapper.batchInsertOrUpdateSelective(estimates, GasEstimate.Column.values());
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        return der;
    }
}
