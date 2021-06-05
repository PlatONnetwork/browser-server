package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomAddressDetail;

public interface CustomAddressMapper {

    CustomAddressDetail findAddressDetail(String address);

}
