package com.platon.browser.enums;

public enum Arc20TxGroupTypeEnum {
    FROM("from","from"),
    TO( "to","to"),
    CONTRACT("contract","contract");
    private String terms;
    private String field;
    Arc20TxGroupTypeEnum(String terms,String field) {
        this.terms = terms;
        this.field = field;
    }

    public String getTerms() {
        return terms;
    }

    public String getField() {
        return field;
    }
}
