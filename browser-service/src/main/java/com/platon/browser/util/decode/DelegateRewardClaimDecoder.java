package com.platon.browser.util.decode;

import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.param.TxParam;
import com.platon.browser.param.claim.Reward;
import com.platon.browser.utils.HexTool;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.platon.browser.util.decode.Decoder.bigIntegerResolver;
import static com.platon.browser.util.decode.Decoder.stringResolver;

/**
 * @description: 领取委托奖励交易输入参数解码器
 * @author: chendongming@juzix.net
 * @create: 2020-01-02 15:28:04
 **/
class DelegateRewardClaimDecoder {
    private DelegateRewardClaimDecoder(){}

    static TxParam decode(RlpList rootList,List<Log> logs) {

        String logData = logs.get(0).getData();
        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        // TODO: 补充领取委托奖励交易输入参数解码逻辑

        DelegateRewardClaimParam param = DelegateRewardClaimParam.builder()
                .rewards(new ArrayList<>())
                .build();
        ((RlpList)RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())
                .getValues()
                .get(0))
                .getValues()
                .forEach(rl -> {
                    RlpList rlpL = (RlpList)rl;

                    String nodeId = ((RlpString)rlpL.getValues().get(0)).asString();
                    BigInteger stakingNum = ((RlpString)rlpL.getValues().get(1)).asPositiveBigInteger();
                    BigInteger amount = ((RlpString)rlpL.getValues().get(1)).asPositiveBigInteger();

                    Reward reward = Reward.builder()
                            .nodeId(HexTool.prefix(nodeId))
                            .stakingNum(stakingNum)
                            .reward(new BigDecimal(amount))
                            .build();
                    param.getRewards().add(reward);
                });
        return param;
    }


    public static  void  test1005(){
        String logData = "0xc23064";

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        BigInteger bigInteger = ((RlpString)rlpList.get(1)).asPositiveBigInteger();

        System.out.println("status="+statusCode+"  value="+bigInteger);
    }


    public static  void  test5000(){
        String logData = "0xf84e30b84bf849f847b840362003c50ed3a523cdede37a001803b8f0fed27cb402b3d6127a1a96661ec202318f68f4c76d9b0bfbabfd551a178d4335eaeaa9b7981a4df30dfc8c0bfe3384830f424064";

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        ((RlpList)((RlpList)RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())).getValues().get(0)).getValues()
                .stream()
                .forEach(rl -> {
                    RlpList rlpL = (RlpList)rl;
                    System.out.println("NodeID="+((RlpString)rlpL.getValues().get(0)).asString());
                    System.out.println("StakingNum="+((RlpString)rlpL.getValues().get(1)).asPositiveBigInteger());
                    System.out.println("Reward="+((RlpString)rlpL.getValues().get(1)).asPositiveBigInteger());
                });

        System.out.println("status="+statusCode);
    }

    public static void main(String[] args) {
        test1005();
        test5000();
    }
}
