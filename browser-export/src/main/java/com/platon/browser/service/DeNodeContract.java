package com.platon.browser.service;

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;

import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.CallResponse;
import com.platon.sdk.contracts.ppos.dto.common.ContractAddress;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.dto.resp.Node;

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
        return new DeNodeContract(ContractAddress.NODE_CONTRACT_ADDRESS, web3j);
    }
}
