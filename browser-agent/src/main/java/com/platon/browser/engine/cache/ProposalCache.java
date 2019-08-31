package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomVote;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提案进程缓存
 * @Auther: Chendongming
 * @Date: 2019/8/22 16:46
 * @Description:
 */
public class ProposalCache {
    // <提案交易hash - 提案实体>
    private Map<String, CustomProposal> proposalMap = new ConcurrentHashMap<>();

    public void init(List<CustomProposal> proposalList,List<CustomVote> voteList) throws CacheConstructException {
        proposalList.forEach(this::addProposal);
        for (CustomVote vote:voteList) {
            try {
                addVote(vote);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException("构造缓存错误:"+e.getMessage()+", 无法向其关联投票(proposalHash="+vote.getProposalHash()+",hash="+vote.getHash()+")");
            }
        }
    }

    public CustomProposal getProposal(String hash) throws NoSuchBeanException {
        CustomProposal proposal = proposalMap.get(hash);
        if(proposal==null) throw new NoSuchBeanException("提案(hash="+hash+")的不存在");
        return proposal;
    }
    public void addProposal(CustomProposal proposal){
        proposalMap.put(proposal.getHash(),proposal);
    }

    public void addVote(CustomVote vote) throws NoSuchBeanException {
        CustomProposal proposal = getProposal(vote.getProposalHash());
        switch (vote.getOptionEnum()){
            case SUPPORT: // 关联支持票
                proposal.getYesList().add(vote);
                break;
            case ABSTENTION: // 关联反对票
                proposal.getNoList().add(vote);
                break;
            case OPPOSITION: // 关联弃权票
                proposal.getAbstentionList().add(vote);
                break;
        }
    }

    public Collection<CustomProposal> getAllProposal(){
        return proposalMap.values();
    }

    /**
     * 缓存维护方法
     * 清扫全量缓存，移除历史数据
     */
    public void sweep() {
        /* 清除依据：
         * 文本提案、取消提案：状态为【2、3】时表示提案已经结束，需要从缓存删除
         * 升级提案：状态为【3、5、6】时表示提案已经结束，需要从缓存删除
         */
        List<CustomProposal> invalidCache = new ArrayList<>();
        proposalMap.values().forEach(proposal -> {
            CustomProposal.TypeEnum typeEnum = CustomProposal.TypeEnum.getEnum(proposal.getType());
            CustomProposal.StatusEnum statusEnum = CustomProposal.StatusEnum.getEnum(proposal.getStatus());
            if(typeEnum== CustomProposal.TypeEnum.TEXT||typeEnum== CustomProposal.TypeEnum.CANCEL){
                // 如果是文本提案或取消提案
                if(statusEnum== CustomProposal.StatusEnum.PASS||statusEnum== CustomProposal.StatusEnum.FAIL){
                    // 提案通过(2)或失败(3)
                    invalidCache.add(proposal);
                }
            }
            if(typeEnum== CustomProposal.TypeEnum.UPGRADE){
                // 如果是升级提案
                if(statusEnum== CustomProposal.StatusEnum.FAIL||statusEnum== CustomProposal.StatusEnum.FINISH|| statusEnum==CustomProposal.StatusEnum.CANCEL){
                    // 提案失败(3)或生效(5)或被取消(6)
                    invalidCache.add(proposal);
                }
            }
        });
        // 删除无效提案
        invalidCache.forEach(proposal -> proposalMap.remove(proposal.getHash()));
    }
}
