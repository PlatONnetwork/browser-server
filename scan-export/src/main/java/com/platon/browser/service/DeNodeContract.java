package com.platon.browser.service;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;

import java.math.BigInteger;
import java.util.List;

public class DeNodeContract extends DeContract{

	protected DeNodeContract(String contractAddress, Web3j web3j) {
		super(contractAddress, web3j);
	}
	
    /**
     * 查询当前共识周期的验证人列表
     *
     * @return
     */
    public RemoteCall<CallResponse<List<Node>>> getValidatorList(BigInteger blockNumer) {
        Function function = new Function(FunctionType.GET_VALIDATORLIST_FUNC_TYPE);
        return executeRemoteCallListValueReturnDe(function, Node.class, blockNumer);
    }
    
    /**
     * 查询当前候选验证人列表
     *
     * @return
     */
    public RemoteCall<CallResponse<List<Node>>> getCandidateList(BigInteger blockNumer) {
        Function function = new Function(FunctionType.GET_CANDIDATELIST_FUNC_TYPE);
        return executeRemoteCallListValueReturnDe(function, Node.class, blockNumer);
    }

    /**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 * 
	 * @param web3j
	 * @return
	 */
    public static DeNodeContract load(Web3j web3j) {
        return new DeNodeContract(NetworkParameters.getPposContractAddressOfStaking(), web3j);
    }
}
