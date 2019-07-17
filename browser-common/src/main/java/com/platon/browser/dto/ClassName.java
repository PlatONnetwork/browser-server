package com.platon.browser.dto;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/1/9
 * Time: 11:34
 */
@Data
public class ClassName {
    private Class clazz;
    private String name;
    public ClassName ( Class clazz, String name ) {
        this.clazz = clazz;
        this.name = name;
    }
}