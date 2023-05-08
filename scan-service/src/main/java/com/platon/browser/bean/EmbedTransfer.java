package com.platon.browser.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmbedTransfer {
    private String from;
    private String to;
    private BigDecimal amount;
}
