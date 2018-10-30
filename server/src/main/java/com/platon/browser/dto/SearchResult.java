package com.platon.browser.dto;

import lombok.Data;

@Data
public class SearchResult<T> {
    private String type;
    private T struct;
}
