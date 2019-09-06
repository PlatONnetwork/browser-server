package com.platon.browser.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.client.AccuVerifiersCount;
import com.platon.browser.client.SpecialContractApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 21:00
 * @Description: 特殊节点合约接口调用
 */
public class SpecialContractApiInvoker {
    private static Logger logger = LoggerFactory.getLogger(SpecialContractApiInvoker.class);
    //    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.120.76:6797")); // atonDev
//    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.120.90:6789")); // atonTest
    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.112.171:6789")); // test
    private static NodeContract nodeContract = NodeContract.load(web3j);

    public static void main(String args[]) throws Exception {
        BaseResponse verifiersCountBaseResponse = SpecialContractApi.getProposalAccuVerifiers(web3j,"0xf1392a0f709974b15f9571282b657671c35d8f0f340ed6ae68cb484255c00bba","0x49d9a5960b3ac2f0ddb191e4af2d9782a1114946a084cdfe95ff33a573977819");
        //BaseResponse<List<Node>>  verifierList = SpecialContractApi.getHistoryVerifierList(web3j,BigInteger.valueOf(50215));
        //BaseResponse<List<Node>>  validatorList = SpecialContractApi.getHistoryValidatorList(web3j,BigInteger.valueOf(50215));

        //BaseResponse<List<Node>> nodes = SpecialContractApi.getHistoryValidatorList(web3j,BigInteger.ONE);

        String a = verifiersCountBaseResponse.data.toString();
        String str =a.substring(1,a.length()-1);
        String[] ids = str.split(",");
        for (String count : ids) {
            System.out.println(count);
        }
        System.out.println(ids);
        System.out.println(verifiersCountBaseResponse.data);
    }
}
