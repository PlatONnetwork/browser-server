package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dto.CustomAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 地址统计信息新增或修改暂存类，入库后各容器需要清空
 */
public class AddressStage {
    private Set<CustomAddress> addressInsertStage =  new HashSet<>();
    private Set<CustomAddress> addressUpdateStage =  new HashSet<>();
    public void insertAddress(CustomAddress address){
        addressInsertStage.add(address);
    }
    public void updateAddress(CustomAddress address){
        addressUpdateStage.add(address);
    }
    public void clear() {
        addressInsertStage.clear();
        addressUpdateStage.clear();
    }

    public Set<CustomAddress> getAddressInsertStage() {
        return addressInsertStage;
    }
    public Set<CustomAddress> getAddressUpdateStage() {
        return addressUpdateStage;
    }

    public Set<Address> exportAddress(){
        Set<Address> returnData = new HashSet<>(addressInsertStage);
        returnData.addAll(addressUpdateStage);
        return returnData;
    }
}
