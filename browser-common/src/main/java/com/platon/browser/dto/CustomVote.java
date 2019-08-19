package com.platon.browser.dto;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:36
 * @Description:
 */
@Data
public class CustomVote extends Vote {

    public void bulidStructure( Vote vote){
        BeanUtils.copyProperties(vote,this);
    }
}
