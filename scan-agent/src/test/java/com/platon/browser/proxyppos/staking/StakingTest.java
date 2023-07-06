package com.platon.browser.proxyppos.staking;

import com.platon.contracts.ppos.abi.Function;
import com.platon.contracts.ppos.dto.common.FunctionType;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.contracts.ppos.dto.req.StakingParam;
import com.platon.contracts.ppos.utils.EncoderUtils;
import com.platon.protocol.Web3j;
import com.platon.protocol.http.HttpService;
import com.platon.utils.Convert;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StakingTest extends StakingBase {
    private byte[] encode(StakingParam stakingParam){
        Function function = new Function(
                FunctionType.STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters());
        return Hex.decode(EncoderUtils.functionEncoder(function));
    }

    @Test
    public void staking() throws Exception {
        byte[] d1 = node1();
        byte[] d2 = node2();
        sendRequest(
                d1,
                d2
        );
    }

    private byte[] node1() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.145:6789"));
        String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
        String blsPubKey = "5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811";
        StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
        String benifitAddress = benefitCredentials.getAddress();
        String externalId = "";
        String nodeName = "chendai-node1";
        String webSite = "www.baidu.com";
        String details = "chendai-node1-details";
        BigDecimal stakingAmount = Convert.toVon("5000000", Convert.Unit.KPVON);
        BigInteger rewardPer = BigInteger.valueOf(1L);

        StakingParam stakingParam = new StakingParam.Builder()
                .setNodeId(nodeId)
                .setAmount(stakingAmount.toBigInteger())
                .setStakingAmountType(stakingAmountType)
                .setBenifitAddress(benifitAddress)
                .setExternalId(externalId)
                .setNodeName(nodeName)
                .setWebSite(webSite)
                .setDetails(details)
                .setBlsPubKey(blsPubKey)
                .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                .setRewardPer(rewardPer)
                .build();

        return encode(stakingParam);
    }

    private byte[] node2() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.145:6790"));
        String nodeId = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
        String blsPubKey = "80d98a48400a36e3da9de8e227e4a8c8fa3f90c08c82a467c9ac01298c2eb57f543d7e9568b0f381cc6c9de911870d1292b62459d083700d3958d775fca60e41ddd7d8532163f5acabaa6e0c47b626c39de51d9d67fb97a5af1871a661ca7788";

        StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
        String benifitAddress = benefitCredentials.getAddress();
        String externalId = "";
        String nodeName = "chendai-node2";
        String webSite = "www.baidu.com";
        String details = "chendai-node2-details";
        BigDecimal stakingAmount = Convert.toVon("5000000", Convert.Unit.KPVON);
        BigInteger rewardPer = BigInteger.valueOf(1L);

        StakingParam stakingParam = new StakingParam.Builder()
                .setNodeId(nodeId)
                .setAmount(stakingAmount.toBigInteger())
                .setStakingAmountType(stakingAmountType)
                .setBenifitAddress(benifitAddress)
                .setExternalId(externalId)
                .setNodeName(nodeName)
                .setWebSite(webSite)
                .setDetails(details)
                .setBlsPubKey(blsPubKey)
                .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                .setRewardPer(rewardPer)
                .build();

        return encode(stakingParam);
    }
}
