package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.Address;

import java.util.HashSet;
import java.util.Set;

/**
 * 待入库或更新地址实体阶段状态暂存类
 * User: dongqile
 * Date: 2019/8/14
 * Time: 16:46
 */
public class AddressStage {
    private Set<Address> addressInsertStage =  new HashSet<>();
    private Set<Address> addressUpdateStage =  new HashSet<>();
    public void insertAddress(Address address){
        addressInsertStage.add(address);
    }
    public void updateAddress(Address address){
        addressUpdateStage.add(address);
    }
    public void clear() {
        addressInsertStage.clear();
        addressUpdateStage.clear();
    }
    public Set<Address> exportAddress(){
        Set<Address> returnData = new HashSet<>(addressInsertStage);
        returnData.addAll(addressUpdateStage);
        return returnData;
    }

    public Set<Address> getAddressInsertStage() {
        return addressInsertStage;
    }
    public Set<Address> getAddressUpdateStage() {
        return addressUpdateStage;
    }


}
