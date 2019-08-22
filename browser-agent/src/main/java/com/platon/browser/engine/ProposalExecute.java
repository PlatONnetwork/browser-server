package com.platon.browser.engine;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.dto.CustomVote;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.handler.*;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class ProposalExecute {
    private static Logger logger = LoggerFactory.getLogger(ProposalExecute.class);

    // 全量数据，需要根据业务变化，保持与数据库一致
    private ProposalCache proposalCache = BlockChain.PROPOSALS_CACHE;

    public static final String pIDIDNum = "PIP-{pip_id}";

    public static final String key = "{pip_id}";
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private VoteMapper voteMapper;

    private ProposalExecuteResult executeResult = BlockChain.STAGE_BIZ_DATA.getProposalExecuteResult();

    /*********************业务事件处理器*********************/

    @Autowired
    private ProposalTextHandler proposalTextHandler;
    @Autowired
    private ProposalCancelHandler proposalCancelHandler;
    @Autowired
    private ProposalUpgradeHandler proposalUpgradeHandler;
    @Autowired
    private VotingProposalHandler votingProposalHandler;

    @PostConstruct
    private void init(){
        // 初始化全量数据
        List<Proposal> proposalList = proposalMapper.selectByExample(null);
        List<Vote> voteList = voteMapper.selectByExample(null);
        proposalList.forEach(proposal -> {
            CustomProposal customProposal = new CustomProposal();
            //构建结构
            customProposal.updateWithProposal(proposal);
            voteList.forEach(vote -> {
                //关联提案的投票结果区分放入全量数据结构
                if(vote.getProposalHash().equals(customProposal.getPipId())){
                    CustomVote customVote = new CustomVote();
                    customVote.buildStructure(vote);
                    if(Integer.valueOf(vote.getOption()).equals(CustomProposal.OptionEnum.SUPPORT.code)){
                        //构建提案关联支持票结构
                        customProposal.getYesList().add(customVote);
                    }
                    if(Integer.valueOf(vote.getOption()).equals(CustomProposal.OptionEnum.OPPOSITION.code)){
                        //构建提案相关反对票结构
                        customProposal.getNoList().add(customVote);
                    }
                    if(Integer.valueOf(vote.getOption()).equals(CustomProposal.OptionEnum.ABSTENTION.code)){
                        //构建提案相关反对票结构
                        customProposal.getAbstentionList().add(customVote);
                    }

                }
            });
            proposalCache.add(customProposal);
        });

    }

    /**
     * 执行交易
     * @param tx
     * @param bc
     */
    public void execute(CustomTransaction tx, BlockChain bc) throws NoSuchBeanException {
        // 事件上下文
        EventContext context = new EventContext(tx,bc,null,null,executeResult);
        switch (tx.getTypeEnum()){
            case CREATE_PROPOSAL_TEXT: proposalTextHandler.handle(context);break; //提交文本提案(创建提案)
            case CREATE_PROPOSAL_UPGRADE: proposalUpgradeHandler.handle(context);break; //提交升级提案(创建提案)
            case CANCEL_PROPOSAL: proposalCancelHandler.handle(context);break; //其他叫取消提案
            case VOTING_PROPOSAL: votingProposalHandler.handle(context);break; //给提案投票(提案投票)
        }
    }

    private void updateTxInfo(CustomTransaction tx, BlockChain bc){

    }
    public void checkProposalStatus(){
        updateProposalStatus();
    }
    private void updateProposalStatus(){

    }
}
