package com.platon.browser.data;

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
        BaseResponse<List<Node>>  verifierList = SpecialContractApi.getHistoryVerifierList(web3j,BigInteger.valueOf(50215));
        BaseResponse<List<Node>>  validatorList = SpecialContractApi.getHistoryValidatorList(web3j,BigInteger.valueOf(50215));

        BaseResponse<List<Node>> nodes = SpecialContractApi.getHistoryValidatorList(web3j,BigInteger.ONE);
        System.out.println(nodes);
    }
}
