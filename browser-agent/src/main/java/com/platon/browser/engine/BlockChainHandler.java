package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.config.BlockChainConfig;
import com.platon.browser.exception.*;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static com.platon.browser.engine.BlockChain.*;

/**
 * 采集到新区块事件处理类
 * @Auther: Chendongming
 * @Date: 2019/8/17 20:09
 * @Description:
 */
@Component
public class BlockChainHandler {
    private static Logger logger = LoggerFactory.getLogger(BlockChainHandler.class);

    private BlockChain bc;
    private StakingExecute stakingExecute;
    private ProposalExecute proposalExecute;
    private AddressExecute addressExecute;
    private BlockChainConfig chainConfig;
    private PlatonClient client;
    private Map<String, Node> preVerifier,curVerifier,preValidator,curValidator;
    public void init(BlockChain bc) {
        this.bc = bc;
        this.chainConfig = bc.getChainConfig();
        this.stakingExecute = bc.getStakingExecute();
        this.proposalExecute = bc.getProposalExecute();
        this.addressExecute = bc.getAddressExecute();
        this.client = bc.getClient();
        this.preVerifier = bc.getPreVerifier();
        this.curVerifier = bc.getCurVerifier();
        this.preValidator = bc.getPreValidator();
        this.curValidator = bc.getCurValidator();
    }

    /**
     * 周期变更通知：
     * 通知各钩子方法处理周期临界点事件，以便更新与周期切换相关的信息
     */
    public void periodChangeNotify() throws SettleEpochChangeException, ConsensusEpochChangeException, ElectionEpochChangeException {
        // 根据区块号是否整除周期来触发周期相关处理方法
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        if (blockNumber % chainConfig.getElectionDistance().longValue() == 0) {
            logger.debug("开始验证人选举：Block Number({})", blockNumber);
            stakingExecute.onElectionDistance(curBlock, bc);

        }

        if (blockNumber % chainConfig.getConsensusPeriod().longValue() == 0) {
            logger.debug("进入新共识周期：Block Number({})", blockNumber);
            stakingExecute.onNewConsEpoch(curBlock, bc);

        }

        if (blockNumber % chainConfig.getSettingPeriod().longValue() == 0) {
            logger.debug("进入新结算周期：Block Number({})", blockNumber);
            stakingExecute.onNewSettingEpoch(curBlock, bc);

        }
    }


    /**
     * 根据区块号推算并更新共识周期和结算周期轮数
     */
    public void updateEpoch () {
        CustomBlock curBlock = bc.getCurBlock();
        // 根据区块号是否与周期整除来触发周期相关处理方法
        // 计算共识周期轮数
        BigInteger curConsensusEpoch = BigInteger.valueOf(BigDecimal.valueOf(curBlock.getNumber())
                .divide(BigDecimal.valueOf(chainConfig.getConsensusPeriod().longValue()), 0, RoundingMode.CEILING).longValue());
        bc.setCurConsensusEpoch(curConsensusEpoch);
        // 计算结算周期轮数
        BigInteger curSettingEpoch = BigInteger.valueOf(BigDecimal.valueOf(curBlock.getNumber())
                .divide(BigDecimal.valueOf(chainConfig.getSettingPeriod().longValue()), 0, RoundingMode.CEILING).longValue());
        bc.setCurSettingEpoch(curSettingEpoch);
        //计算增发周期轮数
        BigInteger addIssueEpoch = BigInteger.valueOf(BigDecimal.valueOf(curBlock.getNumber())
                .divide(BigDecimal.valueOf(chainConfig.getAddIssuePeriod().longValue()), 0, RoundingMode.CEILING).longValue());
        bc.setAddIssueEpoch(addIssueEpoch);
    }

    /**
     * 更新共识周期验证人和结算周期验证人映射缓存
     * // 假设当前链上最高区块号为750
     * 1         250        500        750
     * |----------|----------|----------|
     * A B C      A C D       B C D
     * 共识周期的临界块号分别是：1,250,500,750
     * 使用临界块号查到的验证人：1=>"A,B,C",250=>"A,B,C",500=>"A,C,D",750=>"B,C,D"
     * 如果当前区块号为753，由于未达到
     */
    public void updateValidator () throws CandidateException {
        CustomBlock curBlock = bc.getCurBlock();
        // 根据区块号是否整除周期来触发周期相关处理方法
        Long blockNumber = curBlock.getNumber();
        Long consensusPeriod = chainConfig.getConsensusPeriod().longValue();
        if (blockNumber % consensusPeriod == 0) {
            logger.debug("共识周期切换块号:{}", blockNumber);
            try {
                // 获取链上实时区块号
                Long realTimeBlockNumber = client.getWeb3j().platonBlockNumber().send().getBlockNumber().longValue();

                /****************************更新前一轮共识验证人列表************************/
                if (curValidator.size()>0) {
                    // 如果curValidator有元素，则把它们转存到preValidator
                    preValidator.clear();
                    preValidator.putAll(curValidator);
                }else {
                    // 如果curValidator为空，则使用当前共识周期切换区块号查询前一轮共识周期验证人
                    BaseResponse<List<Node>> result = client.getHistoryValidatorList(BigInteger.valueOf(blockNumber));
                    if (result.isStatusOk()) {
                        curValidator.clear();
                        result.data.stream().filter(Objects::nonNull).forEach(node -> preValidator.put(HexTool.prefix(node.getNodeId()), node));
                    }
                }

                /****************************更新当前轮共识验证人列表************************/
                if(realTimeBlockNumber>blockNumber && realTimeBlockNumber<(blockNumber+consensusPeriod)){
                    // 如果链上实时区块号大于当前同步区块号，且链上实时区块号<(blockNumber+consensusPeriod)时,
                    // 即当前区块号和链上实时区块号处在同一共识周期时，则查询实时共识验证人列表作为当前共识轮验证人列表
                    BaseResponse <List <Node>> result = client.getNodeContract().getValidatorList().send();
                    if (result.isStatusOk()) {
                        curValidator.clear();
                        result.data.stream().filter(Objects::nonNull).forEach(node -> curValidator.put(HexTool.prefix(node.getNodeId()), node));
                    }
                }

                if(realTimeBlockNumber>=(blockNumber+consensusPeriod)){
                    // 如果链上实时区块号>=(blockNumber+consensusPeriod)，则查询(blockNumber+1)时的共识验证人列表
                    BaseResponse <List <Node>> result = client.getHistoryValidatorList(BigInteger.valueOf(blockNumber+1));
                    if (result.isStatusOk()) {
                        curValidator.clear();
                        result.data.stream().filter(Objects::nonNull).forEach(node -> curValidator.put(HexTool.prefix(node.getNodeId()), node));
                    }
                }
            } catch (Exception e) {
                logger.error("更新共识周期验证人列表失败,原因：{}", e.getMessage());
            }
            if(curValidator.size()==0){
                throw new CandidateException("查询不到共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+bc.getCurConsensusEpoch()+")");
            }
        }
    }

    /**
     * 更新结算周期验证人
     * // 假设当前链上最高区块号为750
     * 1         250        500        750
     * |----------|----------|----------|
     * A B C      A C D       B C D
     * 结算周期的临界块号分别是：1,250,500,750
     * 使用临界块号查到的验证人：1=>"A,B,C",250=>"A,B,C",500=>"A,C,D",750=>"B,C,D"
     * 如果当前区块号为753，由于未达到
     */
    public void updateVerifier () throws CandidateException {
        CustomBlock curBlock = bc.getCurBlock();
        // 根据区块号是否整除周期来触发周期相关处理方法
        Long blockNumber = curBlock.getNumber();
        Long settingPeriod = chainConfig.getSettingPeriod().longValue();
        if (blockNumber % settingPeriod == 0) {
            logger.debug("结算周期切换块号:{}", blockNumber);
            // 直接查当前最新的共识周期验证人列表来初始化blockChain的curValidators属性
            try {
                // 获取链上实时区块号
                Long realTimeBlockNumber = client.getWeb3j().platonBlockNumber().send().getBlockNumber().longValue();
                /****************************更新前一轮结算验证人列表************************/
                if (curVerifier.size()>0) {
                    // 如果curValidator有元素，则把它们转存到preValidator
                    preVerifier.clear();
                    preVerifier.putAll(curVerifier);
                }else {
                    // 如果curVerifier为空，则使用当前结算周期切换区块号查询前一轮结算周期验证人
                    BaseResponse <List <Node>> result = client.getHistoryVerifierList(BigInteger.valueOf(blockNumber));
                    if (result.isStatusOk()) {
                        curVerifier.clear();
                        result.data.stream().filter(Objects::nonNull).forEach(node -> preVerifier.put(HexTool.prefix(node.getNodeId()), node));
                    }
                }

                /****************************更新当前轮结算验证人列表************************/
                if(realTimeBlockNumber>blockNumber && realTimeBlockNumber<(blockNumber+settingPeriod)){
                    // 如果链上实时区块号大于当前同步区块号，且链上实时区块号<(blockNumber+settingPeriod)时,
                    // 即当前区块号和链上实时区块号处在同一结算周期时，则查询实时结算验证人列表作为当前结算轮验证人列表
                    BaseResponse <List <Node>> result = client.getNodeContract().getVerifierList().send();
                    if (result.isStatusOk()) {
                        curVerifier.clear();
                        result.data.stream().filter(Objects::nonNull).forEach(node -> curVerifier.put(HexTool.prefix(node.getNodeId()), node));
                    }
                }

                if(realTimeBlockNumber>=(blockNumber+settingPeriod)){
                    // 如果链上实时区块号>=(blockNumber+settingPeriod)，则查询(blockNumber+1)时的共识验证人列表
                    BaseResponse <List <Node>> result = client.getHistoryVerifierList(BigInteger.valueOf(blockNumber+1));
                    if (result.isStatusOk()) {
                        curVerifier.clear();
                        result.data.stream().filter(Objects::nonNull).forEach(node -> curVerifier.put(HexTool.prefix(node.getNodeId()), node));
                    }
                }
            } catch (Exception e) {
                logger.error("更新结算周期验证人列表失败,原因：{}", e.getMessage());
            }
            if(curVerifier.size()==0){
                throw new CandidateException("查询不到结算周期验证人(当前块号="+blockNumber+",当前结算轮数="+bc.getCurSettingEpoch()+")");
            }
        }
    }

    /**
     * 根据交易信息新增或更新相关记录：
     */
    public void analyzeTransaction () {
        CustomBlock curBlock = bc.getCurBlock();
        curBlock.getTransactionList().forEach(tx -> {
            // 链上执行失败的交易不予处理
            if (CustomTransaction.TxReceiptStatusEnum.FAILURE.code == tx.getTxReceiptStatus()) return;
            // 调用交易分析引擎分析交易，以补充相关数据
            switch (tx.getTypeEnum()) {
                case CREATE_VALIDATOR: // 创建验证人
                case EDIT_VALIDATOR: // 编辑验证人
                case INCREASE_STAKING: // 增持质押
                case EXIT_VALIDATOR: // 撤销质押
                case DELEGATE: // 发起委托
                case UN_DELEGATE: // 撤销委托
                case REPORT_VALIDATOR: // 举报多签验证人
                    stakingExecute.execute(tx, bc);
                    break;
                case CREATE_PROPOSAL_TEXT: // 创建文本提案
                case CREATE_PROPOSAL_UPGRADE: // 创建升级提案
                case CREATE_PROPOSAL_PARAMETER: // 创建参数提案
                case VOTING_PROPOSAL: // 给提案投票
                case DUPLICATE_SIGN: // 双签举报
                    proposalExecute.execute(tx, bc);
                    break;
                case CONTRACT_CREATION: // 合约发布(合约创建)
                    logger.debug("合约发布(合约创建): txHash({}),contract({})", tx.getHash(), tx.getTo());
                    break;
                case TRANSFER: // 转账
                    logger.debug("转账: txHash({}),from({}),to({})", tx.getHash(), tx.getFrom(), tx.getTo());
                    break;
                case OTHERS: // 其它
                case MPC:
            }
            // 地址相关
            //addressExecute.execute(tx);
            //更新统计信息
            updateWithNetworkStat();
        });
    }


    /**
     * 统计设置常规值
     */
    public void updateWithNetworkStat () {
        CustomBlock curBlock = bc.getCurBlock();
        try {
            //TODO:地址数需要地址统计
            //当前区块高度
            NETWORK_STAT_CACHE.setCurrentNumber(curBlock.getNumber());
            //当前区块所属节点id
            NETWORK_STAT_CACHE.setNodeId(curBlock.getNodeId());
            //当前区块所属节点name
            NETWORK_STAT_CACHE.setNodeName(NODE_CACHE.getNode(curBlock.getNodeId()).getLatestStaking().getStakingName());
            //TODO:可优化
            //当前增发周期结束块高 =  每个增发周期块数 *  当前增发周期轮数
            NETWORK_STAT_CACHE.setAddIssueEnd(chainConfig.getAddIssuePeriod().multiply(bc.getAddIssueEpoch()).longValue());
            //TODO:可优化
            //当前增发周期开始块高 = (每个增发周期块数 * 当前增发周期轮数) - 每个增发周期块数
            NETWORK_STAT_CACHE.setAddIssueBegin(chainConfig.getAddIssuePeriod().multiply(bc.getAddIssueEpoch()).subtract(chainConfig.getAddIssuePeriod()).longValue());
            //离下个结算周期剩余块高 = (每个结算周期块数 * 当前结算周期轮数) - 当前块高
            NETWORK_STAT_CACHE.setNextSetting(chainConfig.getSettingPeriod().multiply(bc.getCurSettingEpoch()).subtract(curBlock.getBlockNumber()).longValue());
            //质押奖励
            NETWORK_STAT_CACHE.setStakingReward(NODE_CACHE.getNode(curBlock.getNodeId()).getLatestStaking().getStakingRewardValue());
            if (null != NETWORK_STAT_CACHE) {
                //更新时间
                NETWORK_STAT_CACHE.setUpdateTime(new Date());
                //累计交易总数
                NETWORK_STAT_CACHE.setTxQty(NETWORK_STAT_CACHE.getTxQty() + curBlock.getStatTxQty());
                //当前区块交易总数
                NETWORK_STAT_CACHE.setCurrentTps(curBlock.getStatTxQty());
                //已统计区块中最高交易个数
                NETWORK_STAT_CACHE.setMaxTps(NETWORK_STAT_CACHE.getMaxTps() > curBlock.getStatTxQty() ? NETWORK_STAT_CACHE.getMaxTps() : curBlock.getStatTxQty());
                //出块奖励
                NETWORK_STAT_CACHE.setBlockReward(curBlock.getBlockReward());
                if (curBlock.getStatProposalQty() > 0) {
                    //累计提案总数
                    NETWORK_STAT_CACHE.setProposalQty(NETWORK_STAT_CACHE.getProposalQty() + curBlock.getStatProposalQty());
                }
                if (curBlock.getStatStakingQty() > 0) {
                    //统计质押金额
                    Set <Staking> newStaking = BlockChain.STAGE_BIZ_DATA.getStakingExecuteResult().getAddStakings();
                    newStaking.forEach(staking -> {
                        BigInteger stakingValue = new BigInteger(NETWORK_STAT_CACHE.getStakingValue()).add(new BigInteger(staking.getStakingHas())).add(new BigInteger(staking.getStakingLocked()));
                        NETWORK_STAT_CACHE.setStakingValue(stakingValue.toString());
                    });
                }
                if (curBlock.getStatDelegateQty() > 0) {
                    //质押已统计，本次累加上委托
                    Set <Delegation> newDelegation = BlockChain.STAGE_BIZ_DATA.getStakingExecuteResult().getAddDelegations();
                    newDelegation.forEach(delegation -> {
                        //先做委托累加
                        BigInteger delegationValue = new BigInteger(delegation.getDelegateHas()).add(new BigInteger(delegation.getDelegateLocked())).add(new BigInteger(NETWORK_STAT_CACHE.getStakingDelegationValue()));
                        NETWORK_STAT_CACHE.setStakingDelegationValue(delegationValue.toString());
                    });
                    //在累加计算好的质押金
                    NETWORK_STAT_CACHE.setStakingDelegationValue(new BigInteger(NETWORK_STAT_CACHE.getStakingDelegationValue()).add(new BigInteger(NETWORK_STAT_CACHE.getStakingValue())).toString());
                }

                if (BlockChain.STAGE_BIZ_DATA.getProposalExecuteResult().getAddProposals().size() > 0 || BlockChain.STAGE_BIZ_DATA.getProposalExecuteResult().getUpdateProposals().size() > 0) {
                    PROPOSALS_CACHE.forEach(( hash, proposal ) -> {
                        if (proposal.getStatus().equals(CustomProposal.StatusEnum.VOTEING.code)) {
                            NETWORK_STAT_CACHE.setDoingProposalQty(NETWORK_STAT_CACHE.getDoingProposalQty() + 1);
                        }
                    });
                }
            }

        } catch (NoSuchBeanException e) {
            logger.error("");
        }


    }
}
