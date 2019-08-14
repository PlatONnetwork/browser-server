package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.TransactionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class ProposalExecute {
    private static Logger logger = LoggerFactory.getLogger(ProposalExecute.class);

    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String,Proposal> proposals = new HashMap<>();

    @Autowired
    private ProposalMapper proposalMapper;

    @Autowired
    private PlatonClient client;

    private ProposalExecuteResult executeResult = new ProposalExecuteResult();

    @PostConstruct
    private void init(){
        // 初始化全量数据
        List<Proposal> proposalList = proposalMapper.selectByExample(null);
        proposalList.forEach(proposal -> proposals.put(proposal.getHash(),proposal));

    }

    /**
     * 执行交易
     * @param tx
     * @param bc
     */
    public void execute(TransactionBean tx, BlockChain bc){

        switch (tx.getTypeEnum()){
            case CREATEPROPOSALTEXT:
                execute2000(tx,bc);
                break;
            case CREATEPROPOSALUPGRADE:
                execute2001(tx,bc);
                break;
            case CREATEPROPOSALPARAMETER:
                execute2002(tx,bc);
                break;
            case VOTINGPROPOSAL:
                execute2003(tx,bc);
                break;
        }
        updateTxInfo(tx,bc);
    }

    public ProposalExecuteResult exportResult(){
        return executeResult;
    }

    public void commitResult(){
        executeResult.getUpdateProposals().clear();
        executeResult.getAddProposals().clear();
        executeResult.getAddVotes().clear();
    }

    private void updateTxInfo(TransactionBean tx, BlockChain bc){

    }
    //提交文本提案(创建提案)
    private void execute2000(TransactionBean tx, BlockChain bc){

    }
    //提交升级提案(创建提案)
    private void execute2001(TransactionBean tx, BlockChain bc){

    }
    //提交参数提案(创建提案)
    private void execute2002(TransactionBean tx, BlockChain bc){

    }
    //给提案投票(提案投票)
    private void execute2003(TransactionBean tx, BlockChain bc){

    }
    public void checkProposalStatus(){
        updateProposalStatus();
    }
    private void updateProposalStatus(){

    }
}
