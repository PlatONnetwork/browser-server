package com.platon.browser.analyzer.ppos;

import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.dao.param.ppos.StakeModify;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 修改验证人业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeModifyAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private StakeBusinessMapper stakeBusinessMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private StakingMapper stakingMapper;

    /**
     * 修改质押信息(编辑验证人)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) throws NoSuchBeanException {
        // 修改质押信息
        StakeModifyParam txParam = tx.getTxParam(StakeModifyParam.class);

        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus())
            return null;

        long startTime = System.currentTimeMillis();

        StakeModify businessParam = StakeModify.builder()
                .nodeId(txParam.getNodeId())
                .nodeName(txParam.getNodeName())
                .externalId(txParam.getExternalId())
                .benefitAddr(txParam.getBenefitAddress())
                .webSite(txParam.getWebsite())
                .details(txParam.getDetails())
                .isInit(isInit(txParam.getBenefitAddress()))
                .stakingBlockNum(nodeCache.getNode(txParam.getNodeId()).getStakingBlockNum())
                .nextRewardPer(txParam.getDelegateRewardPer())
                .settleEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .build();


        StakingKey stakingKey = new StakingKey();
        stakingKey.setNodeId(txParam.getNodeId());
        stakingKey.setStakingBlockNum(businessParam.getStakingBlockNum().longValue());
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        String preDelegateRewardRate = "0";
        if (staking != null)
            preDelegateRewardRate = staking.getRewardPer().toString();
        /**
         * 如果为激励池合约则不修改收益地址
         */
        if (staking != null && InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress().equals(staking.getBenefitAddr())) {
            businessParam.setBenefitAddr(InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress());
        }

        stakeBusinessMapper.modify(businessParam);
        // 更新节点缓存
        updateNodeCache(HexUtil.prefix(txParam.getNodeId()), txParam.getNodeName());


        String desc = "";
        /**
         * 参数有值且与初始不相等的情况下设置desc
         */
        if (txParam.getDelegateRewardPer() != null && !String.valueOf(businessParam.getNextRewardPer()).equals(preDelegateRewardRate)) {
            desc = NodeOpt.TypeEnum.MODIFY.getTpl()
                    .replace("BEFORERATE", preDelegateRewardRate)
                    .replace("AFTERRATE", String.valueOf(businessParam.getNextRewardPer()));
        }

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.MODIFY.getCode()));
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(tx.getNum());
        nodeOpt.setTime(tx.getTime());
        nodeOpt.setDesc(desc);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return nodeOpt;
    }

}
