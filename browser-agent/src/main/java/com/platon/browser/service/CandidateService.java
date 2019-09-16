package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.bean.AnnualizedRateInfo;
import com.platon.browser.engine.bean.PeriodValueElement;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import lombok.Data;
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
import java.util.concurrent.TimeUnit;

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
    private SpecialContractApi sca;

    @Data
    public static class CandidateResult{
        private List<Node> pre;
        private List<Node> cur;
    }

    @Data
    public static class InitParam{
        private List<CustomNode> nodes=new ArrayList<>();
        private List<CustomStaking> stakings=new ArrayList<>();
    }

    /**
     * 从指定区块号初始化BlockChain的结算周期验证人
     * @param blockNumber
     * @throws CandidateException
     * @throws BlockNumberException
     */
    public CandidateResult getVerifiers(Long blockNumber) throws CandidateException, BlockNumberException {
        CandidateResult cr = new CandidateResult();
        // ==================================更新前一周期验证人列表=======================================
        // 入参区块号属于前一结算周期，因此可以通过它查询前一结算周期验证人历史列表
        Long prevEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(blockNumber,chainConfig.getSettlePeriodBlockCount().longValue());
        while (true) try {
            cr.pre = sca.getHistoryVerifierList(client.getWeb3j(), BigInteger.valueOf(prevEpochLastBlockNumber));
            logger.debug("前一轮结算周期(未块:{})验证人:{}", prevEpochLastBlockNumber, JSON.toJSONString(cr.pre, true));
            break;
        } catch (Exception e) {
            logger.error("【查询前轮结算验证人-底层出错】使用块号【{}】查询结算周期验证人出错,将重试:{}", prevEpochLastBlockNumber, e.getMessage());
        }

        // ==================================更新当前周期验证人列表=======================================
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(prevEpochLastBlockNumber+1);
        blockChain.getCurVerifier().clear();

        while (true) try {
            cr.cur = sca.getHistoryVerifierList(client.getWeb3j(), nextEpochFirstBlockNumber);
            logger.debug("下一轮结算周期验证人(始块:{}):{}", nextEpochFirstBlockNumber, JSON.toJSONString(cr.cur, true));
            break;
        } catch (Exception e) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                cr.cur = getCurVerifiers();
                logger.debug("下一轮结算周期验证人(实时):{}", JSON.toJSONString(cr.cur, true));
                break;
            } catch (Exception e1) {
                logger.error("【查询当前结算验证人-底层出错】查询实时结算周期验证人出错,将重试:{}", e1.getMessage());
            }
        }

        if(cr.cur.size()==0){
            throw new CandidateException("查询不到结算周期验证人(当前块号="+blockNumber+",当前结算轮数="+blockChain.getCurSettingEpoch()+")");
        }
        return cr;
    }

    /**
     * 从指定区块号初始化BlockChain的共识周期验证人
     * @param blockNumber
     * @throws BlockNumberException
     * @throws CandidateException
     */
    public CandidateResult getValidators(Long blockNumber) throws BlockNumberException, CandidateException {
        CandidateResult cr = new CandidateResult();
        Long prevEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(blockNumber,chainConfig.getConsensusPeriodBlockCount().longValue());
        while (true) try {
            cr.pre = sca.getHistoryValidatorList(client.getWeb3j(), BigInteger.valueOf(prevEpochLastBlockNumber));
            logger.debug("前一轮共识周期(未块:{})验证人:{}", prevEpochLastBlockNumber, JSON.toJSONString(cr.pre, true));
            break;
        } catch (Exception e) {
            logger.error("【查询前轮共识验证人-底层出错】查询块号在【{}】的共识周期验证人历史出错,将重试:{}]", prevEpochLastBlockNumber, e.getMessage());
        }

        // ==================================更新当前周期验证人列表=======================================
        BigInteger nextEpochFirstBlockNumber = BigInteger.valueOf(prevEpochLastBlockNumber+1);

        while (true) try {
            cr.cur = sca.getHistoryValidatorList(client.getWeb3j(), nextEpochFirstBlockNumber);
            logger.debug("下一轮共识周期验证人(始块:{}):{}", nextEpochFirstBlockNumber, JSON.toJSONString(cr.cur, true));
            break;
        } catch (Exception e) {
            // 如果取不到节点列表，证明agent已经追上链，则使用实时接口查询节点列表
            try {
                cr.cur = getCurValidators();
                logger.debug("下一轮共识周期验证人(实时):{}", JSON.toJSONString(cr.cur, true));
                break;
            } catch (Exception e1) {
                logger.error("【查询当前共识验证人-底层出错】查询实时共识周期验证人出错,将重试:{}", e1.getMessage());
            }
        }

        if(cr.cur.size()==0){
            throw new CandidateException("查询不到共识周期验证人(当前块号="+blockNumber+",当前共识轮数="+blockChain.getCurConsensusEpoch()+")");
        }
        return cr;
    }

    /**
     * 从区块号0初始化BlockChain的共识周期验证人和结算周期验证人
     * @throws Exception
     */
    public InitParam getInitParam() throws Exception {
        InitParam initParam = new InitParam();
        // 如果库里区块为空，则：
        try {
            /* 根据区块号0查询共识周期验证人，以便对结算周期验证人设置共识标识 */
            List<Node> validators;
            try {
                validators = sca.getHistoryValidatorList(client.getWeb3j(), BigInteger.ZERO);
                logger.debug("共识周期(未块:{})验证人:{}", BigInteger.ZERO, JSON.toJSONString(validators, true));
            } catch (Exception e) {
                validators=getCurValidators();
            }
            Set<String> validatorSet = new HashSet<>();
            validators.forEach(node->validatorSet.add(HexTool.prefix(node.getNodeId())));
            blockChain.updateCurConsensusExpectBlockCount(validatorSet.size());

            // 查询所有候选人
            Map<String,Node> candidateMap = new HashMap<>();
            List<Node> candidates = getCurCandidates();
            candidates.forEach(node->candidateMap.put(HexTool.prefix(node.getNodeId()),node));

            // 配置中的默认内置节点信息
            Map<String,CustomStaking> defaultStakingMap = new HashMap<>();
            chainConfig.getDefaultStakings().forEach(staking -> defaultStakingMap.put(staking.getNodeId(),staking));

            /* 根据区块号0查询结算周期验证人列表并入库 */
            List<Node> verifiers;
            try {
                verifiers = sca.getHistoryVerifierList(client.getWeb3j(),BigInteger.ZERO);
                logger.debug("结算周期(未块:{})验证人:{}",BigInteger.ZERO, JSON.toJSONString(verifiers,true));
            }catch (Exception e){
                verifiers = getCurVerifiers();
            }

            verifiers.stream().filter(Objects::nonNull).forEach(verifier->{
                Node candidate = candidateMap.get(HexTool.prefix(verifier.getNodeId()));
                // 补充完整属性
                if(candidate!=null) BeanUtils.copyProperties(candidate,verifier);

                CustomNode node = new CustomNode();
                node.updateWithNode(verifier);
                node.setIsRecommend(CustomNode.YesNoEnum.YES.code);
                node.setStatVerifierTime(BigInteger.ONE.intValue()); // 提前设置验证轮数
                node.setStatExpectBlockQty(blockChain.getCurConsensusExpectBlockCount().toString());
                initParam.nodes.add(node);

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

                // 记录年化率信息, 由于是周期开始，所以只记录成本，收益需要在结算周期切换时算
                AnnualizedRateInfo ari = new AnnualizedRateInfo();
                PeriodValueElement pve = new PeriodValueElement(BigInteger.ONE,new BigInteger(staking.getStakingLocked()));
                ari.getCost().add(pve);
                staking.setAnnualizedRateInfo(JSON.toJSONString(ari));

                initParam.stakings.add(staking);
            });
        } catch (IOException | CacheConstructException | BusinessException e) {
            throw new CandidateException("查询内置验证人列表失败："+e.getMessage());
        }
        return initParam;
    }

    /**
     * 获取实时候选人列表
     * @return
     * @throws Exception
     */
    public List<Node> getCurCandidates() throws InterruptedException {
        while (true) try {
            BaseResponse<List<Node>> br = client.getNodeContract().getCandidateList().send();
            if (!br.isStatusOk()) {
                throw new CandidateException(br.errMsg);
            }
            return br.data;
        } catch (Exception e) {
            logger.error("底层链查询候选验证节点列表出错,将重试:{}", e.getMessage());
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 获取实时结算周期验证人列表
     * @return
     * @throws Exception
     */
    public List<Node> getCurVerifiers() throws InterruptedException {
        while (true) try {
            BaseResponse<List<Node>> br = client.getNodeContract().getVerifierList().send();
            if (!br.isStatusOk()) {
                throw new CandidateException(br.errMsg);
            }
            return br.data;
        } catch (Exception e) {
            logger.error("底层链查询实时结算周期验证节点列表出错,将重试:{}", e.getMessage());
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 获取实时共识周期验证人列表
     * @return
     * @throws Exception
     */
    public List<Node> getCurValidators() throws Exception {
        while (true) try {
            BaseResponse<List<Node>> br = client.getNodeContract().getValidatorList().send();
            if (!br.isStatusOk()) {
                throw new CandidateException(br.errMsg);
            }
            return br.data;
        } catch (Exception e) {
            logger.error("底层链查询实时共识周期验证节点列表出错,将重试:{}", e.getMessage());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
