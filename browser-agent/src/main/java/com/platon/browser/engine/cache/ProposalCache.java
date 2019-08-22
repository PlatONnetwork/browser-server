package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.exception.NoSuchBeanException;

import java.util.HashMap;
import java.util.Map;

/**
 * 提案进程缓存
 * @Auther: Chendongming
 * @Date: 2019/8/22 16:46
 * @Description:
 */
public class ProposalCache {
    // <提案ID（pipId） - 提案实体>
    private Map<Integer, CustomProposal> proposalMap = new HashMap<>();

    public CustomProposal getProposal(Integer pipId) throws NoSuchBeanException {
        CustomProposal proposal = proposalMap.get(pipId);
        if(proposal==null) throw new NoSuchBeanException("提案(pipId="+pipId+")的不存在");
        return proposal;
    }
    public void add(CustomProposal proposal){
        proposalMap.put(proposal.getPipId(),proposal);
    }

    public Map<Integer, CustomProposal> getAll(){
        return proposalMap;
    }
}
