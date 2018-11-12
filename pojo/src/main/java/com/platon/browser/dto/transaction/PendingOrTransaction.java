package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class PendingOrTransaction {
    private String type; // transaction , pending
    private PendingTxDetail data;
}
