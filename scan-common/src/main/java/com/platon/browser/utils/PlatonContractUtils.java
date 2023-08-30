package com.platon.browser.utils;

import com.platon.bech32.Bech32;
import com.platon.browser.param.DelegateRewardClaimParam;
import com.platon.browser.param.TxParam;
import com.platon.browser.param.claim.Reward;
import com.platon.parameters.NetworkParameters;
import com.platon.rlp.solidity.RlpDecoder;
import com.platon.rlp.solidity.RlpList;
import com.platon.rlp.solidity.RlpString;
import com.platon.rlp.solidity.RlpType;
import com.platon.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PlatonContractUtils {
    /**
     * // addLog let the result add to event.
     * // 参数datas可为空,里面的值不能为空
     * // Log.data字段编码规则:
     * // 如果datas为空,  rlp([errCodeString]),
     * // 如果datas不为空,rlp([errCodeString,rlp(data1),rlp(data2)...]),
     * @return
     */
    public static List<byte[]> decodeLog(String data){
        List<byte[]> decodedList = new ArrayList<>();

        if (StringUtils.isBlank(data)){
            return decodedList;
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(data));
        List<RlpType> rlpDataList = ((RlpList) (rlp.getValues().get(0))).getValues();

        //第0个data是err code转成的字符串
        byte[] errCode =((RlpString) rlpDataList.get(0)).getBytes();
        decodedList.add(errCode);

        //第1个开始是经过rlp编码的真正的返回数据
        for(int i=1; i< rlpDataList.size(); i++){
            //先得到rlp编码的字节数组
            byte[] eachDataRlpEncodedBytes = ((RlpString) rlpDataList.get(i)).getBytes();
            //再rlp解码
            RlpList eachDataDecodedRlpList = RlpDecoder.decode(eachDataRlpEncodedBytes);
            //RlpList中只有一个元素（返回值）
            RlpString eachDataRlpString = (RlpString)eachDataDecodedRlpList.getValues().get(0);

            decodedList.add(eachDataRlpString.getBytes());
        }
        return decodedList;
    }



    public static String toBech32Address(byte[] addrBytes){
        return Bech32.addressEncode(NetworkParameters.getHrp(),Numeric.toHexString(addrBytes));
    }

    public static String toNodeIdWithout0x(byte[] nodeIdBytes){
        return Hex.toHexString(nodeIdBytes);
    }

    public static void main(String[] args){
        //code := "0"
        //datas := []interface{}{big.NewInt(10000), common.Address{0x01, 0x02, 0x03}, discover.NodeID{0x10, 0x20, 0x30}, "testNode"}
        String logData = "0xf869308382271095940102030000000000000000000000000000000000b842b840102030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008988746573744e6f6465";
        String logDataOfWithdrawDelegateReward = "0xf89d30b89af898f84ab840010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008203e8843b9aca00f84ab840020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008207d08477359400";
        List<byte[]>  dataList = PlatonContractUtils.decodeLog(logDataOfWithdrawDelegateReward);

        System.out.println("errCode:" + Integer.parseInt(new String(dataList.get(0))));
        System.out.println("amount:" + new BigInteger(dataList.get(1)));


        //结果一样
        System.out.println("address:" + Bech32.addressEncode(NetworkParameters.getHrp(),Numeric.toHexString(dataList.get(2))));
        System.out.println("address:" + toBech32Address(dataList.get(2)));

        //结果不同，一个有0x前缀，一个没有0x前缀
        System.out.println("nodeID:" + Numeric.toHexString(dataList.get(3))); //有0x前缀
        System.out.println("nodeID:" + toNodeIdWithout0x(dataList.get(3))); //没有0x前缀

        System.out.println("nodeName:" + new String(dataList.get(4)));
    }

    public static void main2(String[] args){
        //code := "0"
        //datas := []interface{}{big.NewInt(10000), common.Address{0x01, 0x02, 0x03}, discover.NodeID{0x10, 0x20, 0x30}, "testNode"}
        String logData = "0xf869308382271095940102030000000000000000000000000000000000b842b840102030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008988746573744e6f6465";
        String logDataOfWithdrawDelegateReward = "0xf89d30b89af898f84ab840010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008203e8843b9aca00f84ab840020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008207d08477359400";
        TxParam  dataList = decode(logDataOfWithdrawDelegateReward);

        System.out.println("size:" + ((DelegateRewardClaimParam)dataList).getRewardList().size());

    }

    public static TxParam decode(String logDataHex) {

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logDataHex));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
//        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
//        int statusCode = Integer.parseInt(decodedStatus);

        DelegateRewardClaimParam param = DelegateRewardClaimParam.builder()
                .rewardList(new ArrayList<>())
                .build();
        if(rlpList.size() < 2) {
            return param;
        }
        ((RlpList)RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())
                .getValues()
                .get(0))
                .getValues()
                .forEach(rl -> {
                    RlpList rlpL = (RlpList)rl;

                    String nodeId = ((RlpString)rlpL.getValues().get(0)).asString();
                    BigInteger stakingNum = ((RlpString)rlpL.getValues().get(1)).asPositiveBigInteger();
                    BigInteger amount = ((RlpString)rlpL.getValues().get(2)).asPositiveBigInteger();

                    Reward reward = Reward.builder()
                            .nodeId(HexUtil.prefix(nodeId))
                            .stakingNum(stakingNum)
                            .reward(new BigDecimal(amount))
                            .build();
                    param.getRewardList().add(reward);
                });
        return param;
    }
}
