package com.platon.browser.service.epoch;

import com.platon.browser.bean.CommonConstant;
import com.platon.browser.bean.ConfigChange;
import com.platon.browser.bean.EpochInfo;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.constant.Browser;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexUtil;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.Web3j;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 此类非线程安全
 * 奖励计算服务
 * 1、根据区块号计算周期切换相关值：
 * 名称/含义                                                                   变量名称
 * 当前增发周期开始时的激励池余额 IB                                             inciteBalance
 * 当前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例               inciteAmount4Block
 * 当前增发周期每个区块奖励值 BR/增发周期区块总数                                 blockReward
 * 当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例               inciteAmount4Stake
 * 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数        settleStakeReward
 * 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数         stakeReward
 * 当前共识周期验证人                                                            curValidators
 * 当前结算周期验证人                                                            curVerifiers
 */
@Slf4j
@Service
public class EpochRetryService {

    private final Queue<ConfigChange> epochChanges = new LinkedList<>();

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private SpecialApi specialApi;

    // 注意：以下所有属性在其所属周期内都是不变的，只有在各自周期变更时才更新各自的值
    // ******* 增发周期相关属性 START *******
    @Getter
    private BigDecimal blockReward = BigDecimal.ZERO; // 当前增发周期每个区块奖励值 BR/增发周期区块总数

    @Getter
    private BigDecimal settleStakeReward = BigDecimal.ZERO;  // 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数

    // ******* 增发周期相关属性 END *******
    // ******* 共识周期相关属性 START *******
    @Getter
    private final List<Node> preValidators = new ArrayList<>(); // 前一共识周期验证人列表

    @Getter
    private final List<Node> curValidators = new ArrayList<>(); // 当前共识周期验证人列表

    @Getter
    private Long expectBlockCount = 0L; // 当前期望出块数

    // ******* 共识周期相关属性 END *******
    // ******* 结算周期相关属性 START *******
    @Getter
    private final List<Node> preVerifiers = new ArrayList<>(); // 前一结算周期验证人列表

    @Getter
    private final List<Node> curVerifiers = new ArrayList<>(); // 当前结算周期验证人列表

    @Getter
    private BigDecimal stakeReward = BigDecimal.ZERO; // 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数

    @Getter
    private BigDecimal preStakeReward = BigDecimal.ZERO; // 前一结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数
    // ******* 结算周期相关属性 END *******

    @Resource
    private NetworkStatCache networkStatCache;

    /**
     * 增发周期变更:
     * 必然伴随着结算周期和共识周期的变更
     *
     * @param currentBlockNumber 增发周期内的任意块
     */
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public void issueChange(BigInteger currentBlockNumber) {
        log.debug("增发周期变更:{}({})", Thread.currentThread().getStackTrace()[1].getMethodName(), currentBlockNumber);
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public void recover(Exception e) {
        log.error("重试完成还是业务失败，请联系管理员处理");
    }

    /**
     * 共识周期变更
     * 1、更新当前周期验证人
     * 2、更新前一周期验证人
     * 3、更新验证人期望出块数
     *
     * @param currentBlockNumber 共识周期内的任意块
     */
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public void consensusChange(BigInteger currentBlockNumber) {
        log.debug("共识周期变更:{}({})", Thread.currentThread().getStackTrace()[1].getMethodName(), currentBlockNumber);
        try {
            // 当前块所处的共识周期
            BigInteger currentEpoch = EpochUtil.getEpoch(currentBlockNumber, chainConfig.getConsensusPeriodBlockCount());
            // 链上最新块所处的共识周期
            Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();
            BigInteger latestBlockNumber = platOnClient.getLatestBlockNumber();
            BigInteger latestEpoch = EpochUtil.getEpoch(latestBlockNumber, chainConfig.getConsensusPeriodBlockCount());
            // 上一个周期的最后一个块号
            BigInteger preEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber, chainConfig.getConsensusPeriodBlockCount());

            // 前一周期的验证人
            List<Node> preNodes = specialApi.getHistoryValidatorList(web3j, preEpochLastBlockNumber);
            preNodes.forEach(n -> n.setNodeId(HexUtil.prefix(n.getNodeId())));
            preValidators.clear();
            preValidators.addAll(preNodes);

            // 当前周期的验证人
            List<Node> curNodes = Collections.emptyList();
            if (latestEpoch.compareTo(currentEpoch) > 0) {
                // >>>>如果链上最新块所在周期>当前块所处周期, 则查询特殊节点历史接口
                // 如果前一个周期的最后一个块是0，则查第0块时的验证人作为当前验证人
                BigInteger targetBlockNumber = preEpochLastBlockNumber.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ZERO : preEpochLastBlockNumber.add(BigInteger.ONE);
                curNodes = specialApi.getHistoryValidatorList(web3j, targetBlockNumber);
            }
            if (latestEpoch.compareTo(currentEpoch) == 0) {
                // >>>>如果链上最新块所在周期==当前块所处周期, 则查询实时接口
                curNodes = platOnClient.getLatestValidators();
            }
            curNodes.forEach(n -> n.setNodeId(HexUtil.prefix(n.getNodeId())));
            curValidators.clear();
            curValidators.addAll(curNodes);

            // 更新期望出块数：期望出块数=共识周期块数/实际参与共识节点数
            expectBlockCount = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(curValidators.size())).longValue();
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            log.error("", e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 结算周期变更
     * 1、更新当前周期验证人
     * 2、更新前一周期验证人
     * 3、更新当前周期区块奖励
     * 4、更新当前周期质押奖励
     *
     * @param currentBlockNumber 结算周期内的任意块
     */
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public void settlementChange(BigInteger currentBlockNumber) {
        log.debug("结算周期变更:{}({})", Thread.currentThread().getStackTrace()[1].getMethodName(), currentBlockNumber);
        try {
            // 当前块所处周期
            BigInteger currentEpoch = EpochUtil.getEpoch(currentBlockNumber, chainConfig.getSettlePeriodBlockCount());
            // 链上最新块所处周期
            Web3j web3j = platOnClient.getWeb3jWrapper().getWeb3j();
            BigInteger latestBlockNumber = platOnClient.getLatestBlockNumber();
            BigInteger latestEpoch = EpochUtil.getEpoch(latestBlockNumber, chainConfig.getSettlePeriodBlockCount());
            // 上一个周期的最后一个块号
            BigInteger preEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber, chainConfig.getSettlePeriodBlockCount());

            // 前一周期的验证人
            List<Node> preNodes = specialApi.getHistoryVerifierList(web3j, preEpochLastBlockNumber);
            preNodes.forEach(n -> n.setNodeId(HexUtil.prefix(n.getNodeId())));
            preVerifiers.clear();
            preVerifiers.addAll(preNodes);

            // 当前周期的验证人
            List<Node> curNodes = Collections.emptyList();
            if (latestEpoch.compareTo(currentEpoch) > 0) {
                // >>>>如果链上最新块所在周期>当前块所处周期, 则查询特殊节点历史接口
                // 如果前一个周期的最后一个块是0，则查第0块时的验证人作为当前验证人
                BigInteger targetBlockNumber = preEpochLastBlockNumber.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ZERO : preEpochLastBlockNumber.add(BigInteger.ONE);
                curNodes = specialApi.getHistoryVerifierList(web3j, targetBlockNumber);
            }
            if (latestEpoch.compareTo(currentEpoch) == 0) {
                // >>>>如果链上最新块所在周期==当前块所处周期, 则查询实时接口
                curNodes = platOnClient.getLatestVerifiers();
            }
            curNodes.forEach(n -> n.setNodeId(HexUtil.prefix(n.getNodeId())));
            curVerifiers.clear();
            curVerifiers.addAll(curNodes);

            // 上一结算周期最后一个块号
            BigInteger preSettleEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber, chainConfig.getSettlePeriodBlockCount());
            // 从特殊接口获取
            EpochInfo epochInfo = specialApi.getEpochInfo(platOnClient.getWeb3jWrapper().getWeb3j(), preSettleEpochLastBlockNumber);
            // 区块奖励
            blockReward = epochInfo.getPackageReward();
            // 当前增发周期内每个结算周期的质押奖励
            settleStakeReward = epochInfo.getStakingReward();
            // 前一结算周期质押奖励轮换
            preStakeReward = stakeReward;
            // 计算当前结算周期内每个验证人的质押奖励
            stakeReward = handleStakeReward(preSettleEpochLastBlockNumber, currentEpoch.subtract(BigInteger.ONE), epochInfo.getCurStakingReward());
            ConfigChange configChange = new ConfigChange();
            configChange.setAvgPackTime(epochInfo.getAvgPackTime());
            configChange.setBlockReward(epochInfo.getNextPackageReward());
            configChange.setIssueEpoch(epochInfo.getYearNum());
            configChange.setYearStartNum(epochInfo.getYearStartNum());
            configChange.setYearEndNum(epochInfo.getYearEndNum());
            configChange.setRemainEpoch(epochInfo.getRemainEpoch());
            configChange.setSettleStakeReward(epochInfo.getNextStakingReward());
            configChange.setStakeReward(stakeReward);
            epochChanges.offer(configChange);
            applyConfigChange();
        } catch (Exception e) {
            log.error("结算周期变更异常，即将重试", e);
            platOnClient.updateCurrentWeb3jWrapper();
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 在n*10750+1个快高计算n*10750快高时的质押奖励
     *
     * @param preSettleEpochLastBlockNumber 上一个结算周期的最后一个块号
     * @param preEpoch                      上一个结算周期
     * @param preStakingReward              上一个结算周期的总质押奖励
     * @return: java.math.BigDecimal
     * @date: 2022/3/7
     */
    private BigDecimal handleStakeReward(BigInteger preSettleEpochLastBlockNumber, BigInteger preEpoch, BigDecimal preStakingReward) throws Exception {
        List<Node> lastNodes = specialApi.getHistoryVerifierList(platOnClient.getWeb3jWrapper().getWeb3j(), preSettleEpochLastBlockNumber);
        BigDecimal stakeReward = preStakingReward.divide(BigDecimal.valueOf(lastNodes.size()), 0, BigDecimal.ROUND_DOWN);
        log.info("块高[{}]第[{}]个结算周期，质押奖励[{}]=总质押奖励[{}]/验证人数量[{}]", preSettleEpochLastBlockNumber, preEpoch, stakeReward.toPlainString(), preStakingReward.toPlainString(), lastNodes.size());
        return stakeReward;
    }

    /**
     * 获取实时候选人列表
     *
     * @return
     * @throws Exception
     */
    @Retryable(value = Exception.class, maxAttempts = CommonConstant.reTryNum)
    public List<Node> getCandidates() throws CandidateException {
        log.debug("获取实时候选人列表:{}()", Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            CallResponse<List<Node>> br = platOnClient.getNodeContract().getCandidateList().send();
            if (!br.isStatusOk()) {
                throw new CandidateException(br.getErrMsg());
            }
            List<Node> candidates = br.getData();
            if (candidates == null) {
                throw new CandidateException("实时候选节点列表为空!");
            }
            candidates.forEach(v -> v.setNodeId(HexUtil.prefix(v.getNodeId())));
            return candidates;
        } catch (Exception e) {
            platOnClient.updateCurrentWeb3jWrapper();
            log.error("", e);
            throw new CandidateException(e.getMessage());
        }
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public List<Node> recoverCandidates(Exception e) {
        log.error("重试完成还是业务失败，请联系管理员处理");
        List<Node> candidates = new ArrayList<>();
        return candidates;
    }

    /**
     * 应用配置变更
     * 1、更新BlockChainConfig涉及的相关配置项
     * 2、更新网络统计缓存涉及的相关数据项
     */
    public void applyConfigChange() {
        ConfigChange summary = new ConfigChange();
        while (epochChanges.peek() != null) {
            ConfigChange configChange = epochChanges.poll();

            if (configChange.getIssueEpoch() != null) {
                /**
                 * 当年份不一样时候需要更新network比例
                 */
                if (chainConfig.getIssueEpochRound() != null && chainConfig.getIssueEpochRound().compareTo(configChange.getIssueEpoch()) != 0) {
                    NetworkStat networkStat = networkStatCache.getNetworkStat();
                    summary.setIssueRates(networkStat.getIssueRates() + Browser.HTTP_SPILT + chainConfig.getAddIssueRate().toPlainString());
                }
                // 更新增发周期轮数
                chainConfig.setIssueEpochRound(configChange.getIssueEpoch());
                summary.setIssueEpoch(configChange.getIssueEpoch());
            }
            if (configChange.getYearStartNum() != null) {
                // 更新增发周期起始块号
                chainConfig.setIssueEpochStartBlockNumber(configChange.getYearStartNum());
                summary.setYearStartNum(configChange.getYearStartNum());
            }
            if (configChange.getYearEndNum() != null) {
                // 更新增发周期结束块号
                chainConfig.setIssueEpochEndBlockNumber(configChange.getYearEndNum());
                summary.setYearEndNum(configChange.getYearEndNum());
            }

            if (configChange.getYearStartNum() != null && configChange.getYearEndNum() != null) {
                // 更新增发周期区块数
                BigInteger blockCountPerIssue = configChange.getYearEndNum().subtract(configChange.getYearStartNum()).add(BigDecimal.ONE).toBigInteger();
                chainConfig.setAddIssuePeriodBlockCount(blockCountPerIssue);
                // 更新每个增发周期的结算周期数
                chainConfig.setSettlePeriodCountPerIssue(chainConfig.getAddIssuePeriodBlockCount().divide(chainConfig.getSettlePeriodBlockCount()));
            }

            if (configChange.getSettleStakeReward() != null) {
                summary.setSettleStakeReward(configChange.getSettleStakeReward());
            }
            if (configChange.getBlockReward() != null) {
                summary.setBlockReward(configChange.getBlockReward());
            }
            if (configChange.getStakeReward() != null) {
                summary.setStakeReward(configChange.getStakeReward());
            }
            if (configChange.getAvgPackTime() != null) {
                summary.setAvgPackTime(configChange.getAvgPackTime());
            }
        }
        networkStatCache.updateByEpochChange(summary);
    }

}
