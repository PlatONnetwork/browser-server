package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;

@Data
public class ESBean {

    private String _id;

    private Transaction _source;

}
