package com.platon.browser.data;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.RestrictingBalance;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.engine.bean.AnnualizedRateInfo;
import com.platon.browser.engine.bean.PeriodValueElement;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 21:00
 * @Description: 特殊节点合约接口调用
 */
public class SpecialContractApiInvoker {
    private static Logger logger = LoggerFactory.getLogger(SpecialContractApiInvoker.class);
//    private Web3j web3j = Web3j.build(new HttpService("http://192.168.21.138:6789"));
    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.112.171:6789"));
//    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:6789"));
//    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.120.76:6797"));
    private static NodeContract nodeContract = NodeContract.load(web3j);

    // 特殊合约接口测试
    @Test
    public void getCandidates() throws Exception {
/*        // 当前区块号
        BigInteger blockNumber = currentValidWeb3j.platonBlockNumber().send().getBlockNumber();
        logger.error("{}",blockNumber);
        // 查询区块号为1时的结算周期历史验证人列表
        BaseResponse<List<Node>> historyVerifierList = getHistoryVerifierList(BigInteger.valueOf(0));
        logger.error("{}",historyVerifierList);
        // 查询实时结算验证人列表
        BaseResponse<List<Node>> verifierList = nodeContract.getVerifierList().send();
        logger.error("{}",verifierList);
        // 查询区块号为1时的共识周期历史验证人列表
        BaseResponse<List<Node>> historyValidatorList = getHistoryValidatorList(BigInteger.valueOf(0));
        logger.error("{}",historyValidatorList);
        // 查询实时共识验证人列表
        BaseResponse<List<Node>> validatorList = nodeContract.getValidatorList().send();
        logger.error("{}",validatorList);*/

        // 获取可用和锁仓余额
        List<String> addresses = new ArrayList<>();
        addresses.add("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
        BaseResponse<List<RestrictingBalance>> balances = SpecialContractApi
                .getRestrictingBalance(
                        web3j,
                        "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219;0x60ceca9c1290ee56b98d4e160ef0453f7c40d211"
                );
        logger.error("{}",balances);
    }


    public BytesType getRlpEncodeData( List<String> list) {

        if (list != null && !list.isEmpty()) {
            List<RlpType> rlpListList = new ArrayList <>();
            for (String t : list) {
                rlpListList.add(RlpString.create(RlpEncoder.encode(RlpString.create((t)))));
            }
            return new BytesType(RlpString.create(RlpEncoder.encode(new RlpList(rlpListList))).getBytes());
        }
        throw new RuntimeException("unsupported types");
    }

    public static void main(String args[]){
       // String a = new String(Numeric.hexStringToByteArray("0xf848b8467b22537461747573223a66616c73652c2244617461223a22222c224572724d7367223a22546869732063616e64696461746520697320616c726561647920657869737473227d"));
        /*String input = "0xf84e838203ec8180b842b84000cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000838203e8";
        RlpList rlpList = RlpDecoder.decode(Hex.decode(input.replace("0x", "")));
        List <RlpType> rlpTypes = rlpList.getValues();
        RlpList rlpList1 = (RlpList) rlpTypes.get(0);
        RlpString rlpString = (RlpString) rlpList1.getValues().get(0);
        String stringValue = Numeric.toHexString(rlpString.getBytes());
        String res = new String(Numeric.hexStringToByteArray(stringValue));
        System.out.println(res);
        BaseResponse response = JSONUtil.parseObject(new String(Numeric.hexStringToByteArray("0xf848b8467b22537461747573223a66616c73652c2244617461223a22222c224572724d7367223a22546869732063616e64696461746520697320616c726561647920657869737473227d")), BaseResponse.class);
        System.out.println(response.status);*/
        //System.out.println(a);


        try {
//            Web3j web3j2 = Web3j.build(new HttpService("http://192.168.112.172:6789"));
            Web3j web3j2 = Web3j.build(new HttpService("http://192.168.112.171:6789"));

            List<Node> nodes = nodeContract.getVerifierList().send().data;

            BigInteger number = web3j2.platonBlockNumber().send().getBlockNumber();

            BaseResponse<List<Node>>  v9840 = SpecialContractApi.getHistoryVerifierList(web3j2,BigInteger.valueOf(50215));
//            List<Node>  v9841 = SpecialContractApi.getHistoryValidatorList(web3j2,BigInteger.valueOf(9841)).data;
//            List<Node>  v9880 = SpecialContractApi.getHistoryValidatorList(web3j2,BigInteger.valueOf(9880)).data;
//            List<Node>  v9881 = SpecialContractApi.getHistoryValidatorList(web3j2,BigInteger.valueOf(9881)).data;
            //BaseResponse<List<Node>>  v9840 = SpecialContractApi.getHistoryVerifierList(web3j2,BigInteger.valueOf(1040));
//            List<Node>  v9841 = SpecialContractApi.getHistoryValidatorList(web3j2,BigInteger.valueOf(9841)).data;
//            List<Node>  v9880 = SpecialContractApi.getHistoryValidatorList(web3j2,BigInteger.valueOf(9880)).data;
//            List<Node>  v9881 = SpecialContractApi.getHistoryValidatorList(web3j2,BigInteger.valueOf(9881)).data;

            BigInteger jili = web3j2.platonGetBalance("0x1000000000000000000000000000000000000003",DefaultBlockParameter.valueOf(BigInteger.valueOf(19200))).send().getBalance();

            BigDecimal part = new BigDecimal(jili).multiply(BigDecimal.valueOf(0.5));

            // 每个区块的奖励
            BigDecimal qukuai = part.divide(BigDecimal.valueOf(1600));

            // 每个结算周期的奖励
            BigDecimal jiesuan = part.divide(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(6));

//            460967779502166554487241.47375
//            73754844720346648717958635.8
            // 12292474120057774786326439.3

            BaseResponse<List<Node>>  node1 = SpecialContractApi.getHistoryVerifierList(web3j2,BigInteger.valueOf(0));
            logger.debug("{}", JSON.toJSONString(node1,true));

            BaseResponse<List<Node>> verifierList = SpecialContractApi.getHistoryVerifierList(web3j2,BigInteger.valueOf(320));
            BigDecimal stakingReward = jiesuan.divide(BigDecimal.valueOf(verifierList.data.size()));

        } catch (Exception e) {
            e.printStackTrace();
        }


        BigDecimal init = Convert.toVon("10000000000", Convert.Unit.LAT);

        try {
            BigDecimal jili = new BigDecimal(web3j
                    .platonGetBalance("0x1000000000000000000000000000000000000003",DefaultBlockParameterName.LATEST)
                    .send().getBalance());


            BigDecimal aa = new BigDecimal("42993086.2369165");

            BigDecimal bd = init.multiply(BigDecimal.valueOf(1.025).pow(4)).subtract(jili).add(aa);
            BigDecimal lat = Convert.fromVon(bd.toString(), Convert.Unit.LAT);
            System.out.println(lat);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = "{\n" +
                "  \"cost\": [\n" +
                "    {\n" +
                "      \"period\": 147,\n" +
                "      \"value\": 5100000000000000000000000\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 146,\n" +
                "      \"value\": 5100000000000000000000000\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 145,\n" +
                "      \"value\": 5100000000000000000000000\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 144,\n" +
                "      \"value\": 5100000000000000000000000\n" +
                "    }\n" +
                "  ],\n" +
                "  \"profit\": [\n" +
                "    {\n" +
                "      \"period\": 147,\n" +
                "      \"value\": 2611933329584907631867019685\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 146,\n" +
                "      \"value\": 2583893470298945701430536350\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 145,\n" +
                "      \"value\": 2555853611012983770994053015\n" +
                "    },\n" +
                "    {\n" +
                "      \"period\": 144,\n" +
                "      \"value\": 2523291193777673142100072370\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        AnnualizedRateInfo ari = JSON.parseObject(str,AnnualizedRateInfo.class);
        BigDecimal cost = BigDecimal.ZERO,profit=BigDecimal.ZERO;
        for(PeriodValueElement e:ari.getCost()) cost=cost.add(new BigDecimal(e.getValue()));
        for(PeriodValueElement e:ari.getProfit()) profit=profit.add(new BigDecimal(e.getValue()));

        BigDecimal rate = profit.divide(cost,20, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(10)).multiply(BigDecimal.valueOf(100));

        System.out.println(rate);


    }
}
