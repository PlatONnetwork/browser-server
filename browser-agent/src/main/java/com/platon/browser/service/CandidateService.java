package com.platon.browser.service;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.CandidateException;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/6 16:21
 * @Description:
 */
@Service
public class CandidateService {

    @Autowired
    private BlockChain blockChain;

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatonClient client;

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
}
