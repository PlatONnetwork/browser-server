package com.platon.browser.dto;

import com.platon.browser.dao.entity.UnDelegation;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description:
 */
@Data
public class DelegationBean {
    private List<UnDelegation> unDelegations;
}
