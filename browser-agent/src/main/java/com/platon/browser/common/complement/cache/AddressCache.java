package com.platon.browser.common.complement.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platon.browser.dao.entity.Address;

import lombok.Data;

@Component
@Data
public class AddressCache {
    private Map<String,Address> addressMap = new HashMap<String, Address>();
}
