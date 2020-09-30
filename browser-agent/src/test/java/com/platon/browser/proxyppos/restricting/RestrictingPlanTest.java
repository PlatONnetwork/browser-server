package com.platon.browser.proxyppos.restricting;

import com.alaya.abi.solidity.datatypes.BytesType;
import com.alaya.bech32.Bech32;
import com.alaya.contracts.ppos.abi.CustomStaticArray;
import com.alaya.contracts.ppos.abi.Function;
import com.alaya.contracts.ppos.dto.common.FunctionType;
import com.alaya.contracts.ppos.dto.resp.RestrictingPlan;
import com.alaya.contracts.ppos.utils.EncoderUtils;
import com.alaya.crypto.Credentials;
import com.alaya.parameters.NetworkParameters;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.http.HttpService;
import com.alaya.tx.RawTransactionManager;
import com.alaya.tx.TransactionManager;
import com.platon.browser.proxyppos.ProxyContract;
import com.platon.browser.proxyppos.TestBase;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestrictingPlanTest extends TestBase {
    private final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfRestrctingPlan(chainId);

    private String benefitAddress = "lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e";

    private byte[] encode(List<RestrictingPlan> p){
        Function f = new Function(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.asList(new BytesType(Bech32.addressDecode(benefitAddress)), new CustomStaticArray<>(p)));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void plan() throws Exception {
        List<RestrictingPlan> restrictingPlans = new ArrayList<>();
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(1000), new BigInteger("2000000000000000000")));
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(2000), new BigInteger("2000000000000000000")));

        BigInteger contractBalance = defaultWeb3j.platonGetBalance(proxyStakingContractAddress, DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger delegatorBalance = defaultWeb3j.platonGetBalance(defaultCredentials.getAddress(chainId), DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("*********************");
        System.out.println("*********************");
        System.out.println("ContractBalance("+proxyStakingContractAddress+"):"+contractBalance);
        System.out.println("OperatorBalance("+defaultCredentials.getAddress(chainId)+"):"+delegatorBalance);
        System.out.println("*********************");
        System.out.println("*********************");

        Credentials credentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));
        TransactionManager manager = new RawTransactionManager(web3j, credentials, chainId);
        ProxyContract contract = ProxyContract.load(proxyStakingContractAddress, web3j, manager, gasProvider, chainId);
        invokeProxyContract(
                contract,
                encode(restrictingPlans),
                TARGET_CONTRACT_ADDRESS,
                encode(restrictingPlans),
                TARGET_CONTRACT_ADDRESS
        );
    }
}
