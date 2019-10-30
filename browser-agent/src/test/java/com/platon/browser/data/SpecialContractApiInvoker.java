package com.platon.browser.data;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.SpecialContractApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.platon.contracts.ProposalContract;
import org.web3j.platon.contracts.RestrictingPlanContract;
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
    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:8789")); // test
    //private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));

    private static NodeContract nodeContract = NodeContract.load(web3j);
    private static DelegateContract delegateContract = DelegateContract.load(web3j);
    private static RestrictingPlanContract restrictingPlanContract = RestrictingPlanContract.load(web3j);
    private static ProposalContract proposalContract = ProposalContract.load(web3j);
    private static SpecialContractApi sca = new SpecialContractApi();

    public static void main(String args[]) throws Exception {

        //List<Node> nodes = nodeContract.getVerifierList().send().data;
        List<Node> nodes = nodeContract.getValidatorList().send().data;
        List<Node> nodes1 = nodeContract.getCandidateList().send().data;

        BaseResponse baseResponse = proposalContract.getActiveVersion().send();
        logger.error("{}",JSON.toJSONString(baseResponse.data));
        logger.error("{}",JSON.toJSONString(nodes1));
        BigInteger stakingNumber = new BigInteger("1");

//        List<Node>  verifierList = sca.getHistoryVerifierList(web3j,BigInteger.valueOf(16930));
//        logger.error("{}",JSON.toJSONString(verifierList));
//        List<Node>  validatorList = sca.getHistoryValidatorList(web3j,BigInteger.valueOf(0));
//        logger.error("{}",JSON.toJSONString(validatorList));
//        List<Node> candidates = nodeContract.getCandidateList().send().data;
//        logger.error("{}",JSON.toJSONString(candidates));
        //BaseResponse<List<Node>> nodes = SpecialContractApi.getHistoryValidatorList(web3j,BigInteger.ONE);

        //ProposalParticiantStat pps = sca.getProposalParticipants(web3j,"0xf1392a0f709974b15f9571282b657671c35d8f0f340ed6ae68cb484255c00bba","0x49d9a5960b3ac2f0ddb191e4af2d9782a1114946a084cdfe95ff33a573977819");
        //System.out.println(JSON.toJSONString(pps,true));

        //BaseResponse<RestrictingItem> res = restrictingPlanContract.getRestrictingInfo("0x493301712671ada506ba6ca7891f436d29185821").send();
        //System.out.println(res);
    }
}
