package com.platon.browser.engine.result;

import com.platon.browser.dao.entity.Address;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 16:46
 */
public class AddressExecuteResult {

    private Set<Address> stageCache =  new HashSet<>();

    public void stage(Address address){
        stageCache.add(address);
    }

    public Set<Address> export(){return stageCache;}

    public void clear() {
        stageCache.clear();
    }
}
