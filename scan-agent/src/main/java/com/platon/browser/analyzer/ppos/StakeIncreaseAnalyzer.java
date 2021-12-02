package com.platon.browser.analyzer.ppos;

import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.dao.param.ppos.StakeIncrease;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.StakeIncreaseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 增持质押业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeIncreaseAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private StakeBusinessMapper stakeBusinessMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    /**
     * 增持质押(增加自有质押)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        // 增持质押
        StakeIncreaseParam txParam = tx.getTxParam(StakeIncreaseParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus())
            return null;

        long startTime = System.currentTimeMillis();


        StakeIncrease businessParam = StakeIncrease.builder()
                .nodeId(txParam.getNodeId())
                .amount(txParam.getAmount())
                .stakingBlockNum(txParam.getStakingBlockNum())
                .build();

        stakeBusinessMapper.increase(businessParam);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.INCREASE.getCode()));
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(tx.getNum());
        nodeOpt.setTime(tx.getTime());


        return nodeOpt;
    }

}
