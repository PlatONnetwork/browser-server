package com.platon.browser.analyzer.ppos;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.dao.custommapper.StakeBusinessMapper;
import com.platon.browser.dao.param.ppos.StakeCreate;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.StakeCreateParam;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.service.ppos.StakeEpochService;
import com.platon.browser.utils.ChainVersionUtil;
import com.platon.browser.utils.DateUtil;
import com.platon.browser.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;


/**
 * @description: 创建验证人(质押)业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class StakeCreateAnalyzer
        extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private StakeBusinessMapper stakeBusinessMapper;

    @Resource
    private ParameterService parameterService;

    @Resource
    private StakeEpochService stakeEpochService;

    /**
     * 发起质押(创建验证人)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus())
            return null;

        long startTime = System.currentTimeMillis();

        StakeCreateParam txParam = tx.getTxParam(StakeCreateParam.class);
        BigInteger bigVersion = ChainVersionUtil.toBigVersion(txParam.getProgramVersion());
        BigInteger stakingBlockNum = BigInteger.valueOf(tx.getNum());

        String configVal = parameterService.getValueInBlockChainConfig(ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        if (StringUtils.isBlank(configVal)) {
            throw new BusinessException("参数表参数缺失：" + ModifiableGovernParamEnum.UN_STAKE_FREEZE_DURATION.getName());
        }
        Date txTime = DateUtil.covertTime(tx.getTime());
        // 更新解质押到账需要经过的结算周期数
        BigInteger unStakeFreezeDuration = stakeEpochService.getUnStakeFreeDuration();
        // 理论上的退出区块号
        BigInteger unStakeEndBlock = stakeEpochService.getUnStakeEndBlock(txParam.getNodeId(), event.getEpochMessage().getSettleEpochRound(), false);
        StakeCreate businessParam = StakeCreate.builder()
                                               .nodeId(txParam.getNodeId())
                                               .stakingHes(txParam.getAmount())
                                               .nodeName(txParam.getNodeName())
                                               .externalId(txParam.getExternalId())
                                               .benefitAddr(txParam.getBenefitAddress())
                                               .programVersion(txParam.getProgramVersion().toString())
                                               .bigVersion(bigVersion.toString())
                                               .webSite(txParam.getWebsite())
                                               .details(txParam.getDetails())
                                               .isInit(isInit(txParam.getBenefitAddress()))
                                               .stakingBlockNum(stakingBlockNum)
                                               .stakingTxIndex(tx.getIndex())
                                               .stakingAddr(tx.getFrom())
                                               .joinTime(txTime)
                                               .txHash(tx.getHash())
                                               .delegateRewardPer(txParam.getDelegateRewardPer())
                                               .unStakeFreezeDuration(unStakeFreezeDuration.intValue())
                                               .unStakeEndBlock(unStakeEndBlock)
                                               .settleEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                                               .build();

        stakeBusinessMapper.create(businessParam);

        updateNodeCache(HexUtil.prefix(txParam.getNodeId()), txParam.getNodeName(), stakingBlockNum);

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getNodeId());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.CREATE.getCode()));
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(tx.getNum());
        nodeOpt.setTime(txTime);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return nodeOpt;
    }

}
