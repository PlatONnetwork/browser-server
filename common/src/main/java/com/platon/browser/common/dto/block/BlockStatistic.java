package com.platon.browser.common.dto.block;

import lombok.Data;

@Data
public class BlockStatistic {
    private int height;
    private int time;
    private int transaction;
}
