package com.platon.browser.engine;

import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dao.mapper.CustomVoteMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.dto.CustomVote;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.handler.*;
import com.platon.browser.engine.handler.proposal.*;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.CacheConstructException;
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
public class ProposalEngine {
    private static Logger logger = LoggerFactory.getLogger(ProposalEngine.class);

    // 全量数据，需要根据业务变化，保持与数据库一致
    private ProposalCache proposalCache = BlockChain.PROPOSALS_CACHE;

    public static final String pIDIDNum = "PIP-{pip_id}";

    public static final String key = "{pip_id}";
    @Autowired
    private CustomProposalMapper customProposalMapper;
    @Autowired
    private CustomVoteMapper customVoteMapper;

    private ProposalStage proposalStage = BlockChain.STAGE_DATA.getProposalStage();

    /*********************业务事件处理器*********************/

    @Autowired
    private ProposalTextHandler proposalTextHandler;
    @Autowired
    private ProposalCancelHandler proposalCancelHandler;
    @Autowired
    private ProposalUpgradeHandler proposalUpgradeHandler;
    @Autowired
    private VotingProposalHandler votingProposalHandler;
    @Autowired
    private DeclareVersionHandler declareVersionHandler;

    private EventContext context = new EventContext();

    @PostConstruct
    private void init() throws CacheConstructException {
        // 初始化全量数据
        List<CustomProposal> proposalList = customProposalMapper.selectAll();
        List<CustomVote> voteList = customVoteMapper.selectAll();
        proposalCache.init(proposalList,voteList);

        context.setProposalStage(proposalStage);
    }

    /**
     * 执行交易
     * @param tx 交易
     * @param bc BlockChain
     */
    void execute(CustomTransaction tx, BlockChain bc) throws BusinessException, NoSuchBeanException {
        // 事件上下文
        context.setBlockChain(bc);
        context.setTransaction(tx);
        switch (tx.getTypeEnum()){
            case CREATE_PROPOSAL_TEXT: proposalTextHandler.handle(context);break; //提交文本提案(创建提案)
            case CREATE_PROPOSAL_UPGRADE: proposalUpgradeHandler.handle(context);break; //提交升级提案(创建提案)
            case CANCEL_PROPOSAL: proposalCancelHandler.handle(context);break; //其他叫取消提案
            case VOTING_PROPOSAL: votingProposalHandler.handle(context);break; //给提案投票(提案投票)
            case DECLARE_VERSION: declareVersionHandler.handle(context);break;//版本声明
        }
    }
}
