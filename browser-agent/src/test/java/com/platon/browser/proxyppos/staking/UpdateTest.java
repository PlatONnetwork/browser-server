package com.platon.browser.proxyppos.staking;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.dto.req.UpdateStakingParam;
import com.platon.contracts.ppos.utils.EncoderUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigInteger;

public class UpdateTest extends StakingBase {
    private byte[] encode(UpdateStakingParam param){
        Function function = new Function(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                param.getSubmitInputParameters());
        return Hex.decode(EncoderUtils.functionEncoder(function));
    }

    @Test
    public void update() throws Exception {
        sendRequest(
                param1(),
                param2()
        );
    }

    private byte[] param1(){
        String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
        String benifitAddress = benefitCredentials.getAddress();
        String externalId = "";
        String nodeName = "chendai-node888-u";
        String webSite = "www.baidu.com-u";
        String details = "chendai-node3-details-u";
        BigInteger rewardPer = BigInteger.valueOf(2L);

        UpdateStakingParam param = new UpdateStakingParam.Builder()
                .setBenifitAddress(benifitAddress)
                .setExternalId(externalId)
                .setNodeId(nodeId)
                .setNodeName(nodeName)
                .setWebSite(webSite)
                .setDetails(details)
                .setRewardPer(rewardPer)
                .build();
        return encode(param);
    }

    private byte[] param2(){
        String nodeId = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
        String benifitAddress = benefitCredentials.getAddress();
        String externalId = "";
        String nodeName = "chendai-node9-u";
        String webSite = "www.baidu.com-u3";
        String details = "chendai-node6-details-u";
        BigInteger rewardPer = BigInteger.valueOf(2L);

        UpdateStakingParam param = new UpdateStakingParam.Builder()
                .setBenifitAddress(benifitAddress)
                .setExternalId(externalId)
                .setNodeId(nodeId)
                .setNodeName(nodeName)
                .setWebSite(webSite)
                .setDetails(details)
                .setRewardPer(rewardPer)
                .build();
        return encode(param);
    }
}
