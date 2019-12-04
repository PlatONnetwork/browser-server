package com.platon.browser.common.service.proposal;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ProposalParticipantStat;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.TallyResult;

/**
 * @description: 提案服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-26 11:08:25
 **/
@Slf4j
@Service
public class ProposalService {
    @Autowired
    private PlatOnClient client;
    @Autowired
    private SpecialApi sca;

    /**
     * 取提案参与者统计信息
     *
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
    public ProposalParticipantStat getProposalParticipantStat (String proposalHash, String blockHash ) throws ContractInvokeException, BlankResponseException {
        return sca.getProposalParticipants(client.getWeb3jWrapper().getWeb3j(), proposalHash, blockHash);
    }

    /**
     * 根据提案hash取提案投票结果
     *
     * @param proposalHash
     * @return
     * @throws Exception
     */
    public TallyResult getTallyResult (String proposalHash ) throws Exception {
        BaseResponse<TallyResult> result = client.getProposalContract().getTallyResult(proposalHash).send();
        if (result.isStatusOk()) {
            return result.data;
        }
        throw new ContractInvokeException("查询不到提案[proposalHash=" + proposalHash + "]对应的投票结果!");
    }
}
