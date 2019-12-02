package com.platon.browser.data;

import com.platon.browser.client.SpecialApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.platon.bean.GovernParam;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.platon.contracts.ProposalContract;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.websocket.WebSocketService;

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
    private static WebSocketService socketService = new WebSocketService("ws://192.168.112.172:8788",false);
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


        //TransactionReceipt tr = web3j.platonGetTransactionReceipt("0x6d73ce6d593b39a4cd47971a8f7be7e9b1df70fce2975945a910738f00513b8d").send().getTransactionReceipt().get();

       /* Web3jWrapper web3jWrapper = Web3jWrapper.builder()
                .web3j(web3j)
                .web3jService(service)
                .address("http://192.168.120.151:6789")
                .build();
        ReceiptResult receiptResult = sca.getReceiptResult(web3jWrapper,BigInteger.valueOf(85087));
        receiptResult.resolve();
        TransactionReceipt tr = web3j.platonGetTransactionReceipt("0x8e464b64554e572ba7c53a6fca539c93f615c98d86c079706d1fee3efb685173").send().getTransactionReceipt().get();

        boolean ok = tr.isStatusOK();
        logger.error("{}",tr.isStatusOK());
*/
//        List<NodeVersion> versions = sca.getNodeVersionList(web3j);
//
//        logger.error("");
//
//        proposalContract.getParamList("");
//        List<Node> nodes = nodeContract.getVerifierList().send().data;
//        List<Node> nodes = nodeContract.getValidatorList().send().data;
//        List<Node> nodes1 = nodeContract.getCandidateList().send().data;

        List<GovernParam> governParamList = proposalContract.getParamList("").send().data;

        //BigInteger blockNumber = web3j.platonBlockNumber().send().getBlockNumber();
        BigInteger balance = web3j
                .platonGetBalance("0x1000000000000000000000000000000000000001", DefaultBlockParameter.valueOf(BigInteger.valueOf(1)))
                .send().getBalance();

        List<Node> preVal = sca.getHistoryValidatorList(web3j,BigInteger.valueOf(40));


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
