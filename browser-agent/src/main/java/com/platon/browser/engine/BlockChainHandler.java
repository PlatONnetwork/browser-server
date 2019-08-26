package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.*;
import com.platon.browser.utils.HexTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.STAGE_DATA;

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
    private StakingEngine stakingExecute;
    private ProposalEngine proposalExecute;
    private AddressEngine addressExecute;
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

        // (当前块号+选举回退块数)%共识周期区块数
        if (blockNumber+chainConfig.getElectionBackwardBlockCount().longValue() % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            logger.debug("开始验证人选举：Block Number({})", blockNumber);
            stakingExecute.onElectionDistance(curBlock, bc);

        }

        if (blockNumber % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            logger.debug("进入新共识周期：Block Number({})", blockNumber);
            stakingExecute.onNewConsEpoch(curBlock, bc);

        }

        if (blockNumber % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            logger.debug("进入新结算周期：Block Number({})", blockNumber);
            stakingExecute.onNewSettingEpoch(curBlock, bc);

        }
    }


    /**
     * 初始化共识周期和结算周期轮数
     * @param blockNumber
     */
    public void initEpoch(Long blockNumber){
        BigInteger curConsensusEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getConsensusPeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        bc.setCurConsensusEpoch(curConsensusEpoch);
        // 计算结算周期轮数
        BigInteger curSettingEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getSettlePeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        bc.setCurSettingEpoch(curSettingEpoch);
        //计算增发周期轮数
        BigInteger addIssueEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getAddIssuePeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        bc.setAddIssueEpoch(addIssueEpoch);
    }

    /**
     * 根据区块号推算并更新共识周期和结算周期轮数
     */
    public void updateEpoch () {
        CustomBlock curBlock = bc.getCurBlock();
        // 根据区块号是否与周期整除来触发周期相关处理方法
        Long blockNumber = curBlock.getNumber();
        initEpoch(blockNumber);
    }

    public void initBlockRewardAndStakingReward(Long blockNumber) throws IssueEpochChangeException {
        // 激励池账户地址
        String incentivePoolAccountAddr = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.address;
        try {
            // 根据激励池地址查询前一增发周期末激励池账户余额：查询前一增发周期末块高时的激励池账户余额
            BigInteger incentivePoolAccountBalance = bc.getClient().getWeb3j()
                    .platonGetBalance(incentivePoolAccountAddr, DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)))
                    .send().getBalance();
            logger.debug("区块号位于({})时激励池账户余额:{}",blockNumber,incentivePoolAccountBalance.toString());
            // 激励池中的质押奖励部分
            BigDecimal stakingPart = new BigDecimal(incentivePoolAccountBalance.toString())
                    .multiply(bc.getChainConfig().getStakeRewardRate()); // 取出激励池余额中属于质押奖励的部分
            logger.debug("质押奖励部分:{}",stakingPart.toString());
            // 每个增发周期的总结算周期数
            BigDecimal settleEpochCountPerIssue = new BigDecimal(bc.getSettleEpochCountPerIssueEpoch());
            // 每个结算周期的质押奖励
            BigDecimal settleReward = stakingPart
                    .divide(settleEpochCountPerIssue,0, RoundingMode.FLOOR); // 除以结算周期轮数，向下取整
            bc.setSettleReward(settleReward);
            logger.debug("当前结算周期奖励:{}",settleReward.toString());
            // 激励池中的出块奖励部分
            BigDecimal blockingPart = new BigDecimal(incentivePoolAccountBalance)
                    .multiply(bc.getChainConfig().getBlockRewardRate()); // 取出激励池余额中属于区块奖励的部分
            logger.debug("区块奖励部分:{}",stakingPart.toString());
            // 每个增发周期的总块数
            BigDecimal issuePeriodBlockCount = new BigDecimal(bc.getChainConfig().getAddIssuePeriodBlockCount());
            // 出块奖励
            BigDecimal blockReward = blockingPart
                    .divide(issuePeriodBlockCount,0, RoundingMode.FLOOR); // 除以一个增发周期的总区块数，向下取整
            bc.setBlockReward(blockReward);
            logger.debug("当前区块奖励:{}",blockReward.toString());
        } catch (IOException e) {
            throw new IssueEpochChangeException("查询激励池(addr="+incentivePoolAccountAddr+")在块号("+blockNumber+")的账户余额失败:"+e.getMessage());
        }
    }

    /**
     * 在增发周期切换时更新区块奖励和质押奖励
     * @throws IssueEpochChangeException
     */
    public void updateBlockRewardAndStakingReward() throws IssueEpochChangeException {
        CustomBlock curBlock = bc.getCurBlock();
        Long blockNumber = curBlock.getNumber();
        Long issuePeriod = chainConfig.getAddIssuePeriodBlockCount().longValue();
        if (blockNumber==1 || blockNumber % issuePeriod == 0) {
            // 进入增发周期
            initBlockRewardAndStakingReward(blockNumber);
        }
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
        Long consensusPeriod = chainConfig.getConsensusPeriodBlockCount().longValue();
        if (blockNumber % consensusPeriod == 0) {
            logger.debug("共识周期切换块号:{}", blockNumber);
            try {
                BaseResponse <List <Node>> result;
                // ==================================更新前一周期验证人列表=======================================
                // 入参区块号属于前一共识周期，因此可以通过它查询前一共识周期验证人历史列表
                BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
                result = client.getHistoryValidatorList(prevEpochLastBlockNumber);
                if (!result.isStatusOk()) {
                    throw new CandidateException("【底层出错】查询块号在【"+prevEpochLastBlockNumber+"】的历史共识周期验证节点列表出错,请检查Agent共识周期块数设置是否与链一致:"+result.errMsg);
                }else{
                    preValidator.clear();
                    result.data.stream().filter(Objects::nonNull).forEach(node -> preValidator.put(HexTool.prefix(node.getNodeId()), node));
                }

                // ==================================更新当前周期验证人列表=======================================
                BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
                result = client.getHistoryValidatorList(nextEpochFirstBlockNumber);
                if (!result.isStatusOk()) {
                    // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
                    result = client.getNodeContract().getValidatorList().send();
                    if(!result.isStatusOk()){
                        throw new CandidateException("【底层出错】底层链查询实时共识周期验证节点列表出错:"+result.errMsg);
                    }
                }
                curValidator.clear();
                result.data.stream().filter(Objects::nonNull).forEach(node -> curValidator.put(HexTool.prefix(node.getNodeId()), node));
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
        Long settingPeriod = chainConfig.getSettlePeriodBlockCount().longValue();
        if (blockNumber % settingPeriod == 0) {
            logger.debug("结算周期切换块号:{}", blockNumber);
            try {
                BaseResponse <List <Node>> result;
                // ==================================更新前一周期验证人列表=======================================
                // 入参区块号属于前一结算周期，因此可以通过它查询前一结算周期验证人历史列表
                BigInteger prevEpochLastBlockNumber = BigInteger.valueOf(blockNumber);
                result = client.getHistoryVerifierList(prevEpochLastBlockNumber);
                if (!result.isStatusOk()) {
                    throw new CandidateException("【底层出错】底层链查询块号在【"+prevEpochLastBlockNumber+"】的历史结算周期验证节点列表出错,请检查Agent共识周期块数设置是否与链一致:"+result.errMsg);
                }else{
                    preVerifier.clear();
                    result.data.stream().filter(Objects::nonNull).forEach(node -> preVerifier.put(HexTool.prefix(node.getNodeId()), node));
                }

                // ==================================更新当前周期验证人列表=======================================
                BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(blockNumber+1);
                result = client.getHistoryVerifierList(nextEpochFirstBlockNumber);
                if (!result.isStatusOk()) {
                    // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
                    result = client.getNodeContract().getVerifierList().send();
                    if(!result.isStatusOk()){
                        throw new CandidateException("【底层出错】底层链查询实时结算周期验证节点列表出错:"+result.errMsg);
                    }
                }
                curVerifier.clear();
                result.data.stream().filter(Objects::nonNull).forEach(node -> curVerifier.put(HexTool.prefix(node.getNodeId()), node));
            } catch (Exception e) {
                logger.error("更新周期验证人列表失败,原因：{}", e.getMessage());
            }
            if(curVerifier.size()==0){
                throw new CandidateException("查询不到结算周期验证人(当前块号="+blockNumber+",当前结算轮数="+bc.getCurSettingEpoch()+")");
            }
        }
    }

    /**
     * 更新node表中的节点出块数信息: stat_block_qty, 由blockChain.execute()调用
     */
    public void updateNodeStatBlockQty(){
        CustomBlock curBlock = bc.getCurBlock();
        try {
            CustomNode node = NODE_CACHE.getNode(curBlock.getNodeId());
            node.setStatBlockQty(node.getStatBlockQty()+1);
            STAGE_DATA.getStakingStage().updateNode(node);
        } catch (NoSuchBeanException e) {
            logger.error("{}",e.getMessage());
        }
    }

    /**
     * 更新质押中与区块相关的信息
     */
    public void updateStakingRelative() {
        CustomBlock curBlock = bc.getCurBlock();
        Node node = curValidator.get(curBlock.getNodeId());
        if(node!=null){
            try {
                CustomNode customNode = NODE_CACHE.getNode(curBlock.getNodeId());
                CustomStaking customStaking = customNode.getLatestStaking();
                if(customStaking.getIsConsensus()== CustomStaking.YesNoEnum.YES.code){
                    // 当前共识周期出块奖励
                    BigDecimal curConsBlockReward = new BigDecimal(customStaking.getBlockRewardValue()).add(bc.getBlockReward());
                    customStaking.setBlockRewardValue(curConsBlockReward.toString());

                    // 节点出块数加1
                    customStaking.setCurConsBlockQty(customStaking.getCurConsBlockQty()+1);
                    // 把更改后的内容暂存至待更新列表
                    STAGE_DATA.getStakingStage().updateStaking(customStaking);
                }
            } catch (NoSuchBeanException e) {
                logger.error("找不到符合条件的质押信息:{}",e.getMessage());
            }
        }
    }

    /**
     * 更新区块中相关信息
     */
    public void updateBlockRelative() {
        CustomBlock curBlock = bc.getCurBlock();
        curBlock.setBlockReward(bc.getBlockReward().toString());
    }


}
