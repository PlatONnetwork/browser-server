package com.platon.browser.client.result;

import lombok.Data;

@Data
public class Log {
    private String data;
    private int logIndex;
    private boolean removed;
}