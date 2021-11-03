package com.platon.browser.client;

import com.alibaba.fastjson.JSON;
import com.platon.abi.solidity.datatypes.BytesType;
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
import com.platon.protocol.core.methods.request.Transaction;
import com.platon.protocol.core.methods.response.PlatonCall;
import com.platon.tx.exceptions.ContractCallException;
import com.platon.utils.JSONUtil;
import com.platon.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
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
    public static final int GET_HISTORY_VERIFIER_LIST_FUNC_TYPE = 1106;
    /**
     * 查询历史共识周期的验证人列
     */
    public static final int GET_HISTORY_VALIDATOR_LIST_FUNC_TYPE = 1107;
    /**
     * 历史低出块处罚信息
     */
    public static final int GET_HISTORY_LOW_RATE_SLASH_LIST_FUNC_TYPE = 1110;
    /**
     * 查询版本列表
     */
    public static final int GET_NODE_VERSION = 1108;
    /**
     * 查询版本列表
     */
    public static final int GET_HISTORY_REWARD = 1109;
    /**
     * 获取可用和锁仓余额
     */
    public static final int GET_RESTRICTING_BALANCE_FUNC_TYPE = 4101;
    /**
     * 获取提案结果
     */
    public static final int GET_PROPOSAL_RES_FUNC_TYPE = 2105;
    /**
     * 查询合约调用PPOS信息
     */
    public static final int GET_PPOS_INFO_FUNC_TYPE = 1111;
    /**
     * 查询质押委托调账信息
     */
    public static final int GET_STAKING_DELEGATE_ADJUST_DATA_FUNC_TYPE = 1112;

    private static final String BLANK_RES = "结果为空!";

    /**
     * rpc调用接口
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
                PlatonCall ethCall = web3j.platonCall(
                        Transaction.createEthCallTransaction(from, to, EncoderUtils.functionEncoder(function)),
                        DefaultBlockParameterName.LATEST)
                        .send();
                if(ethCall.hasError()) {
                    throw new ContractInvokeException(ethCall.getError().getMessage());
                }
                String value = ethCall.getValue();
                if("0x".equals(value)){
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
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<Node> getHistoryVerifierList(Web3j web3j, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        return nodeCall(web3j,blockNumber,GET_HISTORY_VERIFIER_LIST_FUNC_TYPE);
    }

    /**
     * 根据区块号获取共识周期验证人列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<Node> getHistoryValidatorList(Web3j web3j,BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        return nodeCall(web3j,blockNumber,GET_HISTORY_VALIDATOR_LIST_FUNC_TYPE);
    }

    /**
     * 根据区块号获取历史低出块处罚信息列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<HistoryLowRateSlash> getHistoryLowRateSlashList(Web3j web3j, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_HISTORY_LOW_RATE_SLASH_LIST_FUNC_TYPE, Collections.singletonList(new Uint256(blockNumber)));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.NODE_CONTRACT.getAddress(),InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if(br==null){
            throw new BlankResponseException(String.format("【查询历史低出块处罚信息出错】函数类型:%s,区块号:%s,返回为空!%s",String.valueOf(GET_HISTORY_LOW_RATE_SLASH_LIST_FUNC_TYPE),blockNumber,JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if(br.getData()==null){
            // 找不到数据，返回空列表
            return Collections.emptyList();
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data,HistoryLowRateSlash.class);
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询历史低出块处罚信息出错】函数类型:%s,区块号:%s,返回数据:%s",GET_HISTORY_LOW_RATE_SLASH_LIST_FUNC_TYPE,blockNumber.toString(),msg));
        }
    }

    /**
     * 根据区块号获取节点列表
     * @return
     * @throws Exception
     */
	private List<Node> nodeCall(Web3j web3j,BigInteger blockNumber,int funcType) throws ContractInvokeException, BlankResponseException {

        final Function function = new Function(funcType, Collections.singletonList(new Uint256(blockNumber)));

        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.NODE_CONTRACT.getAddress(),InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            throw new BlankResponseException(String.format("【查询验证人出错】函数类型:%s,区块号:%s,返回为空!%s",String.valueOf(funcType),blockNumber,JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data,Node.class);
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询验证人出错】函数类型:%s,区块号:%s,返回数据:%s",funcType,blockNumber.toString(),msg));
        }
    }

    /**
     * 根据区块号获取节点列表
     * @return
     * @throws Exception
     */
    public List<NodeVersion> getNodeVersionList(Web3j web3j) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_NODE_VERSION, Collections.emptyList());
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.NODE_CONTRACT.getAddress(),InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            throw new BlankResponseException(String.format("【查询节点版本出错】函数类型:%s,返回为空!%s",GET_NODE_VERSION,JSON.toJSONString(Thread.currentThread().getStackTrace())));
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data,NodeVersion.class);
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询节点版本出错】函数类型:%s,返回数据:%s",GET_NODE_VERSION,msg));
        }
    }

    /**
     * 根据账户地址获取锁仓余额
     * @param addresses
     * @return
     * @throws Exception
     */
    public List<RestrictingBalance> getRestrictingBalance(Web3j web3j, String addresses) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_RESTRICTING_BALANCE_FUNC_TYPE,Collections.singletonList(new Utf8String(addresses)));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress(),InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            throw new BlankResponseException(String.format("查询锁仓余额出错【addresses:%s)】,返回为空!",addresses));
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data,RestrictingBalance.class);
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询锁仓余额出错】地址:%s,返回数据:%s",addresses,msg));
        }
    }

    /**
     * 根据区块号获取历史周期信息
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public EpochInfo getEpochInfo(Web3j web3j, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_HISTORY_REWARD,Collections.singletonList(new Uint256(blockNumber)));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.NODE_CONTRACT.getAddress(),InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            throw new BlankResponseException(String.format("查询历史周期信息出错【blockNumber:%s)】,返回为空!",blockNumber));
        }

        log.info("总发行量 特殊节点 req = {}  resp = {}", blockNumber,  cn.hutool.json.JSONUtil.toJsonStr(br));

        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            EpochInfo ei= JSON.parseObject(data,EpochInfo.class);
            if(ei.getYearEndNum().compareTo(ei.getYearStartNum())<0){
                String msg = "查询历史周期信息出错【blockNumber:"+blockNumber+")】:增发周期结束区块号【"+ei.getYearEndNum()+"】<开始区块号【"+ei.getYearStartNum()+"】";
                log.error(msg);
                throw new ContractInvokeException(msg);
            }
            return ei;
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询历史周期信息出错】区块号:%s,返回数据:%s",blockNumber,msg));
        }
    }

    /**
     * 获取某个提案的所有参与者
     * @param web3j
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
	public ProposalParticipantStat getProposalParticipants (Web3j web3j, String proposalHash, String blockHash) throws ContractInvokeException, BlankResponseException {

        final Function function = new Function(GET_PROPOSAL_RES_FUNC_TYPE,Arrays.asList(new BytesType(Numeric.hexStringToByteArray(proposalHash)),new BytesType(Numeric.hexStringToByteArray(blockHash))));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.PROPOSAL_CONTRACT.getAddress(),InnerContractAddrEnum.PROPOSAL_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            throw new BlankResponseException(String.format("查询提案参与人出错【提案Hash:%s,区块Hash:%s】",proposalHash,blockHash));
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            String[] a = data.replace("[","").replace("]","").split(",");
            String voterCount="0";
            String supportCount="0";
            String opposeCount="0";
            String abstainCount="0";
            if (a.length>=4){
                voterCount=a[0].trim();
                supportCount=a[1].trim();
                opposeCount=a[2].trim();
                abstainCount=a[3].trim();
            }
            ProposalParticipantStat pps = new ProposalParticipantStat();
            pps.setVoterCount(Long.parseLong(voterCount));
            pps.setSupportCount(Long.parseLong(supportCount));
            pps.setOpposeCount(Long.parseLong(opposeCount));
            pps.setAbstainCount(Long.parseLong(abstainCount));
            return pps;
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询提案参与者出错】提案Hash:%s,区块Hash:%s,返回数据:%s",proposalHash,blockHash,msg));
        }
    }

    public ReceiptResult getReceiptResult(Web3jWrapper web3jWrapper, BigInteger blockNumber) throws IOException {
        Request<?, ReceiptResult> request = new Request<>(
                "platon_getTransactionByBlock",
                Arrays.asList(blockNumber),
                web3jWrapper.getWeb3jService(),
                ReceiptResult.class);
        return request.send();
    }

    /**
     * 根据区块号获取合约调用PPOS信息
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<PPosInvokeContractInput> getPPosInvokeInfo(Web3j web3j, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_PPOS_INFO_FUNC_TYPE, Collections.singletonList(new Uint256(blockNumber)));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.NODE_CONTRACT.getAddress(),InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            return Collections.EMPTY_LIST;
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            return JSON.parseArray(data, PPosInvokeContractInput.class);
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询PPOS调用信息出错】函数类型:%s,区块号:%s,返回数据:%s",GET_PPOS_INFO_FUNC_TYPE,blockNumber.toString(),msg));
        }
    }

    /**
     * 根据区块号获取质押委托调账信息
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<AdjustParam> getStakingDelegateAdjustDataList(Web3j web3j, BigInteger blockNumber) throws ContractInvokeException, BlankResponseException {
        final Function function = new Function(GET_STAKING_DELEGATE_ADJUST_DATA_FUNC_TYPE, Collections.singletonList(new Uint256(blockNumber)));
        CallResponse<String> br = rpc(web3j,function,InnerContractAddrEnum.NODE_CONTRACT.getAddress(),InnerContractAddrEnum.NODE_CONTRACT.getAddress());
        if(br==null||br.getData()==null){
            return Collections.EMPTY_LIST;
        }
        if(br.isStatusOk()){
            String data = br.getData();
            if(data==null){
                throw new BlankResponseException(BLANK_RES);
            }
            data = data.replace("delete","delegate");
            List<AdjustParam> adjustParams = JSON.parseArray(data, AdjustParam.class);
            adjustParams.forEach(param->{
                param.setNodeId(HexUtil.prefix(param.getNodeId()));
                param.setCurrBlockNum(blockNumber);
            });
            return adjustParams;
        }else{
            String msg = JSON.toJSONString(br,true);
            throw new ContractInvokeException(String.format("【查询质押委托调账信息出错】函数类型:%s,区块号:%s,返回数据:%s",GET_STAKING_DELEGATE_ADJUST_DATA_FUNC_TYPE,blockNumber.toString(),msg));
        }
    }
}
