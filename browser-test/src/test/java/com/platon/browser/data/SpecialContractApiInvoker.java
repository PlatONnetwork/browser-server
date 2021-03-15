package com.platon.browser.data;

import com.platon.contracts.ppos.DelegateContract;
import com.platon.contracts.ppos.NodeContract;
import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.RestrictingPlanContract;
import com.platon.contracts.ppos.dto.resp.GovernParam;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.websocket.WebSocketService;
import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.EpochInfo;
import com.platon.browser.client.SpecialApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.ConnectException;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 21:00
 * @Description: 特殊节点合约接口调用
 */
public class SpecialContractApiInvoker {
    private static Logger logger = LoggerFactory.getLogger(SpecialContractApiInvoker.class);
    //    private static Web3j web3j = Web3j.build(new HttpService("http://192.168.120.76:6797")); // atonDev
//    private static Web3jService service = new HttpService("http://192.168.120.151:6789");
//    private static Web3j web3j = Web3j.build(service); // atonTest
    private static WebSocketService socketService = new WebSocketService("ws://192.168.112.171:6666",false);
    static Long chainId = 108l;
    private static Web3j web3j; // test
    static {
        try {
            socketService.connect();
            web3j = Web3j.build(socketService); // test
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    //private Web3j currentValidWeb3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));

    private static NodeContract nodeContract = NodeContract.load(web3j);
    private static DelegateContract delegateContract = DelegateContract.load(web3j);
    private static RestrictingPlanContract restrictingPlanContract = RestrictingPlanContract.load(web3j);
    private static ProposalContract proposalContract = ProposalContract.load(web3j);
    private static SpecialApi sca = new SpecialApi();

    public static void main(String args[]) throws Exception {

//        BigInteger balance = web3j
//                .platonGetBalance("0x1000000000000000000000000000000000000003", DefaultBlockParameter.valueOf(BigInteger.ONE))
//                .send().getBalance();

        String code = web3j
                .platonGetCode("0x1000000000000000000000000000000000000003", DefaultBlockParameterName.LATEST)
                .send().getCode();

        List<Node> ver = nodeContract.getVerifierList().send().getData();
        List<Node> hver = sca.getHistoryVerifierList(web3j,BigInteger.valueOf(43520));

//        List<Node> val = nodeContract.getValidatorList().send().getData();
//        List<Node> hval = sca.getHistoryValidatorList(web3j,BigInteger.valueOf(1660));

        EpochInfo res = sca.getEpochInfo(web3j,BigInteger.valueOf(170000));

        List<GovernParam> governParamList = proposalContract.getParamList("").send().getData();
        String json = JSON.toJSONString(governParamList,true);
        logger.error("{}",json);
        //BigInteger blockNumber = web3j.platonBlockNumber().send().getBlockNumber();
        /*BigInteger balance = web3j
                .platonGetBalance("0x1000000000000000000000000000000000000001", DefaultBlockParameter.valueOf(BigInteger.valueOf(1)))
                .send().getBalance();*/

        List<Node> preVal = sca.getHistoryValidatorList(web3j,BigInteger.valueOf(40));
        System.out.println();


        List<Node> curVal = sca.getHistoryValidatorList(web3j,BigInteger.valueOf(6440L));

//        List<Node> preVer = sca.getHistoryVerifierList(web3j,BigInteger.valueOf(320L));
//        List<Node> curVer = sca.getHistoryVerifierList(web3j,BigInteger.valueOf(321L));
//
//        BaseResponse baseResponse = proposalContract.getActiveVersion().send();
//        logger.error("{}",JSON.toJSONString(baseResponse.data));
//        logger.error("{}",JSON.toJSONString(nodes1));
//        BigInteger stakingNumber = new BigInteger("1");

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
