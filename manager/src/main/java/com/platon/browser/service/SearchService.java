package com.platon.browser.service;

import com.platon.browser.dto.SearchParam;
import com.platon.browser.dto.query.Query;

public interface SearchService {
    /**
     * 搜索
     */
    Query search(SearchParam param);
}
