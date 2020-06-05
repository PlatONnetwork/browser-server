package com.platon.browser.proxyppos.restricting;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.CustomStaticArray;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.dto.resp.RestrictingPlan;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.Bech32;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.BytesType;

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
        invokeProxyContract(
                defaultProxyContract,
                encode(restrictingPlans),
                TARGET_CONTRACT_ADDRESS,
                encode(restrictingPlans),
                TARGET_CONTRACT_ADDRESS
        );
    }
}
