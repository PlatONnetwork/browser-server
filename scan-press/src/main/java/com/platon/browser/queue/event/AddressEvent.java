package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Address;
import lombok.Data;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Data
public class AddressEvent implements Event {

    private List<Address> addressList;

}