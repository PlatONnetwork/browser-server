package com.platon.browser.data;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.FunctionType;
import org.web3j.platon.VoteOption;
import org.web3j.platon.bean.Proposal;
import org.web3j.platon.bean.TallyResult;
import org.web3j.platon.contracts.ProposalContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.http.HttpService;

/**
 * 治理相关接口，包括， 提交文本提案 提交升级提案 提交参数提案 给提案投票 版本声明 查询提案 查询提案结果 查询提案列表 查询生效版本 查询节点代码版本
 * 查询可治理参数列表
 */
public class ProposalContractTest {

	private Web3j web3j = Web3j.build(new HttpService("http://192.168.112.172:8789"));

	String nodeId = "0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7";
	String blsPubKey = "b601ed8838a8c02abd9e0a48aba3315d497ffcdde490cf9c4b46de4599135cdd276b45b49e44beb31eea4bfd1f147c0045c987baf45c0addb89f83089886e3b6e1d4443f00dc4be3808de96e1c9f02c060867040867a624085bb38d01bac0107";
//	private String blsPubKey = "b8560588dc7e317e063dd312479426aeb003b106261a1eeaf48b7562168bbc18db5e1852d4d002bdf319fb96de120c63dfae9cbf55b6fed0a376c7916e5e650f";
	String chainId = "100";

	private Credentials superCredentials;
	private Credentials stakingCredentials;
	private Credentials voteCredentials;

	private ProposalContract proposalContract;


	@Before
	public void init() throws Exception {

		superCredentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
		System.out.println("superCredentials balance=" + web3j
				.platonGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

		stakingCredentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
		System.out.println("stakingCredentials balance="
				+ web3j.platonGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send()
						.getBalance());

		voteCredentials = Credentials.create("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
		System.out.println("voteCredentials balance=" + web3j
				.platonGetBalance(voteCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

		proposalContract = ProposalContract.load(web3j, stakingCredentials, chainId);

//        pIDID = String.valueOf(UUID.randomUUID().toString().hashCode());
	}

	/**
	 * 提交文本提案 verifier 提交提案的验证人 url 提案URL，长度不超过512 endVotingBlock ((当前区块高度 / 200
	 * )*200 + 200*10 - 10)
	 */
	@Test
	public void submitTextProposal() {
		try {
			String pIDID = "1";
			PlatonSendTransaction platonSendTransaction = proposalContract
					.submitProposalReturnTransaction(Proposal.createSubmitTextProposalParam(nodeId, pIDID)).send();
			BaseResponse<?> baseResponse = proposalContract
					.getSubmitProposalResult(platonSendTransaction, FunctionType.SUBMIT_TEXT_FUNC_TYPE).send();

			System.out.println("发起提案结果：" + baseResponse.toString());

			voteForProposal(platonSendTransaction.getTransactionHash());

			queryResult(platonSendTransaction.getTransactionHash());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交升级提案 verifier 提交提案的验证人 url 提案URL，长度不超过512 newVersion 升级版本 endVotingBlock
	 * ((当前区块高度 / 200 )*200 + 200*10 - 10) activeBlock(endVotingBlock + 10 + 4*200 <
	 * 生效块高 <= endVotingBlock + 10 + 10*200)
	 */
	@Test
	public void submitVersionProposal() {
		try {
			String num = "6";
			PlatonSendTransaction platonSendTransaction = proposalContract.submitProposalReturnTransaction(
					Proposal.createSubmitVersionProposalParam(nodeId, num, BigInteger.valueOf(20000), BigInteger.valueOf(4)))
					.send();
			BaseResponse<?> baseResponse = proposalContract
					.getSubmitProposalResult(platonSendTransaction, FunctionType.SUBMIT_VERSION_FUNC_TYPE).send();
			System.out.println("发起提案结果：" + baseResponse.toString());

//			voteForProposal(platonSendTransaction.getTransactionHash());
//
//			queryResult(platonSendTransaction.getTransactionHash());
			
			this.cancelProposal(platonSendTransaction.getTransactionHash(), String.valueOf(Integer.parseInt(num)+1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voteForProposal(String proposalID) {
		vote(proposalID,
				"0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7",
				"http://192.168.112.172:8789", VoteOption.YEAS);
//       
//        vote(proposalID, 
//        		"459d199acb83bfe08c26d5c484cbe36755b53b7ae2ea5f7a5f0a8f4c08e843b51c4661f3faa57b03b710b48a9e17118c2659c5307af0cc5329726c13119a6b85", 
//        		"http://192.168.112.171:7789", VoteOption.YEAS);
//        
//        vote(proposalID, 
//        		"53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3", 
//        		"http://192.168.112.172:6789", VoteOption.YEAS);
//        
//        vote(proposalID, 
//        		"ef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc", 
//        		"http://192.168.112.172:7789", VoteOption.YEAS);
	}

	public void vote(String proposalID, String nodeId, String nodeHost, VoteOption voteOption) {
		try {

			Web3j web3j = Web3j.build(new HttpService(nodeHost));
			ProposalContract voteContract = ProposalContract.load(web3j, voteCredentials, chainId);
			BaseResponse<?> baseResponse = voteContract.vote(proposalID, nodeId, VoteOption.YEAS).send();
			System.out.println("投票结果：" + baseResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * 给提案投票
     * verifier 投票验证人
     * proposalID 提案ID
     * option 投票选项 Years(0x01)支持 Nays(0x02)反对 Abstentions(0x03)弃权
     */
    @Test
    public void vote() {
        try {
            BaseResponse<?> baseResponse = proposalContract.vote("0x1178f6dcecd1731e2556d4a014d30ebe04cf5522c07776135e60f613e51af0c9", "25a2407f1692febff715655d53912b6284d8672a411d39b250ec40530a7e36f0b7970ed1d413f9b079e104aba80e5cef25eaf299cbd6a01e8015b505cffebc2d", VoteOption.YEAS).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	@Test
	public void queryResult() {
		try {
			// 0x3329fba78489aa8242d698155f1c67769fb6d207b983398b7fe4392b7d88e2b3
			// 0x04b8fbbfb7392711d418e67cbfb6a6d82c0386915ceff5e0f0630e6bbb3ab486
//			queryResult("0x3329fba78489aa8242d698155f1c67769fb6d207b983398b7fe4392b7d88e2b3");
			queryResult("0xeb9e0603761b0de058b3801f26efdb8779914f1f6cf46a2d3fc9a539e7a42104");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryResult(String proposalID) {
		try {
			BaseResponse<Proposal> baseResponse = proposalContract.getProposal(proposalID).send();
			System.out.println("提案信息：" + baseResponse.data.toString());
//			BaseResponse<TallyResult> result = proposalContract.getTallyResult(proposalID).send();
//			System.out.println("提案结果：" + result.data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交参数提案（取消不用） verifier 提交提案的验证人 url 提案URL，长度不超过512 newVersion 升级版本
	 * endVotingBlock ((当前区块高度 / 200 )*200 + 200*10 - 10) paramName 参数名称
	 * currentValue 当前值 newValue 新的值
	 */
//    @Test
//    public void submitCancelProposal() {
//        try {
//        	String pIDID = "";
//            PlatonSendTransaction platonSendTransaction = proposalContract.submitProposalReturnTransaction(Proposal.createSubmitCancelProposalParam(nodeId, pIDID, BigInteger.valueOf(11980), "0x0250caeb2ffe9344145c5a41d48384657b3bca109b0c95e3b70248697280eb64")).send();
//            BaseResponse baseResponse = proposalContract.getSubmitProposalResult(platonSendTransaction, FunctionType.SUBMIT_CANCEL_FUNC_TYPE).send();
//            System.out.println(baseResponse.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	/**
	 * 查询节点的链生效版本
	 */
	@Test
	public void getActiveVersion() {
		try {
			BaseResponse<?> baseResponse = proposalContract.getActiveVersion().send();
			System.out.println(baseResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 版本声明 activeNode 声明的节点，只能是验证人/候选人
	 */
	@Test
	public void declareVersion() {
		try {
			BaseResponse<?> baseResponse = proposalContract.declareVersion(
					"0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7")
					.send();
			System.out.println(baseResponse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询节点代码版本
	 */
	@Test
	public void getProgramVersion() {
		try {
			BaseResponse<?> baseResponse = proposalContract.getProgramVersion().send();
			System.out.println(baseResponse.data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询提案列表
	 */
	@Test
	public void listProposal() {
		try {
			BaseResponse<List<Proposal>> baseResponse = proposalContract.getProposalList().send();
			List<Proposal> proposalList = baseResponse.data;
			for (Proposal proposal : proposalList) {
				BaseResponse<TallyResult> result = proposalContract.getTallyResult(proposal.getProposalId()).send();
				System.out.print(proposal);
				System.out.println(result.data.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * 提交取消提案
     * verifier 提交提案的验证人
     * url 提案URL，长度不超过512
     * newVersion 升级版本
     * endVotingBlock ((当前区块高度 / 200 )*200 + 200*10 - 10)
     * paramName 参数名称
     * currentValue 当前值
     * newValue 新的值
     */
    @Test
    public void submitCancelProposal() {

        try {
			Proposal proposal = Proposal.createSubmitCancelProposalParam(nodeId, "7", BigInteger.valueOf(1),
					"0xb581d0d5fb33da7011a2de232273cb949278273dc99000069bd5ee3f1b14a61b");
            PlatonSendTransaction platonSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse<?> baseResponse = proposalContract.getSubmitProposalResult(platonSendTransaction, FunctionType.SUBMIT_CANCEL_FUNC_TYPE).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cancelProposal(String hash,String num) {

        try {
			Proposal proposal = Proposal.createSubmitCancelProposalParam(nodeId, num, BigInteger.valueOf(1),
					hash);
            PlatonSendTransaction platonSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse<?> baseResponse = proposalContract.getSubmitProposalResult(platonSendTransaction, FunctionType.SUBMIT_CANCEL_FUNC_TYPE).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
