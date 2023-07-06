package com.platon.browser.proxyppos.slash;

import com.platon.abi.solidity.datatypes.Utf8String;
import com.platon.abi.solidity.datatypes.generated.Uint32;
import com.platon.browser.proxyppos.ProxyContract;
import com.platon.browser.proxyppos.TestBase;
import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.DuplicateSignType;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.parameters.NetworkParameters;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.http.HttpService;
import com.platon.tx.RawTransactionManager;
import com.platon.tx.TransactionManager;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;

public class SlashTest extends TestBase {
    private final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfSlash();

    private byte[] encode(DuplicateSignType duplicateSignType,String evidence){
        Function f = new Function(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Uint32(BigInteger.valueOf(duplicateSignType.getValue())), new Utf8String(evidence)));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void slash() throws Exception {
        /**
         * 双签证据的生成
         * ./duplicateSign -dtype 1 -sk <被举报节点私钥> -blskey <被举报节点BLS私钥> -blockNumber <最近出的一个块号>
         *
         * ./duplicateSign -dtype 1 -sk c47153938d9bbbb2bbf3024c65b7676fceba40dd4b7e81a781102abdefb97039 -blskey 8ee60655f883f6f76702dce3624e286c3cb9345e58077223b801952ed3e8e819 -blockNumber 296941
         */

        String evidencePath = this.getClass().getClassLoader().getResource("proxyppos/evidence.json").getPath();
        String evidence = FileUtils.readFileToString(new File(evidencePath),"UTF-8");

        BigInteger contractBalance = defaultWeb3j.platonGetBalance(proxyStakingContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger delegatorBalance = defaultWeb3j.platonGetBalance(defaultCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("*********************");
        System.out.println("*********************");
        System.out.println("ContractBalance("+proxyStakingContractAddress+"):"+contractBalance);
        System.out.println("OperatorBalance("+defaultCredentials.getAddress()+"):"+delegatorBalance);
        System.out.println("*********************");
        System.out.println("*********************");

        Web3j web3j = Web3j.build(new HttpService("http://192.168.112.141:8789"));
        TransactionManager manager = new RawTransactionManager(web3j, delegateCredentials);
        // 举报不能与质押使用同一个代理合约，否则会提示“不能举报自己”
        ProxyContract contract = ProxyContract.load(proxySlashContractAddress, web3j, manager, gasProvider, chainId);
        invokeProxyContract(
                contract,
                encode(DuplicateSignType.PREPARE_BLOCK,evidence),
                TARGET_CONTRACT_ADDRESS,
                encode(DuplicateSignType.PREPARE_BLOCK,evidence),
                TARGET_CONTRACT_ADDRESS
        );
    }
}
