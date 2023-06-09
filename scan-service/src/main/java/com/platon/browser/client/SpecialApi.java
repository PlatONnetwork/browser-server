package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.abi.solidity.datatypes.Utf8String;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.browser.bean.*;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.utils.HexUtil;
import com.platon.browser.v0150.bean.AdjustParam;
import com.platon.contracts.ppos.BaseContract;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.Response;
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.utils.JSONUtil;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/31 11:00
 * @Description:
 */
@Slf4j
@Component
public class SpecialApi {


    /**
     * 查询结算周期历史验证人队列
     */
    public static final int GET_HISTORY_VERIFIER_LIST_FUNC_TYPE = 1300;

    /**
     * 查询历史共识周期的验证人列
     */
    public static final int GET_HISTORY_VALIDATOR_LIST_FUNC_TYPE = 1301;

    /**
     * 历史低出块处罚信息
     */
    public static final int GET_HISTORY_LOW_RATE_SLASH_LIST_FUNC_TYPE = 1304;

    /**
     * 查询版本列表
     */
    public static final int GET_NODE_VERSION = 1302;

    /**
     * 查询版本列表
     */
    public static final int GET_HISTORY_REWARD = 1303;

    /**
     * 获取可用和锁仓余额
     */
    public static final int GET_RESTRICTING_BALANCE_FUNC_TYPE = 4200;

    /**
     * 获取提案结果
     */
    public static final int GET_PROPOSAL_RES_FUNC_TYPE = 2105;

    /**
     * 查询合约调用PPOS信息
     */
    public static final int GET_PPOS_INFO_FUNC_TYPE = 1305;

    /**
     * 查询质押委托调账信息
     */
    public static final int GET_STAKING_DELEGATE_ADJUST_DATA_FUNC_TYPE = 1112;

    private static final String BLANK_RES = "结果为空!";

    /**
     * rpc调用接口
     *
     * @param web3j
     * @param function
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
    private CallResponse<String> rpc(Web3j web3j, Function function, String from, String to) throws ContractInvokeException {
        CallResponse<String> br;
        try {
            br = new RemoteCall<>(() -> {
                PlatonCall ethCall = web3j.platonCall(Transaction.createEthCallTransaction(from, to, EncoderUtils.functionEncoder(function)), DefaultBlockParameterName.LATEST).send();
                if (ethCall.hasError()) {
                    throw new ContractInvokeException(ethCall.getError().getMessage());
                }
                String value = ethCall.getValue();
                if ("0x".equals(value)) {
                    // 证明没数据,返回空响应
                    CallResponse<String> data = new CallResponse<>();
                    data.setData(null);
                    data.setErrMsg(null);
                    data.setCode(ErrorCode.SUCCESS);
                    return data;
                }
                String decodedValue = new String(Numeric.hexStringToByteArray(value));
                BaseContract.CallRet callRet = JSONUtil.parseObject(decodedValue, BaseContract.CallRet.class);
                if (callRet == null) {
                    throw new ContractCallException("Unable to convert response: " + decodedValue);
                }
                CallResponse<String> callResponse = new CallResponse<>();
                if (callRet.isStatusOk()) {
                    callResponse.setCode(callRet.getCode());
                    callResponse.setData(JSONUtil.toJSONString(callRet.getRet()));
                } else {
                    callResponse.setCode(callRet.getCode());
                    callResponse.setErrMsg(callRet.getRet().toString());
                }
                return callResponse;
            }).send();
        } catch (Exception e) {
            log.error("get rpc error", e);
            throw new ContractInvokeException(e.getMessage());
        }
        return br;
    }

    /**
     * 根据区块号获取结算周期验证人列表
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<Node> getHistoryVerifierList(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws IOException ,ContractInvokeException,BlankResponseException  {
        Request<?, NodeResult> request = new Request<>("monitor_getVerifiersByBlockNumber", Arrays.asList(blockNumber), web3jWrapper.getWeb3jService(), NodeResult.class);
        NodeResult result = request.send();
        if (null == result){
            throw new BlankResponseException(String.format("【根据区块号获取结算周期验证人列表出错】函数类型:%s,区块号:%s,返回为空!%s", "monitor_getVerifiersByBlockNumber", blockNumber, JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null){
            throw new ContractInvokeException(String.format("【根据区块号获取结算周期验证人列表出错】函数类型:%s,区块号:%s,返回数据:%s", "monitor_getVerifiersByBlockNumber", blockNumber.toString(), result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            throw new BlankResponseException(BLANK_RES);
        }
        return convertValidatorExToNode(result.getResult());
    }

    /**
     * 根据区块号获取共识周期验证人列表
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<Node> getHistoryValidatorList(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws IOException,BlankResponseException,ContractInvokeException {
        Request<?, NodeResult> request = new Request<>("monitor_getValidatorsByBlockNumber", Arrays.asList(blockNumber), web3jWrapper.getWeb3jService(), NodeResult.class);
        NodeResult result = request.send();
        if (null == result){
            throw new BlankResponseException(String.format("【根据区块号获取共识周期验证人列表】函数类型:%s,区块号:%s,返回为空!%s", "monitor_getValidatorsByBlockNumber", blockNumber, JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null){
            throw new ContractInvokeException(String.format("【根据区块号获取共识周期验证人列表】函数类型:%s,区块号:%s,返回数据:%s", "monitor_getValidatorsByBlockNumber", blockNumber.toString(), result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            throw new BlankResponseException(BLANK_RES);
        }
        return convertValidatorExToNode(result.getResult());
    }

    private List<Node> convertValidatorExToNode(List<ValidatorEx> validatorExes){
        List<Node> nodes= new ArrayList<>();
        for (ValidatorEx validatorEx :validatorExes){
            Node node = new Node();
            node.setNodeId(validatorEx.getNodeId());
            node.setStakingAddress(validatorEx.getStakingAddress());
            node.setBenifitAddress(validatorEx.getBenifitAddress());
            node.setNextRewardPer(validatorEx.getNextRewardPer());
            node.setStakingTxIndex(validatorEx.getStakingTxIndex());
            node.setProgramVersion(validatorEx.getProgramVersion());
            node.setStatus(validatorEx.getStatus());
            node.setStakingEpoch(validatorEx.getStakingEpoch());
            node.setStakingBlockNum(validatorEx.getStakingBlockNum());
            node.setShares(null == validatorEx.getShares() ? "": Numeric.encodeQuantity(validatorEx.getShares()));
            node.setReleased(null == validatorEx.getReleased() ? "": Numeric.encodeQuantity(validatorEx.getReleased()));
            node.setReleasedHes(null == validatorEx.getReleasedHes() ? "": Numeric.encodeQuantity(validatorEx.getReleasedHes()));
            node.setRestrictingPlan(null == validatorEx.getRestrictingPlan() ? "": Numeric.encodeQuantity(validatorEx.getRestrictingPlan()));
            node.setRestrictingPlanHes(null == validatorEx.getRestrictingPlanHes() ? "": Numeric.encodeQuantity(validatorEx.getRestrictingPlanHes()));
            node.setExternalId(validatorEx.getExternalId());
            node.setNodeName(validatorEx.getNodeName());
            node.setWebsite(validatorEx.getWebsite());
            node.setDetails(validatorEx.getDetails());
            node.setValidatorTerm(validatorEx.getValidatorTerm());
            node.setDelegateEpoch(validatorEx.getDelegateEpoch());
            node.setDelegateTotal(null == validatorEx.getDelegateTotal() ? "": Numeric.encodeQuantity(validatorEx.getDelegateTotal()));
            node.setDelegateRewardTotal(null == validatorEx.getDelegateRewardTotal() ? "": Numeric.encodeQuantity(validatorEx.getDelegateRewardTotal()));
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * 根据区块号获取历史低出块处罚信息列表
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<HistoryLowRateSlash> getHistoryLowRateSlashList(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws BlankResponseException, ContractInvokeException,IOException {
        Request<?, HistoryLowRateSlashResult> request = new Request<>("monitor_getSlashInfoByBlockNumber", Arrays.asList(blockNumber), web3jWrapper.getWeb3jService(), HistoryLowRateSlashResult.class);
        HistoryLowRateSlashResult result = request.send();
        if (result == null) {
            throw new BlankResponseException(String.format("【根据区块号获取历史低出块处罚信息列表出错】函数类型:%s,区块号:%s,返回为空!%s", "monitor_getSlashInfoByBlockNumber", blockNumber, JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("【根据区块号获取历史低出块处罚信息列表出错】函数类型:%s,区块号:%s,错误编码!%s,错误原因!%s", "monitor_getSlashInfoByBlockNumber", blockNumber, result.getError().getCode(),result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            // 找不到数据，返回空列表
            return Collections.emptyList();
        }
        return result.getResult();
    }

    /**
     * 根据区块号获取节点列表
     *
     * @return
     * @throws Exception
     */
    private List<Node> nodeCall(Web3j web3j, BigInteger blockNumber, int funcType) throws ContractInvokeException, BlankResponseException {

        final Function function = new Function(funcType, Collections.singletonList(new Uint256(blockNumber)));

        CallResponse<String> br = rpc(web3j, function, InnerContractAddrEnum.NODE_CONTRACT.getAddress(), InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if (br == null || br.getData() == null) {
            throw new BlankResponseException(String.format("【查询验证人出错】函数类型:%s,区块号:%s,返回为空!%s", String.valueOf(funcType), blockNumber, JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (br.isStatusOk()) {
            String data = br.getData();
            if (data == null) {
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data, Node.class);
        } else {
            String msg = JSON.toJSONString(br);
            throw new ContractInvokeException(String.format("【查询验证人出错】函数类型:%s,区块号:%s,返回数据:%s", funcType, blockNumber.toString(), msg));
        }
    }

    /**
     * 根据区块号获取节点列表
     *
     * @return
     * @throws Exception
     */
    public List<NodeVersion> getNodeVersionList(Web3jWrapper web3jWrapper) throws  BlankResponseException,ContractInvokeException,IOException {
        Request<?, NodeVersionResult> request = new Request<>("monitor_getNodeVersion", Arrays.asList(), web3jWrapper.getWeb3jService(), NodeVersionResult.class);
        NodeVersionResult result = request.send();
        if (result == null) {
            throw new BlankResponseException(String.format("【查询节点版本出错】函数类型:%s,返回为空!%s", "monitor_getNodeVersion", JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("【查询节点版本出错】函数类型:%s,错误编码!%s,错误原因!%s", "monitor_getNodeVersion", result.getError().getCode(),result.getError().getMessage()));
        }
        if (null == result.getResult()){
            throw new BlankResponseException(BLANK_RES);
        }
        return result.getResult();
    }

    /**
     * 根据账户地址获取锁仓余额
     *
     * @param addresses
     * @return
     * @throws Exception
     */
    public List<RestrictingBalance> getRestrictingBalance(Web3jWrapper web3jWrapper, String addresses) throws BlankResponseException,ContractInvokeException,IOException {
        Request<?, RestrictingBalanceResult> request = new Request<>("monitor_getAccountView", Arrays.asList(Arrays.asList(addresses)), web3jWrapper.getWeb3jService(), RestrictingBalanceResult.class);
        RestrictingBalanceResult result = request.send();
        if (result.getResult() == null) {
            throw new BlankResponseException(String.format("【查询锁仓余额出错】函数类型:%s,返回为空!%s", "monitor_getAccountView", JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("【查询锁仓余额出错】地址:%s,函数类型:%s,错误编码!%s,错误原因!%s", "monitor_getAccountView", addresses,result.getError().getCode(),result.getError().getMessage()));
        }
        if (null == result.getResult()){
            throw new BlankResponseException(BLANK_RES);
        }
        return result.getResult();
    }

    /**
     * 根据区块号获取历史周期信息
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public EpochInfo getEpochInfo(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws  BlankResponseException,ContractInvokeException,IOException {
        Request<?, EpochInfoResult> request = new Request<>("monitor_getEpochInfoByBlockNumber", Arrays.asList(blockNumber), web3jWrapper.getWeb3jService(), EpochInfoResult.class);
        Response<EpochInfo> result = request.send();
        if (result == null) {
            throw new BlankResponseException(String.format("【查询历史周期信息出错】函数类型:%s,返回为空!%s", "monitor_getEpochInfoByBlockNumber", JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("【查询历史周期信息出错】函数类型:%s,错误编码!%s,错误原因!%s", "monitor_getEpochInfoByBlockNumber", result.getError().getCode(),result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            throw new BlankResponseException(BLANK_RES);
        }
        log.info("总发行量 特殊节点 req = {}  resp = {}", blockNumber, cn.hutool.json.JSONUtil.toJsonStr(result));
        return result.getResult();
    }

    /**
     * 获取某个提案的所有参与者
     *
     * @param web3jWrapper
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
    public ProposalParticipantStat getProposalParticipants(Web3jWrapper web3jWrapper, String proposalHash, String blockHash) throws BlankResponseException,ContractInvokeException,IOException {

        Request<?, ProposalParticipantStatResult> request = new Request<>("monitor_getProposalParticipants", Arrays.asList(proposalHash,blockHash), web3jWrapper.getWeb3jService(), ProposalParticipantStatResult.class);
        ProposalParticipantStatResult result = request.send();
        if (result == null) {
            throw new BlankResponseException(String.format("查询提案参与人出错【提案Hash:%s,区块Hash:%s】", proposalHash, blockHash));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("查询提案参与人出错【提案Hash:%s,区块Hash:%s】,错误编码!%s,错误原因!%s",proposalHash, blockHash, result.getError().getCode(),result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            throw new BlankResponseException(BLANK_RES);
        }
        return result.getResult();
    }

    public ReceiptResult getReceiptResult(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws IOException,ContractInvokeException,BlankResponseException {
        Request<?, ReceiptResult> request = new Request<>("monitor_getReceiptExtsByBlockNumber", Arrays.asList(blockNumber), web3jWrapper.getWeb3jService(), ReceiptResult.class);
        ReceiptResult result = request.send();
        if (result == null) {
            throw new BlankResponseException(String.format("【根据区块号获取区块内所有交易的回执信息】函数类型:%s,区块号:%s,返回为空!%s", "monitor_getReceiptExtsByBlockNumber", blockNumber, JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("【根据区块号获取区块内所有交易的回执信息】函数类型:%s,区块号:%s,错误编码!%s,错误原因!%s", "monitor_getReceiptExtsByBlockNumber", blockNumber, result.getError().getCode(),result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            throw new BlankResponseException(BLANK_RES);
        }
        return result;
    }

    /**
     * 根据区块号获取合约调用PPOS信息
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public ImplicitPPOSTxResult getPPosInvokeInfo(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException,IOException {
        Request<?, ImplicitPPOSTxResult> request = new Request<>("monitor_getImplicitPPOSTxsByBlockNumber", Arrays.asList(blockNumber), web3jWrapper.getWeb3jService(), ImplicitPPOSTxResult.class);
        ImplicitPPOSTxResult result = request.send();
        if (result == null) {
            throw new BlankResponseException(String.format("【根据区块号获取合约调用PPOS信息】函数类型:%s,区块号:%s,返回为空!%s", "monitor_getImplicitPPOSTxsByBlockNumber", blockNumber, JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if (result.getError() != null ){
            throw new ContractInvokeException(String.format("【根据区块号获取合约调用PPOS信息】函数类型:%s,区块号:%s,错误编码!%s,错误原因!%s", "monitor_getImplicitPPOSTxsByBlockNumber", blockNumber, result.getError().getCode(),result.getError().getMessage()));
        }
        if (result.getResult() == null) {
            throw new BlankResponseException(BLANK_RES);
        }
        return result;
    }

    /**
     * 根据区块号获取质押委托调账信息
     *
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<AdjustParam> getStakingDelegateAdjustDataList(Web3j web3j, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_STAKING_DELEGATE_ADJUST_DATA_FUNC_TYPE, Collections.singletonList(new Uint256(blockNumber)));
        CallResponse<String> br = rpc(web3j, function, InnerContractAddrEnum.NODE_CONTRACT.getAddress(), InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if (br == null || br.getData() == null) {
            return Collections.EMPTY_LIST;
        }
        if (br.isStatusOk()) {
            String data = br.getData();
            if (data == null) {
                throw new BlankResponseException(BLANK_RES);
            }
            data = data.replace("delete", "delegate");
            List<AdjustParam> adjustParams = JSON.parseArray(data, AdjustParam.class);
            adjustParams.forEach(param -> {
                param.setNodeId(HexUtil.prefix(param.getNodeId()));
                param.setCurrBlockNum(blockNumber);
            });
            return adjustParams;
        } else {
            String msg = JSON.toJSONString(br);
            throw new ContractInvokeException(String.format("【查询质押委托调账信息出错】函数类型:%s,区块号:%s,返回数据:%s", GET_STAKING_DELEGATE_ADJUST_DATA_FUNC_TYPE, blockNumber.toString(), msg));
        }
    }

}
