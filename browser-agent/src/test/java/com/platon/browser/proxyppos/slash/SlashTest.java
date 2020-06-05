package com.platon.browser.proxyppos.slash;

import com.platon.browser.proxyppos.TestBase;
import com.platon.sdk.contracts.ppos.abi.Function;
import com.platon.sdk.contracts.ppos.dto.common.DuplicateSignType;
import com.platon.sdk.contracts.ppos.dto.common.FunctionType;
import com.platon.sdk.contracts.ppos.utils.EncoderUtils;
import com.platon.sdk.utlis.NetworkParameters;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint32;

import java.math.BigInteger;
import java.util.Arrays;

public class SlashTest extends TestBase {
    private final String TARGET_CONTRACT_ADDRESS = NetworkParameters.getPposContractAddressOfSlash(chainId);

    private byte[] encode(DuplicateSignType duplicateSignType,String evidence){
        Function f = new Function(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Uint32(BigInteger.valueOf(duplicateSignType.getValue())), new Utf8String(evidence)));
        byte [] d = Hex.decode(EncoderUtils.functionEncoder(f));
        return d;
    }

    @Test
    public void plan() throws Exception {
        String evidence = "{\"prepareA\":{\"epoch\":0,\"viewNumber\":0,\"blockHash\":\"0x6cb6b43b5ef4c9dafb15490a859e02762971e5e67d81affded0bb83fc55bd9c7\",\"blockNumber\":148631,\"blockIndex\":0,\"blockData\":\"0x4016e722f29004bb4e159ef07fd8b09e14e4512b01f0ac81c688ed66422f92bc\",\"validateNode\":{\"index\":0,\"address\":\"0xcfe51d85f9965f6d031e4e3cce688eab7c95e940\",\"nodeId\":\"bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf\",\"blsPubKey\":\"b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f\"},\"signature\":\"0x2a7f1282177c4a84a5d53c7595956b255042be1efeac5e142b9c6a2d673f04e46b1af2665294a3994c14aa1ee6dd211000000000000000000000000000000000\"},\"prepareB\":{\"epoch\":0,\"viewNumber\":0,\"blockHash\":\"0xc410e26bd8a8d8a0e5c57560b0b2561fd2449992d9616fc42f9d3dc2d9235e73\",\"blockNumber\":148631,\"blockIndex\":0,\"blockData\":\"0xd46bd95d65689b8c53c643e07c5deb24e757b3786c49451118830bce0b7056a2\",\"validateNode\":{\"index\":0,\"address\":\"0xcfe51d85f9965f6d031e4e3cce688eab7c95e940\",\"nodeId\":\"bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf\",\"blsPubKey\":\"b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60f\"},\"signature\":\"0x6fa3eaeefb494b65f68c8740f5dcd29e000bb003e5672d71e95dadfd19811c88075b6668bf16d35dda5857830812421600000000000000000000000000000000\"}}";
        invokeProxyContract(encode(DuplicateSignType.PREPARE_BLOCK,evidence),TARGET_CONTRACT_ADDRESS,encode(DuplicateSignType.PREPARE_BLOCK,evidence),TARGET_CONTRACT_ADDRESS);
    }
}
