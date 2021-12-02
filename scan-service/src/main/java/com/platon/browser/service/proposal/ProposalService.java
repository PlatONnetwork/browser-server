package com.platon.browser.service.proposal;

import com.platon.browser.bean.ProposalParticipantStat;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.contracts.ppos.dto.CallResponse;
import com.platon.contracts.ppos.dto.resp.TallyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 提案服务
 * @create: 2019-11-26 11:08:25
 **/
@Slf4j
@Service
public class ProposalService {

    @Resource
    private PlatOnClient platOnClient;

    @Resource
    private SpecialApi specialApi;

    /**
     * 取提案参与者统计信息
     *
     * @param proposalHash
     * @param blockHash
     * @return
     * @throws Exception
     */
    public ProposalParticipantStat getProposalParticipantStat(String proposalHash, String blockHash) throws ContractInvokeException, BlankResponseException {
        return specialApi.getProposalParticipants(platOnClient.getWeb3jWrapper().getWeb3j(), proposalHash, blockHash);
    }

    /**
     * 根据提案hash取提案投票结果
     *
     * @param proposalHash
     * @return
     * @throws Exception
     */
    public TallyResult getTallyResult(String proposalHash) throws Exception {
        CallResponse<TallyResult> result = platOnClient.getProposalContract().getTallyResult(proposalHash).send();
        if (result.getData() == null) {
            log.warn("提案[" + proposalHash + "]的投票结果为空!");
        }
        return result.getData();
    }

}
