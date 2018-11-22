package com.platon.browser.service;

import com.platon.browser.dto.search.SearchResult;
import com.platon.browser.req.search.SearchReq;

public interface SearchService {
    /**
     * 搜索
     */
    SearchResult<?> search(SearchReq param);
}
