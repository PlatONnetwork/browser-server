package com.platon.browser.engine;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.TransactionInfo;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class ProposalExecute {
    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String,Proposal> proposals = new HashMap<>();

    @Autowired
    private ProposalMapper proposalMapper;

    private ProposalExecuteResult executeResult = new ProposalExecuteResult();

    @PostConstruct
    private void init(){
        // 初始化全量数据
        List<Proposal> proposalList = proposalMapper.selectByExample(null);
        proposalList.forEach(proposal -> proposals.put(proposal.getHash(),proposal));

    }

    /**
     * 执行交易
     * @param trans
     * @param bc
     */
    public void execute(TransactionInfo trans,BlockChain bc){

        switch (trans.getTypeEnum()){
            case CREATEPROPOSALTEXT:
                execute2000(trans);
                break;
            case CREATEPROPOSALUPGRADE:
                execute2001(trans);
                break;
            case CREATEPROPOSALPARAMETER:
                execute2002(trans);
                break;
            case VOTINGPROPOSAL:
                execute2003(trans);
                break;
        }
        updateTxInfo(trans,bc);
    }

    public ProposalExecuteResult exportResult(){
        return executeResult;
    }

    public void commitResult(){
        executeResult.getUpdateProposals().clear();
        executeResult.getAddProposals().clear();
        executeResult.getAddVotes().clear();
    }

    private void updateTxInfo(TransactionInfo trans,BlockChain bc){

    }
    private void execute2000(TransactionInfo trans){

    }
    private void execute2001(TransactionInfo trans){

    }
    private void execute2002(TransactionInfo trans){

    }
    private void execute2003(TransactionInfo trans){

    }
    public void checkProposalStatus(){
        updateProposalStatus();
    }
    private void updateProposalStatus(){

    }
}
