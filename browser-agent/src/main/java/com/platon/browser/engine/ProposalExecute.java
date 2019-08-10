package com.platon.browser.engine;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dto.TransactionInfo;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.stereotype.Component;

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

    private ProposalExecuteResult executeResult = new ProposalExecuteResult();

    private void init(){

    }

    /**
     * 执行交易
     * @param trans
     * @param bc
     */
    public void execute(TransactionInfo trans,BlockChain bc){
        init();
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
