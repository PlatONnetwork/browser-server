package com.platon.browser.v0150.service;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomAddressMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.param.ppos.DelegateExit;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.v0150.V0150Config;
import com.platon.browser.v0150.bean.AdjustParam;
import com.platon.browser.v0150.bean.ValidatedContext;
import com.platon.browser.v0150.context.AbstractAdjustContext;
import com.platon.browser.v0150.context.DelegateAdjustContext;
import com.platon.browser.v0150.context.StakingAdjustContext;
import com.platon.browser.v0150.dao.StakingDelegateBalanceAdjustmentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class StakingDelegateBalanceAdjustmentService {

    @Resource
    protected DelegationMapper delegationMapper;

    @Resource
    protected StakingMapper stakingMapper;

    @Resource
    protected NodeMapper nodeMapper;

    @Resource
    protected StakingDelegateBalanceAdjustmentMapper stakingDelegateBalanceAdjustmentMapper;

    @Resource
    protected BlockChainConfig chainConfig;

    @Resource
    protected V0150Config v0150Config;

    @Resource
    private CustomAddressMapper customAddressMapper;

    private static final Logger log = Logger.getLogger(StakingDelegateBalanceAdjustmentService.class.getName());

    @PostConstruct
    private void init() {
        File logFile = new File(v0150Config.getAdjustLogFilePath());
        if (logFile.exists()) {
            boolean deleted = logFile.delete();
            if (!deleted) log.warning("删除日志文件失败！");
        }
        try {
            log.setLevel(Level.INFO);
            FileHandler fileHandler = new FileHandler(v0150Config.getAdjustLogFilePath());
            fileHandler.setLevel(Level.INFO);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            log.addHandler(fileHandler);
        } catch (SecurityException | IOException e) {
            log.warning(e.getMessage());
        }
    }

    /**
     * 验证调账参数，返回验证结果
     *
     * @param adjustParams
     * @return
     */
    public void adjust(List<AdjustParam> adjustParams) throws BlockNumberException {
        ValidatedContext validatedContext = new ValidatedContext();
        if (adjustParams.isEmpty()) return;
        // 针对每笔委托调账数据获取完整的【调账上下文数据】
        for (AdjustParam adjustParam : adjustParams) {
            AbstractAdjustContext context = null;
            if ("staking".equals(adjustParam.getOptType())) {
                // 拼装质押调账需要的完整上下文数据
                StakingAdjustContext sac = new StakingAdjustContext();
                validatedContext.getStakingAdjustContextList().add(sac);
                context = sac;
            }

            if ("delegate".equals(adjustParam.getOptType())) {
                // 根据<委托者地址,质押块高,节点ID>找到对应的委托信息
                DelegationKey delegationKey = new DelegationKey();
                delegationKey.setDelegateAddr(adjustParam.getAddr());
                delegationKey.setStakingBlockNum(Long.valueOf(adjustParam.getStakingBlockNum()));
                delegationKey.setNodeId(adjustParam.getNodeId());
                Delegation delegation = delegationMapper.selectByPrimaryKey(delegationKey);
                // 拼装委托调账需要的完整上下文数据
                DelegateAdjustContext dac = new DelegateAdjustContext();
                dac.setDelegation(delegation);
                validatedContext.getDelegateAdjustContextList().add(dac);
                context = dac;
            }

            if (context != null) {
                context.setChainConfig(chainConfig);
                context.setAdjustParam(adjustParam);
                // 根据<质押块高,节点ID>找到对应的质押信息
                StakingKey stakingKey = new StakingKey();
                stakingKey.setNodeId(adjustParam.getNodeId());
                stakingKey.setStakingBlockNum(Long.valueOf(adjustParam.getStakingBlockNum()));
                Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
                context.setStaking(staking);
                // 根据<节点ID>找到对应的节点信息
                Node node = nodeMapper.selectByPrimaryKey(adjustParam.getNodeId());
                context.setNode(node);
                // 校验上下文
                context.validate();

                String adjustMsg;
                if ("delegate".equals(adjustParam.getOptType())) {
                    // 委托调账
                    if (StringUtils.isBlank(context.errorInfo())) {
                        AdjustParam param = context.getAdjustParam();
                        // 调账上下文没有错误信息，则执行调账操作
                        stakingDelegateBalanceAdjustmentMapper.adjustDelegateData(param);

                        // 更新缓存中的委托者地址已领取奖励
                        Delegation delegation = ((DelegateAdjustContext) context).getDelegation();
                        DelegateExit delegateExit = DelegateExit.builder().txFrom(delegation.getDelegateAddr()).delegateReward(param.getReward()).build();
                        customAddressMapper.updateAddressHaveReward(delegateExit.getTxFrom(), delegateExit.getDelegateReward());
                        StringBuilder sb = new StringBuilder("============ ").append(context.getAdjustParam().getOptType()).append("调账成功 ============\n").append(context.contextInfo());
                        adjustMsg = sb.toString();
                        log.info(adjustMsg);
                    } else {
                        // 调账上下文有错误信息，调账参数打印到错误文件
                        adjustMsg = context.errorInfo();
                        log.warning(adjustMsg);
                    }
                }

                if ("staking".equals(adjustParam.getOptType())) {
                    if (StringUtils.isBlank(context.errorInfo())) {
                        // 调账上下文没有错误信息，则执行调账操作
                        stakingDelegateBalanceAdjustmentMapper.adjustStakingData(context.getAdjustParam());
                        StringBuilder sb = new StringBuilder("============ ").append(context.getAdjustParam().getOptType()).append("调账成功 ============\n").append(context.contextInfo());
                        adjustMsg = sb.toString();
                        log.info(adjustMsg);
                    } else {
                        // 调账上下文有错误信息，调账参数打印到错误文件
                        adjustMsg = context.errorInfo();
                        log.warning(adjustMsg);
                    }
                }
            }
        }
    }

}
