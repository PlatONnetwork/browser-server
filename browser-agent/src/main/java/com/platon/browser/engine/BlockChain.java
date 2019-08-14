package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.CustomNodeOptMapper;
import com.platon.browser.dao.mapper.CustomSlashMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dto.BlockBean;
import com.platon.browser.dto.NodeBean;
import com.platon.browser.dto.StakingBean;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:11
 * @Description:
 */
@Component
@Data
public class BlockChain {
    private static Logger logger = LoggerFactory.getLogger(BlockChain.class);

    private Map<String,Long> nodeIdToBlockCountMap = new HashMap<>();

    private BlockChainResult execResult = new BlockChainResult();
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private StakingExecute stakingExecute;
    @Autowired
    private ProposalExecute proposalExecute;
    @Autowired
    private AddressExecute addressExecute;



    @Autowired
    private PlatonClient client;

    private long curSettingEpoch;
    private long curConsensusEpoch;
    private BlockBean curBlock;
    private long transactionCount;

    // 上轮结算周期验证人
    private Map<String, NodeBean> preVerifier = new HashMap<>();
    // 当前结算周期验证人
    private Map<String, NodeBean> curVerifier = new HashMap<>();
    // 上轮共识周期验证人
    private Map<String, NodeBean> preValidator = new HashMap<>();
    // 当前共识周期验证人
    private Map<String, NodeBean> curValidator = new HashMap<>();

    @PostConstruct
    private void init(){
        /***把当前库中的验证人列表加载到内存中**/

    }

    /**
     * 执行区块
     * @param block
     */
    public void execute( BlockBean block){
        //累加区块中的交易总数
        transactionCount = transactionCount + block.getTransactionList().size();

        curBlock=block;
        //新开线程去查询rpc共识列表

        // 节点区块数统计
        Long blockCount = nodeIdToBlockCountMap.get(block.getNodeId());
        if(blockCount==null){
            blockCount=0l;
        }
        nodeIdToBlockCountMap.put(block.getNodeId(),++blockCount);

        if(block.getNumber()==1){
            // TODO: 初始化共识周期验证人列表和结算周期验证人列表，结算周期验证人列表包含了共识周期验证人列表
            // 调用节点合约获取初始验证人,并入库
            client.getNodeContract().getValidatorList();
        }


        //数据回填
        // 推算并更新共识周期和结算周期
        updateEpoch();
        // 更新共识周期验证人和结算周期验证人列表
        updateNodes();
        // 分析交易信息
        analyzeTransaction();
    }

    /**
     * 根据区块号推算并更新共识周期和结算周期轮数
     */
    private void updateEpoch(){
        // 根据区块号是否与周期整除来触发周期相关处理方法
        // 计算共识周期轮数
        curConsensusEpoch = BigDecimal.valueOf(curBlock.getNumber()).divide(BigDecimal.valueOf(chainConfig.getConsensusPeriod()),0, RoundingMode.CEILING).longValue();
        // 计算结算周期轮数
        curSettingEpoch = BigDecimal.valueOf(curBlock.getNumber()).divide(BigDecimal.valueOf(chainConfig.getSettingPeriod()),0, RoundingMode.CEILING).longValue();
    }

    /**
     * 更新共识周期验证人和结算周期验证人列表
     * // 假设当前链上最高区块号为750
     *   1         250        500        750
     *   |----------|----------|----------|
     *      A B C      A C D       B C D
     *   共识周期的临界块号分别是：1,250,500,750
     *   使用临界块号查到的验证人：1=>"A,B,C",250=>"A,B,C",500=>"A,C,D",750=>"B,C,D"
     *   如果当前区块号为753，由于未达到
     */
    private List<Staking> convertToStaking(BaseResponse<List<org.web3j.platon.bean.Node>> response){
        List<Staking> stakingList = new ArrayList<>();
        if(response.isStatusOk()){
            List<org.web3j.platon.bean.Node> validators = response.data;
            Date date = new Date();
            validators.forEach(validator->{
                StakingBean staking = new StakingBean();
                staking.initWithNode(validator);
                staking.setCreateTime(date);
                staking.setUpdateTime(date);
                staking.setIsConsensus(0);
                if(curBlock.getNumber()%chainConfig.getConsensusPeriod()==0) staking.setIsConsensus(1);
                staking.setIsSetting(0);
                if(curBlock.getNumber()%chainConfig.getSettingPeriod()==0) staking.setIsSetting(1);

                stakingList.add(staking);
            });
            logger.debug("validators: {}",validators);
        }
        return stakingList;
    }
    private void updateNodes(){
        /*if(curBlock.getNumber()%chainConfig.getConsensusPeriod()==0){
            // 进入新共识周期
            // 把curValidator引用赋给preValidator
            preValidator.clear();
            preValidator.putAll(curValidator);
            // 清除当前共识周期验证人
            curVerifier.clear();
        }else{
            if(curValidator.size()==0){
                // 直接查询实时共识验证人列表作为curValidator
                try {
                    BaseResponse<List<org.web3j.platon.bean.Node>> response = client.getNodeContract().getValidatorList().send();
                    convertToStaking(response).forEach(staking -> curValidator.put(staking.getNodeId()+staking.getStakingBlockNum(),staking));
                    logger.debug("{}",response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(curBlock.getNumber()%chainConfig.getSettingPeriod()==0){
            // 进入新结算周期
            // 把curVerifier引用赋给preVerifier
            preVerifier.clear();
            preVerifier.putAll(curVerifier);
            // 清除当前结算周期验证人
            curVerifier.clear();
        }else{
            if(curVerifier.size()==0){
                // 直接查询实时共识验证人列表作为curVerifier
                try {
                    BaseResponse<List<org.web3j.platon.bean.Node>> response = client.getNodeContract().getVerifierList().send();
                    convertToStaking(response).forEach(staking -> curVerifier.put(staking.getNodeId()+staking.getStakingBlockNum(),staking));
                    logger.debug("{}",response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    /**
     * 根据交易信息新增或更新相关记录：
     *
     */
    private void analyzeTransaction(){
        curBlock.getTransactionList().forEach(transactionInfo -> {
            // 地址相关

            // 调用交易分析引擎分析交易，以补充相关数据
            switch (transactionInfo.getTypeEnum()){
                case CREATEVALIDATOR: // 创建验证人
                case EDITVALIDATOR: // 编辑验证人
                case INCREASESTAKING: // 增持质押
                case EXITVALIDATOR: // 撤销质押
                case UNDELEGATE: // 撤销委托
                case REPORTVALIDATOR: // 举报多签验证人
                    stakingExecute.execute(transactionInfo,this);
                    StakingExecuteResult ser = stakingExecute.exportResult();
                    StakingExecuteResult serSummary = execResult.getStakingExecuteResult();
                    // 汇总添加【节点】记录
                    serSummary.getAddNodes().addAll(ser.getAddNodes());
                    // 汇总更新【节点】记录
                    serSummary.getUpdateNodes().addAll(ser.getUpdateNodes());
                    // 汇总添加【惩罚】记录
                    serSummary.getAddSlash().addAll(ser.getAddSlash());
                    // 汇总添加【节点操作】记录
                    serSummary.getAddNodeOpts().addAll(ser.getAddNodeOpts());
                    // 汇总添加【委托】记录
                    serSummary.getAddDelegations().addAll(ser.getAddDelegations());
                    // 汇总更新【委托】记录
                    serSummary.getUpdateDelegations().addAll(ser.getUpdateDelegations());
                    // 汇总添加【撤销委托】记录
                    serSummary.getAddUnDelegations().addAll(ser.getAddUnDelegations());
                    // 汇总更新【撤销委托】记录
                    serSummary.getUpdateUnDelegations().addAll(ser.getUpdateUnDelegations());
                    // 汇总添加【质押】记录
                    serSummary.getAddStakings().addAll(ser.getAddStakings());
                    // 汇总更新【质押】记录
                    serSummary.getUpdateStakings().addAll(ser.getUpdateStakings());
                    break;
                case CREATEPROPOSALTEXT: // 创建文本提案
                case CREATEPROPOSALUPGRADE: // 创建升级提案
                case CREATEPROPOSALPARAMETER: // 创建参数提案
                case VOTINGPROPOSAL: // 给提案投票
                    proposalExecute.execute(transactionInfo,this);
                    ProposalExecuteResult per = proposalExecute.exportResult();
                    ProposalExecuteResult perSummary = execResult.getProposalExecuteResult();
                    // 汇总添加【提案】记录
                    perSummary.getAddProposals().addAll(per.getAddProposals());
                    // 汇总更新【提案】记录
                    perSummary.getUpdateProposals().addAll(per.getUpdateProposals());
                    // 汇总添加【提案投票】记录
                    perSummary.getAddVotes().addAll(per.getAddVotes());
                    break;
            }
        });


        // 根据区块号是否整除周期来触发周期相关处理方法
        Long blockNumber = curBlock.getNumber();
        if(blockNumber%chainConfig.getConsensusPeriod()==0){
            // 进入新共识周期
            logger.debug("进入新共识周期：Block Number({})",blockNumber);

            // TODO: 更新共识周期验证人列表
            onNewConsEpoch();
        }

        if(blockNumber%chainConfig.getSettingPeriod()==0){
            // 进入新结算周期
            logger.debug("进入新结算周期：Block Number({})",blockNumber);

            // TODO: 更新结算周期验证人列表
            onNewSettingEpoch();
        }

        if(blockNumber%chainConfig.getElectionDistance()==0){
            // 开始验证人选举
            logger.debug("开始验证人选举：Block Number({})",blockNumber);

            // TODO: 对上一轮共识验证人进行出块率计算，并进行处罚罚款（更新对应的staking表中的金额），罚款计算公式参考底层文档
            onElectionDistance();
        }
    }



    /**
     * 导出需要入库的数据
     * @return
     */
    public BlockChainResult exportResult(){
        return execResult;
    }

    public void commitResult(){
        proposalExecute.commitResult();
        stakingExecute.commitResult();
    }

    /**
     * 进入新的结算周期
     */
    private void onNewSettingEpoch(){
        stakingExecute.onNewSettingEpoch();
    }

    /**
     * 进入新的共识周期变更
     */
    private void onNewConsEpoch(){
        stakingExecute.onNewConsEpoch();
    }

    /**
     * 进行选择验证人时触发
     */
    private void onElectionDistance(){
        stakingExecute.onElectionDistance();
    }



}
