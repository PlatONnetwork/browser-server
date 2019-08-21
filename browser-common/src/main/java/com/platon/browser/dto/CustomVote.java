package com.platon.browser.dto;

import com.platon.browser.dao.entity.Vote;
import com.platon.browser.param.VotingProposalParam;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:36
 * @Description:
 */
@Data
public class CustomVote extends Vote {

    public void buildStructure( Vote vote){
        BeanUtils.copyProperties(vote,this);
    }

    public void updateWithVote( CustomTransaction tx, VotingProposalParam param ){
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setHash(tx.getHash());
        this.setProposalHash(param.getProposalId());
        this.setOption(param.getOption());
        this.setTimestamp(tx.getTimestamp());
        this.setVerifier(param.getVerifier());
    }
}
