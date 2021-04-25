package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Delegation;
import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class DelegationEvent implements Event{
    private List <Delegation> delegationList;
}