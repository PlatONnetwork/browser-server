package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Proposal;
import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class ProposalEvent implements Event{
    private List <Proposal> proposalList;
}