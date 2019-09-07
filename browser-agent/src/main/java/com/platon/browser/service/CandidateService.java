package com.platon.browser.service;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.platon.browser.engine.BlockChain.NODE_NAME_MAP;
import static java.lang.String.format;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 16:21
 * @Description: 候选人、共识周期验证人、结算周期验证人服务类
 */
@Service
public class CandidateService {
    private static Logger logger = LoggerFactory.getLogger(CandidateService.class);
    @Autowired
    private BlockChain blockChain;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatonClient client;
    @Autowired
    private DbService dbService;

    /**
     * 从指定区块号初始化BlockChain的共识周期验证人
     * @param blockNumber
     * @throws BlockNumberException
     * @throws CandidateException
     */
    public void initValidator(Long blockNumber) throws BlockNumberException, CandidateException {
        BaseResponse<List <Node>> result;
        // 取入参区块号的前一共识周期结束块号，因此可以通过它查询前一共识周期验证人历史列表
        Long prevEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(blockNumber,chainConfig.getConsensusPeriodBlockCount().longValue());
        try {
            result = SpecialContractApi.getHistoryValidatorList(client.getWeb3j(),BigInteger.valueOf(prevEpochLastBlockNumber));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询前轮共识验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",prevEpochLastBlockNumber,e.getMessage()));
        }
        if (!result.isStatusOk()) {
            throw new CandidateException(format("【查询前轮共识验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",prevEpochLastBlockNumber,result.errMsg));
        }else{
            blockChain.getPreValidator().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> blockChain.getPreValidator().put(HexTool.prefix(node.getNodeId()), node));
        }

        // ==================================更新当前周期验证人列表=======================================
        BigInteger nextEpochLastBlockNumber = BigInteger.valueOf(prevEpochLastBlockNumber+1);
        try {
            result = SpecialContractApi.getHistoryValidatorList(client.getWeb3j(),nextEpochLastBlockNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询当前共识周期验证人-底层出错】查询块号在【%s】的共识周期验证人历史出错:%s]",nextEpochLastBlockNumber,e.getMessage()));
        }
        if (!result.isStatusOk()) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                result = client.getNodeContract().getValidatorList().send();
            } catch (Exception e) {
                e.printStackTrace();
                throw new CandidateException(format("【查询当前共识验证人-底层出错】查询实时共识周期验证人出错:%s",e.getMessage()));
            }
            if(!result.isStatusOk()){
                throw new CandidateException(format("【查询当前共识验证人-底层出错】查询实时共识周期验证人出错:%s",result.errMsg));
            }
        }
        blockChain.getCurValidator().clear();
        result.data.stream().filter(Objects::nonNull).forEach(node -> blockChain.getCurValidator().put(HexTool.prefix(node.getNodeId()), node));

        if(blockChain.getCurValidator().size()==0){
            throw new CandidateException("查询不到共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+blockChain.getCurConsensusEpoch()+")");
        }
    }

    /**
     * 从指定区块号初始化BlockChain的结算周期验证人
     * @param blockNumber
     * @throws CandidateException
     * @throws BlockNumberException
     */
    public void initVerifier (Long blockNumber) throws CandidateException, BlockNumberException {
        BaseResponse<List<Node>> result;
        // ==================================更新前一周期验证人列表=======================================
        // 入参区块号属于前一结算周期，因此可以通过它查询前一结算周期验证人历史列表
        Long prevEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(blockNumber,chainConfig.getSettlePeriodBlockCount().longValue());
        try {
            result = SpecialContractApi.getHistoryVerifierList(client.getWeb3j(),BigInteger.valueOf(prevEpochLastBlockNumber));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询前轮结算验证人-底层出错】查询块号在【%s】的结算周期验证人历史出错:%s]",prevEpochLastBlockNumber,e.getMessage()));
        }
        if (!result.isStatusOk()) {
            throw new CandidateException(format("【查询前轮结算验证人-底层出错】查询块号在【%s】的结算周期验证人历史出错:%s]",prevEpochLastBlockNumber,result.errMsg));
        }else{
            blockChain.getPreVerifier().clear();
            result.data.stream().filter(Objects::nonNull).forEach(node -> blockChain.getPreVerifier().put(HexTool.prefix(node.getNodeId()), node));
        }

        // ==================================更新当前周期验证人列表=======================================
        BigInteger nextEpochLastBlockNumber = BigInteger.valueOf(prevEpochLastBlockNumber+1);
        try {
            result = SpecialContractApi.getHistoryVerifierList(client.getWeb3j(),nextEpochLastBlockNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CandidateException(format("【查询当前结算验证人-底层出错】查询块号在【%s】的结算周期验证人历史出错:%s]",nextEpochLastBlockNumber,e.getMessage()));
        }
        if (!result.isStatusOk()) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                result = client.getNodeContract().getVerifierList().send();
            } catch (Exception e) {
                throw new CandidateException(format("【查询当前结算验证人-底层出错】查询实时结算周期验证人出错:%s",e.getMessage()));
            }
            if(!result.isStatusOk()){
                throw new CandidateException(format("【查询当前结算验证人-底层出错】查询实时结算周期验证人出错:%s",result.errMsg));
            }
        }
        blockChain.getCurVerifier().clear();
        result.data.stream().filter(Objects::nonNull).forEach(node -> blockChain.getCurVerifier().put(HexTool.prefix(node.getNodeId()), node));

        if(blockChain.getCurVerifier().size()==0){
            throw new CandidateException("查询不到结算周期验证人(当前块号="+blockNumber+",当前结算轮数="+blockChain.getCurSettingEpoch()+")");
        }
    }

    /**
     * 从区块号0初始化BlockChain的共识周期验证人和结算周期验证人
     * @throws Exception
     */
    public void init() throws Exception {
        // 如果库里区块为空，则：
        try {
            // 根据区块号0查询共识周期验证人，以便对结算周期验证人设置共识标识
            BaseResponse<List<Node>> result = SpecialContractApi.getHistoryValidatorList(client.getWeb3j(),BigInteger.ZERO);
            if(!result.isStatusOk()){
                logger.debug("查询实时共识周期验证人列表...");
                result = client.getNodeContract().getValidatorList().send();
                if(!result.isStatusOk()){
                    throw new CandidateException("底层链查询实时共识周期验证节点列表出错:"+result.errMsg);
                }
            }
            // 查询内置共识周期验证人初始化blockChain的curValidator属性
            Set<String> validatorSet = new HashSet<>();
            result.data.forEach(node->validatorSet.add(HexTool.prefix(node.getNodeId())));

            // 查询所有候选人
            Map<String,Node> candidateMap = new HashMap<>();
            result = client.getNodeContract().getCandidateList().send();
            if(!result.isStatusOk()){
                throw new CandidateException("底层链查询候选验证节点列表出错:"+result.errMsg);
            }
            result.data.forEach(node->candidateMap.put(HexTool.prefix(node.getNodeId()),node));

            // 配置中的默认内置节点信息
            Map<String,CustomStaking> defaultStakingMap = new HashMap<>();
            chainConfig.getDefaultStakings().forEach(staking -> defaultStakingMap.put(staking.getNodeId(),staking));

            // 根据区块号0查询结算周期验证人列表并入库
            result = SpecialContractApi.getHistoryVerifierList(client.getWeb3j(),BigInteger.ZERO);
            if(!result.isStatusOk()){
                logger.debug("查询实时结算周期验证人列表...");
                result = client.getNodeContract().getVerifierList().send();
                if(!result.isStatusOk()){
                    throw new CandidateException("底层链查询实时结算周期验证节点列表出错:"+result.errMsg);
                }
            }

            result.data.stream().filter(Objects::nonNull).forEach(verifier->{
                Node candidate = candidateMap.get(HexTool.prefix(verifier.getNodeId()));
                // 补充完整属性
                if(candidate!=null) BeanUtils.copyProperties(candidate,verifier);

                CustomNode node = new CustomNode();
                node.updateWithNode(verifier);
                node.setIsRecommend(CustomNode.YesNoEnum.YES.code);
                node.setStatVerifierTime(BigInteger.ONE.intValue()); // 提前设置验证轮数
                node.setStatExpectBlockQty(chainConfig.getExpectBlockCount().longValue());
                BlockChain.STAGE_DATA.getStakingStage().insertNode(node);

                CustomStaking staking = new CustomStaking();
                staking.updateWithNode(verifier);
                staking.setStatVerifierTime(BigInteger.ONE.intValue()); // 提前设置验证轮数
                staking.setIsInit(CustomStaking.YesNoEnum.YES.code);
                staking.setIsSetting(CustomStaking.YesNoEnum.YES.code);
                // 内置节点默认设置状态为1
                staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.code);
                // 设置内置节点质押锁定金额
                BigDecimal initStakingLocked = Convert.toVon(chainConfig.getDefaultStakingLockedAmount(), Convert.Unit.LAT);
                staking.setStakingLocked(initStakingLocked.toString());
                // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
                if(validatorSet.contains(node.getNodeId())) staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
                staking.setIsSetting(CustomStaking.YesNoEnum.YES.code);

                CustomStaking defaultStaking = defaultStakingMap.get(staking.getNodeId());
                if(StringUtils.isBlank(staking.getStakingName())&&defaultStaking!=null)
                    staking.setStakingName(defaultStaking.getStakingName());

                // 暂存至新增质押待入库列表
                BlockChain.STAGE_DATA.getStakingStage().insertStaking(staking);

                // 更新节点名称映射缓存
                NODE_NAME_MAP.put(staking.getNodeId(),staking.getStakingName());
            });
            BlockChainStage bcr = blockChain.exportResult();
            dbService.batchSave(Collections.emptyList(),bcr);
            blockChain.commitResult();

            // 通知质押引擎重新初始化节点缓存
            blockChain.getStakingExecute().loadNodes();
        } catch (IOException | CacheConstructException | BusinessException e) {
            throw new CandidateException("查询内置初始验证人列表失败："+e.getMessage());
        }
    }
}
