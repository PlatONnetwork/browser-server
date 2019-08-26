package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.Address;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 地址统计信息新增或修改暂存类，入库后各容器需要清空
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
