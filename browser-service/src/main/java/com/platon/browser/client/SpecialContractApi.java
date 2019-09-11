package com.platon.browser.client;

import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.ContractInvokeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.PlatonCall;
import org.web3j.utils.JSONUtil;
import org.web3j.utils.Numeric;
import org.web3j.utils.PlatOnUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/31 11:00
 * @Description:
 */
@Component
public class SpecialContractApi {

    /**
     * 查询结算周期历史验证人队列
     */
    public static final int GET_HISTORY_VERIFIERLIST_FUNC_TYPE = 1106;
    /**
     * 查询历史共识周期的验证人列
     */
    public static final int GET_HISTORY_VALIDATORLIST_FUNC_TYPE = 1107;
    /**
     * 获取可用和锁仓余额
     */
    public static final int GET_RESTRICTINGBALANCE_FUNC_TYPE = 4101;
    /**
     * 获取提案结果
     */
    public static final int GET_PROPOSALRES_FUNC_TYPE = 2105;

    /**
     * rpc调用接口
     * @param web3j
     * @param function
     * @param blockParameter
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private BaseResponse<String> rpc(Web3j web3j,Function function,DefaultBlockParameter blockParameter,String from,String to) throws Exception {
        BaseResponse<String> br = new RemoteCall<>((Callable<BaseResponse<String>>) () -> {
            String encodedFunction = PlatOnUtil.invokeEncode(function);
            PlatonCall ethCall = web3j.platonCall(Transaction.createEthCallTransaction(from,to,encodedFunction),blockParameter).send();
            String value = ethCall.getValue();
            if("0x".equals(value)){
                // 证明没数据,返回空响应
                return new BaseResponse<>();
            }
            String decodedValue = new String(Numeric.hexStringToByteArray(value));
            return JSONUtil.parseObject(decodedValue, BaseResponse.class);
        }).send();
        return br;
    }

    /**
     * 根据区块号获取结算周期验证人列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<Node> getHistoryVerifierList(Web3j web3j, BigInteger blockNumber) throws Exception {
        List<Node> nodes = nodeCall(web3j,blockNumber,GET_HISTORY_VERIFIERLIST_FUNC_TYPE);
        return nodes;
    }

    /**
     * 根据区块号获取共识周期验证人列表
     * @param blockNumber
     * @return
     * @throws Exception
     */
    public List<Node> getHistoryValidatorList(Web3j web3j,BigInteger blockNumber) throws Exception {
        List<Node> nodes = nodeCall(web3j,blockNumber,GET_HISTORY_VALIDATORLIST_FUNC_TYPE);
        return nodes;
    }

    /**
     * 根据区块号获取节点列表
     * @return
     * @throws Exception
     */
	private List<Node> nodeCall(Web3j web3j,BigInteger blockNumber,int funcType) throws Exception {
        final Function function = new Function(
            funcType,
            Collections.singletonList(new Uint256(blockNumber)),
            Collections.singletonList(new TypeReference<Utf8String>() {})
        );
        BaseResponse<String> br = rpc(web3j,function,DefaultBlockParameter.valueOf(blockNumber),InnerContractAddrEnum.NODE_CONTRACT.address,InnerContractAddrEnum.NODE_CONTRACT.address);
        if(br==null||br.data==null){
            throw new ContractInvokeException("查询历史节点合约出错: 入参(blockNumber="+blockNumber+",funcType="+funcType);
        }
        if(br.isStatusOk()){
            String data = br.data;
            if(StringUtils.isBlank(data)){
                throw new ContractInvokeException("结果为空!");
            }
            data = data.replace("\"Shares\":null","\"Shares\":\"0x0\"");
            List<Node> result = JSONUtil.parseArray(data, Node.class);
            return result;
        }else{
            throw new ContractInvokeException("调用合约失败:"+br.errMsg);
        }
    }

    /**
     * 根据账户地址获取锁仓余额
     * @param addresses
     * @return
     * @throws Exception
     */
    public List<RestrictingBalance> getRestrictingBalance(Web3j web3j, String addresses) throws Exception {
        final Function function = new Function(
            GET_RESTRICTINGBALANCE_FUNC_TYPE,
            Arrays.asList(new Utf8String(addresses)),
            Collections.emptyList()
        );
        BaseResponse<String> br = rpc(web3j,function,DefaultBlockParameterName.LATEST,InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address,InnerContractAddrEnum.RESTRICTING_PLAN_CONTRACT.address);
        if(br==null||br.data==null){
            throw new ContractInvokeException("查询锁仓计划合约出错: 入参(addresses="+addresses);
        }
        if(br.isStatusOk()){
            String data = br.data;
            if(StringUtils.isBlank(data)){
                throw new ContractInvokeException("结果为空!");
            }
            data = data.replace("\"lockBalance\":null","\"lockBalance\":\"0x0\"");
            data = data.replace("\"pledgeBalance\":null","\"pledgeBalance\":\"0x0\"");
            List<RestrictingBalance> result = JSONUtil.parseArray(data, RestrictingBalance.class);
            return result;
        }else{
            throw new ContractInvokeException("调用合约失败:"+br.errMsg);
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
	public ProposalParticiantStat getProposalParticipants ( Web3j web3j, String proposalHash, String blockHash)throws Exception{
        final Function function = new Function(
            GET_PROPOSALRES_FUNC_TYPE,
            Arrays.asList(new BytesType(Numeric.hexStringToByteArray(proposalHash)),
            new BytesType(Numeric.hexStringToByteArray(blockHash))),
            Collections.emptyList()
        );

        BaseResponse<String> br = rpc(web3j,function,DefaultBlockParameterName.LATEST,InnerContractAddrEnum.PROPOSAL_CONTRACT.address,InnerContractAddrEnum.PROPOSAL_CONTRACT.address);
        if(br==null||br.data==null){
            throw new ContractInvokeException("查询提案参与人出错: 入参(proposalHash="+proposalHash+",blockHash="+blockHash);
        }
        if(br.isStatusOk()){
            String data = br.data;
            if(StringUtils.isBlank(data)){
                throw new ContractInvokeException("结果为空!");
            }
            String[] a = data.replace("[","").replace("]","").split(",");
            if (a.length<4) throw new ContractInvokeException("返回数据不完整!");
            String voterCount=a[0].trim(),supportCount=a[1].trim(),opposeCount=a[2].trim(),abstainCount=a[3].trim();
            ProposalParticiantStat pps = new ProposalParticiantStat();
            pps.setVoterCount(Long.parseLong(voterCount));
            pps.setSupportCount(Long.parseLong(supportCount));
            pps.setOpposeCount(Long.parseLong(opposeCount));
            pps.setAbstainCount(Long.parseLong(abstainCount));
            return pps;
        }else{
            throw new ContractInvokeException("调用合约失败:"+br.errMsg);
        }
    }
}
