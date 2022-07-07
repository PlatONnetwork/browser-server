package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.ErcTx;
import lombok.Data;

@Data
public class ES721Bean {

    private String _id;

    private ErcTx _source;

}
