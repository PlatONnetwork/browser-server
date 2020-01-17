package com.platon.browser.util.decode.innercontract;

import com.platon.browser.param.ContractExecParam;
import com.platon.browser.param.TxParam;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpList;

import java.util.List;

public class ContractExecDecoder {
    private ContractExecDecoder(){}

    static TxParam decode(RlpList rootList, List<Log> logs) {

        return ContractExecParam.builder().build();
    }
}
