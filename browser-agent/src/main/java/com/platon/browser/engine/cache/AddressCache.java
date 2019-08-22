package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomAddress;
import com.platon.browser.exception.NoSuchBeanException;

import java.util.HashMap;
import java.util.Map;

/**
 * 地址实体进程缓存
 * @Auther: Chendongming
 * @Date: 2019/8/17 18:03
 * @Description:
 */
public class AddressCache {
    // <节点ID - 地址实体>
    private Map<String, CustomAddress> addressMap = new HashMap<>();
    /**
     * 根据地址获取地址实体
     * @param address
     * @return
     * @throws NoSuchBeanException
     */
    public CustomAddress getAddress(String address) throws NoSuchBeanException {
        CustomAddress customAddress = addressMap.get(address);
        if(customAddress==null) throw new NoSuchBeanException("地址(id="+address+")对应的缓存不存在");
        return customAddress;
    }

    /**
     * 把地址实体添加进缓存中
     * @param address
     */
    public void add(CustomAddress address){
        addressMap.put(address.getAddress(),address);
    }
}
