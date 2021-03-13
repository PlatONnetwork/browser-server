package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import lombok.Data;

import java.util.List;

@Data
public class Erc20TokenAddressRelEvent implements Event{
    private List<Erc20TokenAddressRel> erc20TokenAddressRelList;
}